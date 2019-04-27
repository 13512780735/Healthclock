package com.healthclock.healthclock.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;

public class WelComeActivity extends BaseActivity {
    private View view;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_wel_come);
        view = View.inflate(this, R.layout.activity_wel_come, null);
        setContentView(view);
        animation = AnimationUtils.loadAnimation(this, R.anim.splash_alpha);
        initUI();
    }

    private void initUI() {
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }   //在动画开始时使用

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }  //在动画重复时使用

            @Override
            public void onAnimationEnd(Animation arg0) {
                // toActivityFinish(LoginActivity.class);//
                Bundle bundle = new Bundle();
                bundle.putString("isLogin", "0");
                toActivity(LoginActivity.class, bundle);
                finish();

            }
        });
    }
}
