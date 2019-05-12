package com.healthclock.healthclock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.healthgo.step.StepService;
import com.healthclock.healthclock.ui.fragment.main.AlarmClockFragment;
import com.healthclock.healthclock.ui.fragment.main.MemberFragment;
import com.healthclock.healthclock.ui.fragment.main.ShopFragment;
import com.healthclock.healthclock.ui.adapter.FragPagerAdapter;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.AppManager;
import com.healthclock.healthclock.util.PermissionUtil;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.widget.IconFontTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.if_home)
    IconFontTextView ifHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.if_type)
    IconFontTextView ifType;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.if_user)
    IconFontTextView ifUser;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    private List<Fragment> mFragments = new ArrayList<>();


    EventBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bus = EventBus.getDefault();
        bus.register(this);
        PermissionUtil.requestPermission(this, com.yanzhenjie.permission.runtime.Permission.Group.STORAGE);
        initUI();

     // bus.post(true);
        Intent intent = new Intent(this, StepService.class);
        intent.putExtra("isActivity", true);
        if (!bus.isRegistered(this))
            bus.register(this);
        startService(intent);
        bus.post(true);
        Intent intent1 = new Intent(this, StepService.class);
        intent1.putExtra("foreground_model", "on");
        intent1.putExtra("isActivity", true);
        if (!bus.isRegistered(this))
            bus.register(this);
        bus.post(true);
        startService(intent1);
    }

    private void initUI() {
        setTabColor(ifHome, tvHome);
        mFragments.add(AlarmClockFragment.newInstance());
        mFragments.add(ShopFragment.newInstance());
        mFragments.add(MemberFragment.newInstance());
        viewPager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(), mFragments));
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTabColor(ifHome, tvHome);
                        break;
                    case 1:
                        setTabColor(ifType, tvType);
                        break;
                    case 2:
                        setTabColor(ifUser, tvUser);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.ll_home, R.id.ll_type, R.id.ll_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                setTabColor(ifHome, tvHome);
                break;
            case R.id.ll_type:
                viewPager.setCurrentItem(1);
                setTabColor(ifType, tvType);
                break;
            case R.id.ll_user:
                viewPager.setCurrentItem(2);
                setTabColor(ifUser, tvUser);
                break;
        }
    }

    private void setTabColor(IconFontTextView icon, TextView textView) {
        ifHome.setTextColor(this.getResources().getColor(R.color.tab_nor_color));
        tvHome.setTextColor(this.getResources().getColor(R.color.tab_nor_color));
        ifType.setTextColor(this.getResources().getColor(R.color.tab_nor_color));
        tvType.setTextColor(this.getResources().getColor(R.color.tab_nor_color));
        ifUser.setTextColor(this.getResources().getColor(R.color.tab_nor_color));
        tvUser.setTextColor(this.getResources().getColor(R.color.tab_nor_color));
        icon.setTextColor(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
        textView.setTextColor(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                //finish();
                AppManager.getAppManager().finishAllActivity();
                // System.exit(0);
            } else {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.post(false);
        if (bus.isRegistered(this))
            bus.unregister(this);
    }
}
