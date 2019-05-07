package com.healthclock.healthclock.ui.activity.main;

import android.support.v4.app.Fragment;


public class RingSelectActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RingSelectFragment();
    }

}
