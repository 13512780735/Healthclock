package com.healthclock.healthclock.ui.activity.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.healthclock.healthclock.R;
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
