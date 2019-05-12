package com.healthclock.healthclock.ui.activity.member;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.activity.login.EditInformationActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;

import butterknife.BindView;

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
                toActivity(EditInformationActivity.class);
            }
        });

        etAge.setText("35");
        etHeight.setText("175");
        etWeight.setText("140");
        etBlood.setText("120");
        etHeart.setText("80");
    }
}
