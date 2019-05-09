package com.healthclock.healthclock.ui.activity.clock;

import android.support.v4.app.Fragment;

import com.healthclock.healthclock.ui.fragment.clock.AlarmClockOntimeFragment;

public abstract class AlarmClockOntimeActivity extends SingleFragmentDialogActivity {

    @Override
    protected Fragment createFragment() {
        return new AlarmClockOntimeFragment();
    }

    @Override
    public void onBackPressed() {
        // 禁用back键
    }

}
