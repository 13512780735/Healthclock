package com.healthclock.healthclock.ui.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.presenter.LoginPresenter;
import com.healthclock.healthclock.ui.view.LoginView;
import com.healthclock.healthclock.util.T;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {
    @BindView(R.id.login_et_phone)
    EditText etPhone;
    @BindView(R.id.login_et_pwd)
    EditText etPwd;
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }

    @Override
    public void showProgress(String tipString) {
        showWaitingDialog(tipString);
    }

    @Override
    public void hideProgress() {
        hideProgress();
    }


    @OnClick({R.id.tv_login, R.id.tv_register_account, R.id.tv_forget_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                break;
            case R.id.tv_register_account:
                toActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                break;
        }
    }

    @Override
    public void loginSuccess(LoginRegisterBean user) {

    }

    @Override
    public void loginFail() {
        T.showShort(mContext, "登录失败");
    }
}
