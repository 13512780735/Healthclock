package com.healthclock.healthclock.ui.view;

import com.healthclock.healthclock.network.model.user.LoginRegisterBean;

public interface RegisterView {

    void showProgress(String tipString);

    void hideProgress();

    void registerSuccess(LoginRegisterBean user);
    void getDataError(String message);

    void registerFail();
}
