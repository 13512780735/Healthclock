package com.healthclock.healthclock.clock.activity;

import android.support.v4.app.Fragment;

import com.healthclock.healthclock.clock.fragment.RingSelectFragment;


public class RingSelectActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RingSelectFragment();
    }

}
