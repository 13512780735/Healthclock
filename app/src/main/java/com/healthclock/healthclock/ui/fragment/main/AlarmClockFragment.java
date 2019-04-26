package com.healthclock.healthclock.ui.fragment.main;


import android.support.v4.app.Fragment;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmClockFragment extends BaseFragment {


    public static AlarmClockFragment newInstance() {
        return new AlarmClockFragment();
    }
    @Override
    protected int setContentView() {
        return R.layout.fragment_alarm_clock;
    }

    @Override
    protected void lazyLoad() {

    }

}
