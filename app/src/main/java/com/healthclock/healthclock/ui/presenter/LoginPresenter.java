package com.healthclock.healthclock.ui.presenter;

import com.healthclock.healthclock.network.api.WanService;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxObserver;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxResultHelper;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxSchedulersHelper;
import com.healthclock.healthclock.network.model.ResponseData;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.LoginView;



import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BasePresenter<LoginView> {
    //登录
    public void toLogin(String username, String password, String openid) {
        WanService.UserLogin(username, password, openid)
                .compose(RxSchedulersHelper.<ResponseData<LoginRegisterBean>>io_main())
                .compose(RxResultHelper.<LoginRegisterBean>handleResult())
                .subscribe(new RxObserver<LoginRegisterBean>() {
                    @Override
                    public void _onNext(LoginRegisterBean loginRegisterBean) {
                        getView().loginSuccess(loginRegisterBean);
                    }

                    @Override
                    public void _onComplete() {
                        getView().hideProgress();
                    }

                    @Override
                    public void _onSubscribe(String d) {
                        getView().showProgress("正在登陆...");
                    }

                    @Override
                    public void _onError(String errorMessage) {
                        getView().loginFail();
                    }

                });

    }
}

