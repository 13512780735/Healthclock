/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.healthclock.healthclock.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import com.coolerfall.daemon.Daemon;
import com.healthclock.healthclock.broadcast.WakeReceiver;
import com.healthclock.healthclock.common.WeacConstants;
import com.healthclock.healthclock.db.AlarmClockOperate;
import com.healthclock.healthclock.network.model.main.AlarmClock;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.MyUtil;

import java.util.List;

public class DaemonService extends Service {

    /**
     * Log tag ：DaemonService
     */
    private static final String LOG_TAG = "DaemonService";

    /**
     * 定时唤醒的时间间隔，5分钟
     */
    private final static int ALARM_INTERVAL = 5 * 60 * 1000;
    private final static int WAKE_REQUEST_CODE = 6666;

    private final static int GRAY_SERVICE_ID = -1001;

    @Override
    public void onCreate() {
        super.onCreate();
        L.d(LOG_TAG, "onCreate");
        Daemon.run(DaemonService.this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE);
        startTimeTask();
        grayGuard();

        // Notification notification = new Notification();
        // startForeground(-1, notification);
    }

    private void startTimeTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
                for (AlarmClock alarmClock : list) {
                    // 当闹钟为开时刷新开启闹钟
                    if (alarmClock.isOnOff()) {
                        MyUtil.startAlarmClock(DaemonService.this, alarmClock);
                    }
                }

                SharedPreferences preferences = getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                // 倒计时时间
                long countdown = preferences.getLong(WeacConstants.COUNTDOWN_TIME, 0);
                boolean isStop = preferences.getBoolean(WeacConstants.IS_STOP, false);
                if (countdown != 0 && !isStop) {
                    long now = SystemClock.elapsedRealtime();
                    long remainTime = countdown - now;
                    if (remainTime > 0 && (remainTime / 1000 / 60) < 60) {
                       // MyUtil.startAlarmTimer(DaemonService.this, remainTime);
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(LOG_TAG, "onDestroy");
        startService(new Intent(this, DaemonService.class));
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d(LOG_TAG, "onStartCommand");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    private void grayGuard() {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, DaemonInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, operation);
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class DaemonInnerService extends Service {

        @Override
        public void onCreate() {
            L.i(LOG_TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            L.i(LOG_TAG, "InnerService -> onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            L.i(LOG_TAG, "InnerService -> onDestroy");
            super.onDestroy();
        }
    }
}
