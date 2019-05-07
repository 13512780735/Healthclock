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
package com.healthclock.healthclock.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.healthclock.healthclock.common.WeacConstants;
import com.healthclock.healthclock.common.WeacStatus;
import com.healthclock.healthclock.db.AlarmClockOperate;
import com.healthclock.healthclock.network.model.main.AlarmClock;
import com.healthclock.healthclock.ui.activity.main.AlarmClockOntimeActivity;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.MyUtil;


/**
 * 闹钟响起广播
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class AlarmClockBroadcast extends BroadcastReceiver {

    /**
     * Log tag ：AlarmClockBroadcast
     */
    private static final String LOG_TAG = "AlarmClockBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmClock alarmClock = intent
                .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        if (alarmClock != null) {
            // 单次响铃
            if (alarmClock.getWeeks() == null) {
                AlarmClockOperate.getInstance().updateAlarmClock(false,
                        alarmClock.getId());

                Intent i = new Intent("com.kaku.weac.AlarmClockOff");
                context.sendBroadcast(i);
            } else {
                // 重复周期闹钟
                MyUtil.startAlarmClock(context, alarmClock);
            }
        }

        // 小睡已执行次数
        int napTimesRan = intent.getIntExtra(WeacConstants.NAP_RAN_TIMES, 0);
        // 当前时间
        long now = SystemClock.elapsedRealtime();
        // 当上一次闹钟响起时间等于0
        if (WeacStatus.sLastStartTime == 0) {
            // 上一次闹钟响起时间等于当前时间
            WeacStatus.sLastStartTime = now;
            // 当上一次响起任务距离现在小于3秒时
        } else if ((now - WeacStatus.sLastStartTime) <= 3000) {

            L.d(LOG_TAG, "进入3秒以内再次响铃 小睡次数：" + napTimesRan + "距离时间毫秒数："
                    + (now - WeacStatus.sLastStartTime));
            L.d(LOG_TAG, "WeacStatus.strikerLevel："
                    + WeacStatus.sStrikerLevel);
//            LogUtil.d(LOG_TAG, "闹钟名：" + alarmClock.getTag());

            // 当是新闹钟任务并且上一次响起也为新闹钟任务时，开启了时间相同的多次闹钟，只保留一个其他关闭
            if ((napTimesRan == 0) & (WeacStatus.sStrikerLevel == 1)) {
                return;
            }
        } else {
            // 上一次闹钟响起时间等于当前时间
            WeacStatus.sLastStartTime = now;
        }

        Intent it = new Intent(context, AlarmClockOntimeActivity.class);

        // 新闹钟任务
        if (napTimesRan == 0) {
            // 设置响起级别为闹钟
            WeacStatus.sStrikerLevel = 1;
            // 小睡任务
        } else {
            // 设置响起级别为小睡
            WeacStatus.sStrikerLevel = 2;
            // 小睡已执行次数
            it.putExtra(WeacConstants.NAP_RAN_TIMES, napTimesRan);
        }
        it.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);
        // 清除栈顶的Activity
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(it);
    }
}
