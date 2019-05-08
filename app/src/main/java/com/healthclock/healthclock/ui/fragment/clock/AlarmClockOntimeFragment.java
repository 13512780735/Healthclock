package com.healthclock.healthclock.ui.fragment.clock;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthclock.healthclock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmClockOntimeFragment extends Fragment {


    public AlarmClockOntimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_clock_ontime, container, false);
    }

}
