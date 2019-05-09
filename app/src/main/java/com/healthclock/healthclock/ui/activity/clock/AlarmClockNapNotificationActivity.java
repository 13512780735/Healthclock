package com.healthclock.healthclock.ui.activity.clock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.common.WeacConstants;
import com.healthclock.healthclock.network.model.main.AlarmClock;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.MyUtil;

public class AlarmClockNapNotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock_nap_notification);
        AlarmClock alarmClock = getIntent().getParcelableExtra(
                WeacConstants.ALARM_CLOCK);
        // 关闭小睡
        MyUtil.cancelAlarmClock(this, -alarmClock.getId());
        finish();
    }
}
