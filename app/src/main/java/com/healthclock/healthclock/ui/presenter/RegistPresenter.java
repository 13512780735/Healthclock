package com.healthclock.healthclock.ui.presenter;

import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.RegisterView;

import rx.Subscriber;

/**
 * user：lqm
 * desc：登录注册
 */
public class RegistPresenter extends BasePresenter<RegisterView> {


    //注册
    public void toRegist(String username, String code, String password, String referralCode) {
        RetrofitUtil.getInstance().getUserRegister(username, code, password, referralCode, new Subscriber<BaseResponse<LoginRegisterBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onStart() {
                getView().showProgress("正在注册...");
            }

            @Override
            public void onNext(BaseResponse<LoginRegisterBean> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    LoginRegisterBean loginRegisterBean = baseResponse.getData();
                    getView().registerSuccess(loginRegisterBean);
                } else {
                    getView().registerFail();
                }
            }
        });

    }
}
