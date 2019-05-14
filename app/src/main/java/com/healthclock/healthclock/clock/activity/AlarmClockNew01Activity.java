package com.healthclock.healthclock.clock.activity;

import android.support.v4.app.Fragment;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.clock.activity.SingleFragmentOrdinaryActivity;
import com.healthclock.healthclock.clock.fragment.AlarmClockNew01Fragment;
public class AlarmClockNew01Activity extends SingleFragmentOrdinaryActivity {

    @Override
    protected Fragment createFragment() {
        return new AlarmClockNew01Fragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 按下返回键开启渐变缩小动画
        overridePendingTransition(0, R.anim.zoomout);
    }
}