package com.healthclock.healthclock.util;

import android.content.Context;

import com.yanzhenjie.permission.AndPermission;

/**
 * 权限请求工具
 */
public class PermissionUtil {
    /**
     * 请求运行时权限
     * @param context
     * @param permissions 要请求的权限或者权限组
     */
    public static void requestPermission(Context context,String... permissions){
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .start();
    }
}
