package com.healthclock.healthclock.clock.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.clock.activity.base.BaseActivity;

public abstract class SingleFragmentActivity extends BaseActivity {

    /**
     * 抽象方法：创建Fragment
     *
     * @return Fragment
     */
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_containers);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_containers, fragment)
                    .commit();

        }

    }
}
