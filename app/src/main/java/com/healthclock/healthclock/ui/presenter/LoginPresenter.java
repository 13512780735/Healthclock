package com.healthclock.healthclock.ui.presenter;

import com.elvishew.xlog.XLog;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.LoginView;

import rx.Subscriber;

public class LoginPresenter extends BasePresenter<LoginView> {
    //登录
    public void toLogin(String username, String password, String openid) {
        XLog.e("msg"+"2222");
        RetrofitUtil.getInstance().getUserLogin(username, password, openid, new Subscriber<BaseResponse<LoginRegisterBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<LoginRegisterBean> baseResponse) {
                XLog.e("msg"+baseResponse.getMsg());
                if (baseResponse.getStatus() == 1) {
                    LoginRegisterBean loginRegisterBean = baseResponse.getData();
                    getView().loginSuccess(loginRegisterBean);
                } else {
                    getView().loginFail();
                    getView().showProgress(baseResponse.getMsg());
                }
            }
        });

    }

}
