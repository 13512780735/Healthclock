package com.healthclock.healthclock.ui.presenter;

import com.healthclock.healthclock.api.WanService;
import com.healthclock.healthclock.helper.rxjavahelper.RxObserver;
import com.healthclock.healthclock.helper.rxjavahelper.RxResultHelper;
import com.healthclock.healthclock.helper.rxjavahelper.RxSchedulersHelper;
import com.healthclock.healthclock.model.ResponseData;
import com.healthclock.healthclock.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.RegisterView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * user：lqm
 * desc：登录注册
 */
public class LoginRegistPresenter extends BasePresenter<RegisterView> {
    //登录
//    public void toLogin(String username, String password) {
//
//        WanService.login(username, password)
//                .compose(RxSchedulersHelper.io_main())
//                .compose(RxResultHelper.handleResult())
//                .subscribe(new RxObserver<UserBean>() {
//
//                    @Override
//                    public void _onSubscribe(Disposable d) {
//                        getView().showProgress("正在登陆...");
//                    }
//
//                    @Override
//                    public void _onNext(UserBean userBean) {
//                        getView().loginSuccess(userBean);
//                    }
//
//                    @Override
//                    public void _onError(String errorMessage) {
//                        getView().loginFail();
//
//                    }
//
//                    @Override
//                    public void _onComplete() {
//                        getView().hideProgress();
//                    }
//
//                });
//    }

    //注册
    public void toRegist(String username, String code, String password) {
        WanService.regist(username, code, password)
                .compose(RxSchedulersHelper.<ResponseData<LoginRegisterBean>>io_main())
                .compose(RxResultHelper.<LoginRegisterBean>handleResult())
                .subscribe(new RxObserver<LoginRegisterBean>() {
                    @Override
                    public void _onNext(LoginRegisterBean loginRegisterBean) {
                        getView().registerSuccess(loginRegisterBean);
                    }

                    @Override
                    public void _onError(String errorMessage) {
                        getView().registerFail();
                    }

                    @Override
                    public void _onSubscribe(Disposable d) {
                        getView().showProgress("正在注册...");
                    }

                    @Override
                    public void _onComplete() {
                        getView().hideProgress();
                    }
                });

    }
}
