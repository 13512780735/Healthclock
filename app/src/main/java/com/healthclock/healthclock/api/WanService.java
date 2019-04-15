package com.healthclock.healthclock.api;

import com.healthclock.healthclock.app.AppConst;
import com.healthclock.healthclock.helper.JsonConvert;
import com.healthclock.healthclock.model.ResponseData;
import com.healthclock.healthclock.model.user.LoginRegisterBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okrx2.adapter.ObservableBody;

import java.util.List;

import io.reactivex.Observable;

/**
 * user：lqm
 * desc：玩Android提供的api接口
 * www.wanandroid.com
 */

public class WanService {

    private static String loginUrl = AppConst.BASE_URL + "member/login";
    private static String registUrl = AppConst.BASE_URL + "member/reg";

    /**
     * 注册
     *
     * @param username
     * @param code
     * @param password
     * @return
     */
    public static Observable<ResponseData<LoginRegisterBean>> regist(String username, String code, String password) {
        return OkGo.<ResponseData<LoginRegisterBean>>post(registUrl)
                .params("username", username)
                .params("code", code)
                .params("password", password)
                .converter(new JsonConvert<ResponseData<LoginRegisterBean>>() {
                })
                .adapt(new ObservableBody<ResponseData<LoginRegisterBean>>());
    }

    /**
     * 登录
     *
     * @param username username
     * @param password password
     * @POST("/user/login")
     */
    public static Observable<ResponseData<LoginRegisterBean>> login(String username, String password, String openid) {
        return OkGo.<ResponseData<LoginRegisterBean>>post(loginUrl)
                .params("username", username)
                .params("password", password)
                .params("openid", openid)
                .converter(new JsonConvert<ResponseData<LoginRegisterBean>>() {
                })
                .adapt(new ObservableBody<ResponseData<LoginRegisterBean>>());
    }
//    /**
//     * 首页Banner
//     *
//     * @GET("/banner/json")
//     */
//    public static Observable<ResponseData<List<BannerBean>>> getBannerData() {
//        return OkGo.<ResponseData<List<BannerBean>>>get(homeBannerData)
//                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
//                .converter(new JsonConvert<ResponseData<List<BannerBean>>>() {
//                })
//                .adapt(new ObservableBody<ResponseData<List<BannerBean>>>());
//    }
//
//    /**
//     * 首页数据
//     * http://www.wanandroid.com/article/list/0/json
//     *
//     * @param page
//     * @GET("/article/list/{page}/json")
//     */
//    public static Observable<ResponseData<ArticleListVO>> getHomeData(int page) {
//        String url = AppConst.BASE_URL + "article/list/" + page + "/json";
//        return OkGo.<ResponseData<ArticleListVO>>get(url)
//                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //使用缓存
//                .converter(new JsonConvert<ResponseData<ArticleListVO>>() {
//                })
//                .adapt(new ObservableBody<ResponseData<ArticleListVO>>());
//    }
//
//    /**
//     * 知识体系 (类别tag)
//     * http://www.wanandroid.com/tree/json
//     *
//     * @GET("/tree/json")
//     */
//    public static Observable<ResponseData<List<TypeTagVO>>> getTypeTagData() {
//        String url = AppConst.BASE_URL + "tree/json";
//        return OkGo.<ResponseData<List<TypeTagVO>>>get(url)
//                .converter(new JsonConvert<ResponseData<List<TypeTagVO>>>() {
//                })
//                .adapt(new ObservableBody<ResponseData<List<TypeTagVO>>>());
//    }
//
//
//    /**
//     * 知识体系下的文章
//     * http://www.wanandroid.com/article/list/0/json?cid=168
//     *
//     * @param page page
//     * @param cid  cid
//     * @GET("/article/list/{page}/json")
//     */
//    public static Observable<ResponseData<ArticleListVO>> getTypeDataById(int page, int cid) {
//        String url = AppConst.BASE_URL + "article/list/" + page + "/json";
//        return OkGo.<ResponseData<ArticleListVO>>get(url)
//                .params("cid", cid)
//                .converter(new JsonConvert<ResponseData<ArticleListVO>>() {
//                })
//                .adapt(new ObservableBody<ResponseData<ArticleListVO>>());
//    }
//
//    /**
//     * 大家都在搜
//     * http://www.wanandroid.com/hotkey/json
//     *
//     * @GET("/hotkey/json")
//     */
//    public static Observable<ResponseData<List<HotKeyBean>>> getHotKey() {
//        return OkGo.<ResponseData<List<HotKeyBean>>>get(hotKeyUrl)
//                .converter(new JsonConvert<ResponseData<List<HotKeyBean>>>() {
//                })
//                .adapt(new ObservableBody<ResponseData<List<HotKeyBean>>>());
//    }
//
//    /**
//     * 搜索
//     * http://www.wanandroid.com/article/query/0/json
//     * @param page page
//     * @param k    POST search key
//     * @POST("/article/query/{page}/json")
//     */
//    public static Observable<ResponseData<ArticleListVO>> searchArticle(int page,String key) {
//        String url = AppConst.BASE_URL + "article/query/" + page + "/json";
//        return OkGo.<ResponseData<ArticleListVO>>post(url)
//                .params("k",key)
//                .converter(new JsonConvert<ResponseData<ArticleListVO>>() {
//                })
//                .adapt(new ObservableBody<ResponseData<ArticleListVO>>());
//    }


}
