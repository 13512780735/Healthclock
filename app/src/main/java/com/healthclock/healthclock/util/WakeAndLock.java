package com.healthclock.healthclock.util;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

import com.healthclock.healthclock.app.App;

public class WakeAndLock {
    Context context;
    PowerManager pm;
    PowerManager.WakeLock wakeLock;

    public WakeAndLock() {
        context = App.getContext();
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "WakeAndLock");
    }

    /**
     * 唤醒屏幕
     */
    public void screenOn() {
        wakeLock.acquire();
        android.util.Log.i("cxq", "screenOn");
    }

    /**
     * 熄灭屏幕
     */
//    public void screenOff() {
//        pm.goToSleep(SystemClock.uptimeMillis());
//        android.util.Log.i("cxq", "screenOff");
//    }
}
