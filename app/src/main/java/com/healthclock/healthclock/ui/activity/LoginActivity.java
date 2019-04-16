package com.healthclock.healthclock.ui.activity;


import com.healthclock.healthclock.R;
import com.healthclock.healthclock.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.presenter.LoginPresenter;
import com.healthclock.healthclock.ui.view.LoginView;
import com.healthclock.healthclock.util.T;

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void showProgress(String tipString) {
        showWaitingDialog(tipString);
    }

    @Override
    public void hideProgress() {
        hideProgress();
    }

    @Override
    public void loginSuccess(LoginRegisterBean user) {

    }

    @Override
    public void loginFail() {
        T.showShort(mContext, "登录失败");
    }
}
