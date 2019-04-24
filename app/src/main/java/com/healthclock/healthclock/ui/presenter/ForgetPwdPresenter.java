package com.healthclock.healthclock.ui.presenter;

import com.healthclock.healthclock.network.api.WanService;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxObserver;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxResultHelper;
import com.healthclock.healthclock.network.helper.rxjavahelper.RxSchedulersHelper;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.ResponseData;
import com.healthclock.healthclock.ui.base.BasePresenter;
import com.healthclock.healthclock.ui.view.ForgetPwdView;
import com.healthclock.healthclock.util.L;


/**
 * user：lqm
 * desc：忘记密码
 */
public class ForgetPwdPresenter extends BasePresenter<ForgetPwdView> {


    //忘记密码
    public void toFindPwd(String username, String code, String newpassword) {
        WanService.UserForgetPwd(username, code, newpassword)
                .compose(RxSchedulersHelper.<ResponseData<EmptyEntity>>io_main())
                .compose(RxResultHelper.<EmptyEntity>handleResult())
                .subscribe(new RxObserver<EmptyEntity>() {
                    @Override
                    public void _onNext(EmptyEntity emptyEntity) {
                        L.d("执行了");
                        getView().editSuccess();
                    }

                    @Override
                    public void _onError(String errorMessage) {
                        L.d(errorMessage);
                        getView().editFail(errorMessage);
                    }

                    @Override
                    public void _onComplete() {
                        getView().hideProgress();
                    }

                    @Override
                    public void _onSubscribe(String errorMessage) {
                        L.printJson("TAG",errorMessage,"");
                       // getView().showProgress(errorMessage);
                    }

                });


    }
}
