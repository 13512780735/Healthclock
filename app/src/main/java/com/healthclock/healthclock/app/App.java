package com.healthclock.healthclock.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import java.util.LinkedList;
import java.util.List;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * user：lqm
 * desc：
 */

public class App extends Application {

    public static List<Activity> activities = new LinkedList<>();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();


    }

    /**
     * 配置AutoLayout
     */
    private void initAutoLayout() {
        //默认使用的高度是设备的可用高度，也就是不包括状态栏和底部的操作栏的，以下配置可以拿到设备的物理高度进行百分比
//        AutoLayoutConifg.getInstance().useDeviceSize();
    }


    public static Context getmContext() {
        return mContext;
    }

    /**
     * 退出程序
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

}