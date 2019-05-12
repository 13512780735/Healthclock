package com.healthclock.healthclock.ui.activity.member;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;

import butterknife.OnClick;

public class HealthManagementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_management);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("我的健康管理");
    }

    @OnClick({R.id.ll_Health_score, R.id.ll_Health_records})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_Health_records:
                toActivity(HealthRecordsActivity.class);
                break;
            case R.id.ll_Health_score:
                toActivity(HealthScoreActivity.class);
                break;
        }
    }
}
