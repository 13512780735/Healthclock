package com.healthclock.healthclock.network.api;

import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.good.ShopListModel;
import com.healthclock.healthclock.network.model.health.healthInfoModel;
import com.healthclock.healthclock.network.model.indent.AddressModel;
import com.healthclock.healthclock.network.model.indent.CreateOrderModel;
import com.healthclock.healthclock.network.model.indent.DefaultAddressModel;
import com.healthclock.healthclock.network.model.indent.OrderListModel;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.network.model.user.UserInfoModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
     * 用户
     */


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
     * 退出登录
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("member/logout")
    Observable<BaseResponse<EmptyEntity>> UserLogout(@Field("token") String token
    );

    /**
     * 用户注册
     *
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
     *
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

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     */

    @FormUrlEncoded
    @POST("member/info")
    Observable<BaseResponse<UserInfoModel>> GetUserInfo(@Field("token") String token
    );

    /**
     * 编辑用户
     *
     * @param token
     * @param head_img
     * @param phone
     * @param nickname
     * @param real_name
     * @param email
     * @param sex
     * @param personalSign
     * @param card
     * @param remark
     * @return
     */
    @FormUrlEncoded
    @POST("member/info/edit")
    Observable<BaseResponse<EmptyEntity>> UserInfoEdit(@Field("token") String token,
                                                       @Field("head_img") String head_img,
                                                       @Field("phone") String phone,
                                                       @Field("nickname") String nickname,
                                                       @Field("real_name") String real_name,
                                                       @Field("email") String email,
                                                       @Field("sex") String sex,
                                                       @Field("personalSign") String personalSign,
                                                       @Field("card") String card,
                                                       @Field("remark") String remark
    );

    /**
     * 上傳頭像
     *
     * @param token
     * @param file
     * @return
     */
    @Multipart
    @POST("member/upload/header")
    Observable<BaseResponse<EmptyEntity>> UserUploadImg(@Part("token") RequestBody token,
                                                        @Part MultipartBody.Part file
    );

    /**
     * 健康信息
     */

    /**
     * 健康信息编辑
     *
     * @param token
     * @param age
     * @param blood
     * @param heart
     * @param height
     * @param weight
     * @param isArdiacPacemaker
     * @param vision
     * @param headache
     * @param neckPain
     * @param stomachache
     * @param bellyache
     * @param skin
     * @param vigor
     * @param morningWood
     * @param runNumber
     * @param remark
     * @return
     */
    @FormUrlEncoded
    @POST("health/save")
    Observable<BaseResponse<healthInfoModel>> UserHealthSave(@Field("token") String token,
                                                             @Field("age") String age,
                                                             @Field("blood") String blood,
                                                             @Field("heart") String heart,
                                                             @Field("height") String height,
                                                             @Field("weight") String weight,
                                                             @Field("isArdiacPacemaker") String isArdiacPacemaker,
                                                             @Field("vision") String vision,
                                                             @Field("headache") String headache,
                                                             @Field("neckPain") String neckPain,
                                                             @Field("stomachache") String stomachache,
                                                             @Field("bellyache") String bellyache,
                                                             @Field("skin") String skin,
                                                             @Field("vigor") String vigor,
                                                             @Field("morningWood") String morningWood,
                                                             @Field("runNumber") String runNumber,
                                                             @Field("remark") String remark
    );

    /**
     * 获取地址列表
     *
     * @param token
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("address/list")
    Observable<BaseResponse<AddressModel>> GetAddressList(@Field("token") String token,
                                                          @Field("page") String page
    );

    /**
     * 添加地址
     *
     * @param token
     * @param phone
     * @param contact
     * @param province
     * @param city
     * @param district
     * @param detail
     * @return
     */
    @FormUrlEncoded
    @POST("address/add")
    Observable<BaseResponse<EmptyEntity>> addAddress(@Field("token") String token,
                                                     @Field("phone") String phone,
                                                     @Field("contact") String contact,
                                                     @Field("province") String province,
                                                     @Field("city") String city,
                                                     @Field("district") String district,
                                                     @Field("detail") String detail
    );

    /**
     * 获取用户默认地址
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("address/default")
    Observable<BaseResponse<DefaultAddressModel>> defaultAddress(@Field("token") String token
    );

    /**
     * 设置默认地址
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("address/isDefault")
    Observable<BaseResponse<EmptyEntity>> isDefaultAddress(@Field("token") String token,
                                                           @Field("id") String id
    );

    /**
     * 删除地址
     *
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("address/delete")
    Observable<BaseResponse<EmptyEntity>> deleteAddress(@Field("token") String token,
                                                        @Field("id") String id
    );

    /**
     * 编辑地址
     *
     * @param token
     * @param id
     * @param phone
     * @param contact
     * @param province
     * @param city
     * @param district
     * @param detail
     * @return
     */
    @FormUrlEncoded
    @POST("address/eidt")
    Observable<BaseResponse<EmptyEntity>> editAddress(@Field("token") String token,
                                                      @Field("id") String id,
                                                      @Field("phone") String phone,
                                                      @Field("contact") String contact,
                                                      @Field("province") String province,
                                                      @Field("city") String city,
                                                      @Field("district") String district,
                                                      @Field("detail") String detail
    );

    /**
     * 商品列表
     * @param token
     * @param category
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("shop/list")
    Observable<BaseResponse<ShopListModel>> GetShopList(@Field("token") String token,
                                                        @Field("category") String category,
                                                        @Field("page") String page
    );

    /**
     * 获取订单列表
     * 状态0.已取消 1.待付款 2.已付款 3.待发货 4.已完成
     * @param token
     * @param status
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("order/list")
    Observable<BaseResponse<OrderListModel>> GetOrderList(@Field("token") String token,
                                                          @Field("status") String status,
                                                          @Field("page") String page
    );

    /**
     * 创建订单
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("order/create")
    Observable<BaseResponse<CreateOrderModel>> CreateOrder(@Field("token") String token,
                                                           @Field("shopId") String shopId,
                                                           @Field("number") String number,
                                                           @Field("sum") String sum,
                                                           @Field("discount") String discount,
                                                           @Field("addressId") String addressId,
                                                           @Field("type") String type
    );

    /**
     * 删除订单
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("order/delete")
    Observable<BaseResponse<EmptyEntity>> deleteOrder(@Field("token") String token,
                                                           @Field("id") String id
    );
}
