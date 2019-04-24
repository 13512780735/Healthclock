package com.healthclock.healthclock.network.api;


import com.healthclock.healthclock.network.Appconst.AppConst;
import com.healthclock.healthclock.network.helper.JsonConvert;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.ResponseData;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.lzy.okgo.OkGo;
import com.lzy.okrx2.adapter.ObservableBody;

import io.reactivex.Observable;

/**
 * user：lqm
 * desc：玩Android提供的api接口
 * www.wanandroid.com
 */

public class WanService {
    private static String loginUrl = AppConst.BASE_URL + "/member/login";//登录
    private static String registUrl = AppConst.BASE_URL + "member/reg";//注册
    private static String forgetUrl = AppConst.BASE_URL + "/member/forget";//忘记密码
    private static String userInfoUrl = AppConst.BASE_URL + "/member/info";  //用户信息
    private static String logoutUrl = AppConst.BASE_URL + "/member/logout";  //退出登录
    private static String editUserInfoUrl = AppConst.BASE_URL + "/member/info/edit";  //编辑信息
    private static String editpwdfoUrl = AppConst.BASE_URL + "/member/password";   //修改密码
    private static String verificationCodeUrl = AppConst.BASE_URL + "/member/verificationCode";  //获取邀请码

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param openid
     * @return
     */
    public static Observable<ResponseData<LoginRegisterBean>> UserLogin(String username, String password, String openid) {
        return OkGo.<ResponseData<LoginRegisterBean>>post(loginUrl)
                .params("username", username)
                .params("password", password)
                .params("openid", openid)
                .converter(new JsonConvert<ResponseData<LoginRegisterBean>>() {
                })
                .adapt(new ObservableBody<ResponseData<LoginRegisterBean>>());
    }

    /**
     * 注册
     *
     * @param username
     * @param code
     * @param password
     * @param referralCode
     * @return
     */
    public static Observable<ResponseData<LoginRegisterBean>> UserRegister(String username, String code, String password, String referralCode) {
        return OkGo.<ResponseData<LoginRegisterBean>>post(registUrl)
                .params("username", username)
                .params("code", code)
                .params("password", password)
                .params("referralCode", referralCode)
                .converter(new JsonConvert<ResponseData<LoginRegisterBean>>() {
                })
                .adapt(new ObservableBody<ResponseData<LoginRegisterBean>>());
    }

    public static Observable<ResponseData<EmptyEntity>> UserForgetPwd(String username, String code, String newpassword) {
        return OkGo.<ResponseData<EmptyEntity>>post(forgetUrl)
                .params("username", username)
                .params("code", code)
                .params("newpassword", newpassword)
                .converter(new JsonConvert<ResponseData<EmptyEntity>>() {
                })
                .adapt(new ObservableBody<ResponseData<EmptyEntity>>());
    }


}
