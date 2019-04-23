package com.healthclock.healthclock.ui.presenter;

import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.ForgetPwdView;

import rx.Subscriber;

/**
 * user：lqm
 * desc：忘记密码
 */
public class ForgetPwdPresenter extends BasePresenter<ForgetPwdView> {


    //忘记密码
    public void toFindPwd(String username, String code, String newpassword) {
        RetrofitUtil.getInstance().getUserForgetPwd(username, code, newpassword, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    getView().showProgress(baseResponse.getMsg());
                } else {
                    getView().showProgress(baseResponse.getMsg());
                }
            }
        });

    }
}
