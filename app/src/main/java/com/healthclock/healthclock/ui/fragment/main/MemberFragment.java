package com.healthclock.healthclock.ui.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.activity.member.IndentActivity;
import com.healthclock.healthclock.ui.activity.member.UserInfoActivity;
import com.healthclock.healthclock.ui.base.BaseFragment;
import com.healthclock.healthclock.util.AppManager;
import com.healthclock.healthclock.util.T;

import android.view.View;

import butterknife.OnClick;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends BaseFragment {

    private int status;

    public static MemberFragment newInstance() {
        return new MemberFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_member;
    }

    @Override
    protected void lazyLoad() {
        initUserInfo();
    }

    private void initUserInfo() {
    }

    @OnClick({R.id.rl_all_orders, R.id.ll_obligation, R.id.ll_shipments, R.id.ll_Receiving, R.id.ll_finish, R.id.tv_set, R.id.tv_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 订单
             */
            case R.id.rl_all_orders:
                status = 0;
                startIndentActivity(status);
                break;
            case R.id.ll_obligation:
                status = 1;
                startIndentActivity(status);
                break;
            case R.id.ll_shipments:
                status = 2;
                startIndentActivity(status);
                break;
            case R.id.ll_Receiving:
                status = 3;
                startIndentActivity(status);
                break;
            case R.id.ll_finish:
                status = 4;
                startIndentActivity(status);
                break;
            /**
             * 设置
             */
            case R.id.tv_set:
                toActivity(UserInfoActivity.class);
                break;
            case R.id.tv_logout:

                logout();
                break;
        }
    }

    /**
     * 退出
     */
    private void logout() {
        RetrofitUtil.getInstance().UserLogout(token, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    T.showShort(getActivity(), baseResponse.getMsg());
                    toActivityFinish(LoginActivity.class);
                    AppManager.getAppManager().finishAllActivity();
                } else if (baseResponse.getStatus() == -1) {
                    T.showShort(getActivity(), baseResponse.getMsg());
                    toActivityFinish(LoginActivity.class);
                    AppManager.getAppManager().finishAllActivity();
                } else {
                    T.showShort(getActivity(), baseResponse.getMsg());
                }
            }
        });
    }

    /**
     * 订单页面
     *
     * @param status
     */
    private void startIndentActivity(int status) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        Intent intent = new Intent(getActivity(), IndentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
