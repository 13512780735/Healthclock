package com.healthclock.healthclock.ui.activity.login;

import android.os.Bundle;
import android.view.View;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.activity.MainActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.AppManager;

public class EditInformationActivity extends BaseActivity {

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
                toActivityFinish(PerfectInformationActivity.class);
            }
        });
    }
}
