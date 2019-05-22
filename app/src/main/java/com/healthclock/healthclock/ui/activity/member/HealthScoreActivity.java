package com.healthclock.healthclock.ui.activity.member;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.healthgo.DateTimeHelper;
import com.healthclock.healthclock.healthgo.model.StepModel;
import com.healthclock.healthclock.ui.activity.login.EditInformationActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import io.realm.Realm;

public class HealthScoreActivity extends BaseActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_score);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("我的健康评分");
        setRightText("编辑", 14, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("flag", "1");
                toActivity(EditInformationActivity.class, bundle);
            }
        });

        etAge.setText("35");
        etHeight.setText("175");
        etWeight.setText("140");
        etBlood.setText("120");
        etHeart.setText("80");


         /*

        步数
         */
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

    public void updateShowSteps() {
        String text = "" + numSteps;

        if (numSteps >= 10000000)
            et_step.setTextSize(14);

        else if (numSteps >= 1000000)
            et_step.setTextSize(14);
        else if (numSteps >= 100000)
            et_step.setTextSize(55);
        else if (numSteps >= 10000) {
            notifyIsUpToStandard("太棒了，你今天超过1万步了");
            et_step.setTextSize(14);
        } else {
            et_step.setTextSize(14);
            if (numSteps >= 5000) notifyIsUpToStandard("加油，你已经再走走你就达到1万步了");
            else notifyIsUpToStandard("你今天都没怎么走路，快出门运动吧");
        }
        et_step.setText(text);

    }

    private void notifyIsUpToStandard(String msg) {
        App app = (App) getApplication();
        if (!app.isShowToast()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            app.setShowToast(true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(false);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
