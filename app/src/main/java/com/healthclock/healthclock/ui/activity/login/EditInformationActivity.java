package com.healthclock.healthclock.ui.activity.login;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.health.healthInfoModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.T;

import butterknife.BindView;
import butterknife.OnClick;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("健康评分");
        setRightText("跳过", new View.OnClickListener() {
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
    }

    @OnClick({R.id.tv_getStep, R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getStep://获取步数
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
                }
                T.showShort(mContext,baseResponse.getMsg());
            }
        });
    }
}
