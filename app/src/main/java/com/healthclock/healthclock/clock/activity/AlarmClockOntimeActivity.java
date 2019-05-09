package com.healthclock.healthclock.clock.activity;

import android.support.v4.app.Fragment;

import com.healthclock.healthclock.clock.fragment.AlarmClockOntimeFragment;

public  class AlarmClockOntimeActivity extends SingleFragmentDialogActivity {

    @Override
    protected Fragment createFragment() {
        return new AlarmClockOntimeFragment();
    }

    @Override
    public void onBackPressed() {
        // 禁用back键
    }

}
