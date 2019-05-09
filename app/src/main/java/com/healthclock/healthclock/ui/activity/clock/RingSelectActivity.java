package com.healthclock.healthclock.ui.activity.clock;

import android.support.v4.app.Fragment;

import com.healthclock.healthclock.ui.fragment.clock.RingSelectFragment;


public class RingSelectActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RingSelectFragment();
    }

}
