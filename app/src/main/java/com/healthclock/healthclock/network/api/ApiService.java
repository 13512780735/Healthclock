package com.healthclock.healthclock.network.api;

import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;


/**
 * Created by admin on 2018/5/11.
 */

public interface ApiService {
//    private static String registUrl = AppConst.BASE_URL + "member/reg";     //注册
//    private static String forgetUrl = AppConst.BASE_URL + "/member/forget";//忘记密码
//    private static String userInfoUrl = AppConst.BASE_URL + "/member/info";  //用户信息
//    private static String logoutUrl = AppConst.BASE_URL + "/member/logout";  //退出登录
//    private static String editUserInfoUrl = AppConst.BASE_URL + "/member/info/edit";  //编辑信息
//    private static String editpwdfoUrl = AppConst.BASE_URL + "/member/password";   //修改密码
//    private static String verificationCodeUrl = AppConst.BASE_URL + "/member/verificationCode";  //获取邀请码

    /**
     * 用户登录接口
     *
     * @param username
     * @param password
     * @param openid
     * @return
     */
    @FormUrlEncoded
    @POST("member/login")
    Observable<BaseResponse<LoginRegisterBean>> UserLogin(@Field("username") String username,
                                                          @Field("password") String password,
                                                          @Field("openid") String openid
    );

    /**
     * 用户注册
     * @param username
     * @param code
     * @param password
     * @param referralCode
     * @return
     */
    @FormUrlEncoded
    @POST("member/reg")
    Observable<BaseResponse<LoginRegisterBean>> UserRegister(@Field("username") String username,
                                                             @Field("code") String code,
                                                             @Field("password") String password,
                                                             @Field("referralCode") String referralCode
    );

    /**
     * 忘记密码
     * @param username
     * @param code
     * @param newpassword
     * @return
     */
    @FormUrlEncoded
    @POST("member/forget")
    Observable<BaseResponse<EmptyEntity>> UserForgetPwd(@Field("username") String username,
                                                       @Field("code") String code,
                                                       @Field("newpassword") String newpassword
    );

}
