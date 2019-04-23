package com.healthclock.healthclock.ui.activity;

import android.os.Bundle;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.presenter.ForgetPwdPresenter;
import com.healthclock.healthclock.ui.view.ForgetPwdView;

public class ForgetPwdActivity extends BaseActivity<ForgetPwdView, ForgetPwdPresenter> implements ForgetPwdView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
    }

    @Override
    protected ForgetPwdPresenter createPresenter() {
        return new ForgetPwdPresenter();
    }

    @Override
    public void showProgress(String tipString) {
        showWaitingDialog(tipString);
    }
}
