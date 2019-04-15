package com.healthclock.healthclock.ui.view;


import com.healthclock.healthclock.model.user.LoginRegisterBean;

/**
 * user：lqm
 * desc：登录注册
 */

public interface LoginView {


    void showProgress(String tipString);

    void hideProgress();

    void loginSuccess(LoginRegisterBean user);

    void loginFail();


}
