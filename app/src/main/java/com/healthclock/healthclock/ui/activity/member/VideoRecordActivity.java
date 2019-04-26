package com.healthclock.healthclock.ui.activity.member;

import android.os.Bundle;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;

public class VideoRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("视频播放记录");
    }
}
