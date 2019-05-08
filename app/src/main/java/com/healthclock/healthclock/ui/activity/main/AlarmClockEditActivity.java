package com.healthclock.healthclock.ui.activity.main;

import android.support.v4.app.Fragment;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.fragment.clock.AlarmClockEditFragment;

public class AlarmClockEditActivity extends SingleFragmentOrdinaryActivity {

    @Override
    protected Fragment createFragment() {
        return new AlarmClockEditFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 按下返回键开启移动退出动画
        overridePendingTransition(0, R.anim.move_out_bottom);
    }

}
