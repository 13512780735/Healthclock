package com.healthclock.healthclock.ui.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.activity.member.EditPwdActivity;
import com.healthclock.healthclock.ui.activity.member.FeedbackActivity;
import com.healthclock.healthclock.ui.activity.member.HealthManagementActivity;
import com.healthclock.healthclock.ui.activity.member.HealthRecordsActivity;
import com.healthclock.healthclock.ui.activity.member.HealthScoreActivity;
import com.healthclock.healthclock.ui.activity.member.IndentActivity;
import com.healthclock.healthclock.ui.activity.member.InvitationCodeActivity;
import com.healthclock.healthclock.ui.activity.member.MyWalletActivity;
import com.healthclock.healthclock.ui.activity.member.SelectAddressActivity;
import com.healthclock.healthclock.ui.activity.member.UserInfoActivity;
import com.healthclock.healthclock.ui.activity.member.VideoRecordActivity;
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

    @OnClick({R.id.rl_all_orders, R.id.ll_obligation, R.id.ll_shipments, R.id.ll_Receiving, R.id.ll_finish, R.id.tv_set, R.id.tv_logout

    ,R.id.ll_wallet,R.id.ll_Invitation_code,R.id.ll_video,R.id.ll_health_management,R.id.ll_Health_records,R.id.ll_Health_score,R.id.ll_feedback,R.id.ll_edit_pwd
    ,R.id.ll_address})
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
             * 个人信息页面
              */
            case R.id.ll_wallet: //我的钱包
                toActivity(MyWalletActivity.class);
                break;
            case R.id.ll_Invitation_code://邀请码
                toActivity(InvitationCodeActivity.class);
                break;
            case R.id.ll_address://我的收货地址
                toActivity(SelectAddressActivity.class);
                break;
            case R.id.ll_video://视频播放记录
                toActivity(VideoRecordActivity.class);
                break;
            case R.id.ll_health_management://健康管理
                toActivity(HealthManagementActivity.class);
                break;
            case R.id.ll_Health_records://健康记录
                toActivity(HealthRecordsActivity.class);
                break;
//            case R.id.ll_Health_integral://健康积分
//                break;
            case R.id.ll_Health_score://健康评分
                toActivity(HealthScoreActivity.class);
                break;
            case R.id.ll_feedback://意见反馈
                toActivity(FeedbackActivity.class);
                break;
            case R.id.ll_edit_pwd://修改密码
                toActivity(EditPwdActivity.class);
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
                }else if (baseResponse.getStatus() == -1) {
                    T.showShort(getActivity(), baseResponse.getMsg());
                    Bundle bundle=new Bundle();
                    bundle.putString("isLogin","1");
                    toActivity(LoginActivity.class,bundle);
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
