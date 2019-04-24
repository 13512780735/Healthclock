package com.healthclock.healthclock.ui.presenter;

import com.healthclock.healthclock.network.api.WanService;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxObserver;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxResultHelper;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxSchedulersHelper;
import com.healthclock.healthclock.network.model.ResponseData;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.RegisterView;

import io.reactivex.disposables.Disposable;


/**
 * user：lqm
 * desc：登录注册
 */
public class RegistPresenter extends BasePresenter<RegisterView> {


    //注册
    public void toRegist(String username, String code, String password, String referralCode) {
        WanService.UserRegister(username, code, password, referralCode)
                .compose(RxSchedulersHelper.<ResponseData<LoginRegisterBean>>io_main())
                .compose(RxResultHelper.<LoginRegisterBean>handleResult())
                .subscribe(new RxObserver<LoginRegisterBean>() {
                    @Override
                    public void _onNext(LoginRegisterBean loginRegisterBean) {
                        getView().registerSuccess(loginRegisterBean);
                    }

                    @Override
                    public void _onComplete() {
                        getView().hideProgress();
                    }

                    @Override
                    public void _onSubscribe(String d) {
                        getView().showProgress("正在注册...");
                    }

                    @Override
                    public void _onError(String errorMessage) {
                        getView().registerFail();
                    }

                });


    }
}
