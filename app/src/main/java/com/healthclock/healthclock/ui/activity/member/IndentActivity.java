package com.healthclock.healthclock.ui.activity.member;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.adapter.FragPagerAdapter;
import com.healthclock.healthclock.ui.adapter.IndentTabAdapter;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.fragment.indent.AllIndentFragment;
import com.healthclock.healthclock.ui.fragment.indent.Indent01Fragment;
import com.healthclock.healthclock.ui.fragment.indent.Indent02Fragment;
import com.healthclock.healthclock.ui.fragment.indent.Indent03Fragment;
import com.healthclock.healthclock.ui.fragment.indent.Indent04Fragment;
import com.healthclock.healthclock.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndentActivity extends BaseActivity {
    private List<String> mTitles;
    private TabLayout mTabLayout;
    private int status;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent);
        mTitles = new ArrayList<>(Arrays.asList("全部", "待付款", "待发货", "待收货", "已完成"));
        status = getIntent().getExtras().getInt("status");
        Log.e("TAG", status + "");
        initUI();
        // initData();
    }

    private void initUI() {
        setBackView();
        setTitle("订单记录");
        mTabLayout = (TabLayout) findViewById(R.id.indent_TabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
        mTabLayout.setTabTextColors(Color.parseColor("#000000"), StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
        List<Fragment> mfragments = new ArrayList<Fragment>();
        mfragments.add(new AllIndentFragment());
        mfragments.add(new Indent01Fragment());
        mfragments.add(new Indent02Fragment());
        mfragments.add(new Indent03Fragment());
        mfragments.add(new Indent04Fragment());
        mViewPager.setAdapter(new IndentTabAdapter(getSupportFragmentManager(), mfragments, mTitles) {
        });
        mViewPager.setCurrentItem(status);
    }


}
