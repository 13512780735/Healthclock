package com.healthclock.healthclock.network.util;


import com.healthclock.healthclock.BuildConfig;
import com.healthclock.healthclock.network.Appconst.AppConst;
import com.healthclock.healthclock.network.api.ApiService;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.good.ShopListModel;
import com.healthclock.healthclock.network.model.health.healthInfoModel;
import com.healthclock.healthclock.network.model.indent.AddressModel;
import com.healthclock.healthclock.network.model.indent.CreateOrderModel;
import com.healthclock.healthclock.network.model.indent.DefaultAddressModel;
import com.healthclock.healthclock.network.model.indent.OrderListModel;
import com.healthclock.healthclock.network.model.other.VideoModel;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.network.model.user.UserInfoModel;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo.util
 * @date 2016/12/12  10:38
 */

public class RetrofitUtil {

    public static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private static RetrofitUtil mInstance;


    /**
     * 私有构造方法
     */
    private RetrofitUtil() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG)

        {
            //打印网络请求日志
            LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .build();
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);

        }


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .baseUrl(AppConst.BASE_URL)
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param openid
     * @param subscriber
     */

    public void getUserLogin(String username, String password, String
            openid, Subscriber<BaseResponse<LoginRegisterBean>> subscriber) {
        mApiService.UserLogin(username, password, openid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 退出登录
     *
     * @param token
     * @param subscriber
     */
    public void UserLogout(String token, Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.UserLogout(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 注册
     *
     * @param username
     * @param code
     * @param password
     * @param referralCode
     * @param subscriber
     */
    public void getUserRegister(String username, String code, String password, String
            referralCode, Subscriber<BaseResponse<LoginRegisterBean>> subscriber) {
        mApiService.UserRegister(username, code, password, referralCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 忘记密码
     *
     * @param username
     * @param code
     * @param newpassword
     * @param subscriber
     */
    public void getUserForgetPwd(String username, String code, String
            newpassword, Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.UserForgetPwd(username, code, newpassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @param subscriber
     */
    public void GetUserInfo(String token,
                            Subscriber<BaseResponse<UserInfoModel>> subscriber) {
        mApiService.GetUserInfo(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 編輯
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
     * @param subscriber
     */
    public void UserInfoEdit(String token, String head_img, String phone, String nickname, String real_name, String email, String sex, String personalSign, String card, String remark, Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.UserInfoEdit(token, head_img, phone, nickname, real_name, email, sex, personalSign, card, remark)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 上传头像
     *
     * @param token
     * @param file
     * @param subscriber
     */
    public void UserUploadImg(RequestBody token, MultipartBody.Part file, Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.UserUploadImg(token, file)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

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
     * @param subscriber
     */

    public void getUserHealthSave(String token, String age, String blood, String heart, String height, String weight, String isArdiacPacemaker, String vision, String headache, String neckPain,
                                  String stomachache, String bellyache, String skin, String vigor, String morningWood, String runNumber, String remark, Subscriber<BaseResponse<healthInfoModel>> subscriber) {
        mApiService.UserHealthSave(token, age, blood, heart, height, weight, isArdiacPacemaker, vision, headache, neckPain, stomachache, bellyache, skin, vigor, morningWood, runNumber, remark)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

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
     * @param subscriber
     */
    public void addAddress(String token, String phone, String contact, String province, String city, String district, String detail,
                           Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.addAddress(token, phone, contact, province, city, district, detail)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取收货地址
     *
     * @param token
     * @param subscriber
     */
    public void GetAddressList(String token, String page,
                               Subscriber<BaseResponse<AddressModel>> subscriber) {
        mApiService.GetAddressList(token, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取用户默认地址
     *
     * @param token
     * @param subscriber
     */
    public void defaultAddress(String token,
                               Subscriber<BaseResponse<DefaultAddressModel>> subscriber) {
        mApiService.defaultAddress(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 设置默认地址
     *
     * @param token
     * @param subscriber
     */
    public void isDefaultAddress(String token, String id,
                                 Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.isDefaultAddress(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除地址
     *
     * @param token
     * @param subscriber
     */
    public void deleteAddress(String token, String id,
                              Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.deleteAddress(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 编辑地址
     *
     * @param token
     * @param subscriber
     */
    public void editAddress(String token, String id, String phone, String contact, String province, String city, String district, String detail,
                            Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.editAddress(token, id, phone, contact, province, city, district, detail)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 商品列表
     *
     * @param token
     * @param category
     * @param page
     * @param subscriber
     */
    public void GetShopList(String token, String category, String page,
                            Subscriber<BaseResponse<ShopListModel>> subscriber) {
        mApiService.GetShopList(token, category, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取订单列表
     *
     * @param token
     * @param status
     * @param page
     * @param subscriber
     */
    public void GetOrderList(String token, String status, String page,
                             Subscriber<BaseResponse<OrderListModel>> subscriber) {
        mApiService.GetOrderList(token, status, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 创建订单
     *
     * @param token
     * @param shopId
     * @param number
     * @param sum
     * @param discount
     * @param addressId
     * @param type
     * @param subscriber
     */
    public void CreateOrder(String token, String shopId, String number, String sum, String discount, String addressId, String type,
                            Subscriber<BaseResponse<CreateOrderModel>> subscriber) {
        mApiService.CreateOrder(token, shopId, number, sum, discount, addressId, type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除订单
     *
     * @param token
     * @param id
     * @param subscriber
     */
    public void deleteOrder(String token, String id,
                            Subscriber<BaseResponse<EmptyEntity>> subscriber) {
        mApiService.deleteOrder(token, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 视频记录
     * @param token
     * @param page
     * @param type
     * @param subscriber
     */
    public void VideoList(String token, String page, String type,
                          Subscriber<BaseResponse<VideoModel>> subscriber) {
        mApiService.VideoList(token, page, type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private <T> void toSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
