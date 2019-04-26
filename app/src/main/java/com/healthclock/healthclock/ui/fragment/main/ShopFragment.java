package com.healthclock.healthclock.ui.fragment.main;


import android.support.v4.app.Fragment;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void lazyLoad() {

    }

}
