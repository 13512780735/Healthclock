package com.healthclock.healthclock.ui.activity.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;

public class MyVideoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        intUI();
    }

    private void intUI() {
        setBackView();
        setTitle("现在做操");
    }
}
