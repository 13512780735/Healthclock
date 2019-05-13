package com.healthclock.healthclock.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.healthgo.DateTimeHelper;
import com.healthclock.healthclock.healthgo.model.StepModel;
import com.healthclock.healthclock.healthgo.step.StepService;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.health.healthInfoModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.T;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import rx.Subscriber;

public class EditInformationActivity extends BaseActivity {

    @BindView(R.id.et_age)
    EditText etAge; //年齡
    @BindView(R.id.et_height)
    EditText etHeight; //身高
    @BindView(R.id.et_weight)
    EditText etWeight; //体重
    @BindView(R.id.et_blood)
    EditText etBlood; //血压
    @BindView(R.id.et_heart)
    EditText etHeart; //心率
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup; //是否用起搏器
    @BindView(R.id.et_eyesight_left)
    EditText etEyesightLeft; //左视力
    @BindView(R.id.et_eyesight_right)
    EditText etEyesightRight; //右视力
    @BindView(R.id.et_headache)
    EditText etHheadache; //头疼
    @BindView(R.id.et_Neck)
    EditText etNeck; //颈肩腰
    @BindView(R.id.et_stomachache)
    EditText etStomachache; //胃痛
    @BindView(R.id.et_bellyache)
    EditText etBellyache; //腹痛
    @BindView(R.id.et_skin)
    EditText etSkin; //肤白嫩
    @BindView(R.id.et_vigor)
    EditText etVigor; //精力态
    @BindView(R.id.et_wood)
    EditText et_wood; //晨勃
    @BindView(R.id.et_step)
    EditText et_step; //今日步


    SharedPreferences sharedPreferences;
    long numSteps;
    boolean isServiceRun;
    boolean isforeground_model;

    EventBus bus;
    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        initUI();
        bus = EventBus.getDefault();
        bus.register(this);
        // bus.post(true);
        Intent intent = new Intent(this, StepService.class);
        intent.putExtra("isActivity", true);
        if (!bus.isRegistered(this))
            bus.register(this);
        startService(intent);
        bus.post(true);
        Intent intent1 = new Intent(this, StepService.class);
        intent1.putExtra("foreground_model", "on");
        intent1.putExtra("isActivity", true);
        if (!bus.isRegistered(this))
            bus.register(this);
        bus.post(true);
        startService(intent1);
    }

    private void initUI() {
        setBackView();
        setTitle("健康评分");
        setRightText("跳过", 14,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PerfectInformationActivity.class);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        isArdiacPacemaker = "1";
                        break;
                    case R.id.radioButton1:
                        isArdiacPacemaker = "0";
                        break;
                }
            }
        });

        sharedPreferences = getSharedPreferences("conf", MODE_PRIVATE);

        detectService();


        Realm realm = Realm.getDefaultInstance();
        StepModel result = realm.where(StepModel.class)
                .equalTo("date", DateTimeHelper.getToday())
                .findFirst();
        numSteps = result == null ? 0 : result.getNumSteps();

        updateShowSteps();
        realm.close();


    }

    @OnClick({R.id.tv_getStep, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getStep://获取步数
                updateShowSteps();
                break;
            case R.id.tv_confirm:
                saveInfo();
                break;
        }
    }
    String vision="";
    String isArdiacPacemaker="0";
    private void saveInfo() {
        String age = etAge.getText().toString().trim();
        String blood = etBlood.getText().toString().trim();
        String heart = etHeart.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String visionLeft = etEyesightLeft.getText().toString().trim();
        String visionRight = etEyesightRight.getText().toString().trim();
        String headache = etHheadache.getText().toString().trim();
        String neckPain = etNeck.getText().toString().trim();
        String stomachache = etStomachache.getText().toString().trim();
        String bellyache = etBellyache.getText().toString().trim();
        String skin = etSkin.getText().toString().trim();
        String vigor = etVigor.getText().toString().trim();
        String morningWood = et_wood.getText().toString().trim();
        String runNumber = et_step.getText().toString().trim();
        String remark = "";
        vision=visionLeft+","+visionRight;

        RetrofitUtil.getInstance().getUserHealthSave(token, age, blood, heart, height, weight, isArdiacPacemaker, vision, headache, neckPain, stomachache, bellyache, skin, vigor, morningWood, runNumber, remark, new Subscriber<BaseResponse<healthInfoModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<healthInfoModel> baseResponse) {
                if(baseResponse.getStatus()==1){
                    T.showShort(mContext,baseResponse.getMsg());
                    toActivity(PerfectInformationActivity.class);
                }else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    Bundle bundle=new Bundle();
                    bundle.putString("isLogin","1");
                    toActivity(LoginActivity.class,bundle);
                }else {
                T.showShort(mContext,baseResponse.getMsg());}
            }
        });
    }
    public void updateShowSteps() {
        String text = "" + numSteps;

        if (numSteps >= 10000000)
            et_step.setTextSize(14);

        else if (numSteps >= 1000000)
            et_step.setTextSize(14);
        else if (numSteps >= 100000)
            et_step.setTextSize(55);
        else if (numSteps >= 10000) {
            notifyIsUpToStandard( "太棒了，你今天超过1万步了");
            et_step.setTextSize(14);
        }

        else {
            et_step.setTextSize(14);
            if (numSteps>=5000) notifyIsUpToStandard("加油，你已经再走走你就达到1万步了");
            else notifyIsUpToStandard("你今天都没怎么走路，快出门运动吧");
        }
        et_step.setText(text);

    }

    private void notifyIsUpToStandard(String msg)
    {
        App app = (App) getApplication();
        if(!app.isShowToast()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            app.setShowToast(true);
        }

    }
    public void detectService() {
        App app = (App) getApplication();
        isServiceRun = app.getServiceRun();
        boolean temp = sharedPreferences.getBoolean("switch_on", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isServiceRun != temp) {
            if (!isServiceRun) {
                Toast.makeText(getApplicationContext(), "计步服务意外终止,请把应用加入白名单",
                        Toast.LENGTH_LONG).show();
            }
            editor.putBoolean("switch_on", isServiceRun);
            editor.apply();
        }

        temp = sharedPreferences.getBoolean("foreground_model", false);
        if (temp && !isServiceRun) {
            editor.putBoolean("foreground_model", false);
            editor.apply();
            isforeground_model = false;
        } else isforeground_model = temp;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(false);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
