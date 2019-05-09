package com.healthclock.healthclock.clock.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.clock.activity.base.BaseActivityOrdinary;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

public abstract class SingleFragmentOrdinaryActivity extends BaseActivityOrdinary {
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟session的统计
        MobclickAgent.onResume(this);
    }
}
