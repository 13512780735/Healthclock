package com.healthclock.healthclock.ui.activity.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.main.AlarmClock;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.MyUtil;

import java.util.TreeMap;

public class EditAlarmActivity extends BaseActivity {

    private TextView mTimePickerTv;
    /**
     * 闹钟实例
     */
    private AlarmClock mAlarmClock;

    //1233
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        // 初始化时间选择
        initTimeSelect();
        // 初始化重复
        initRepeat();
        // 初始化标签
        initTag();
        // 初始化铃声
        initRing();
        // 初始化振动、小睡、天气提示
        initToggleButton();
    }

    /**
     * 设置时间选择
     *
     * @param
     */
    private void initTimeSelect() {
        // 下次响铃提示
        mTimePickerTv = (TextView) findViewById(R.id.tv_time);
        // 计算倒计时显示
        displayCountDown();
        // 闹钟时间选择器
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        // 初始化时间选择器的小时
        //noinspection deprecation
        timePicker.setCurrentHour(mAlarmClock.getHour());
        // 初始化时间选择器的分钟
        //noinspection deprecation
        timePicker.setCurrentMinute(mAlarmClock.getMinute());

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // 保存闹钟实例的小时
                mAlarmClock.setHour(hourOfDay);
                // 保存闹钟实例的分钟
                mAlarmClock.setMinute(minute);
                // 计算倒计时显示
                displayCountDown();
            }

        });
    }
    /**
     * 设置重复信息
     *
     * @param view view
     */
    private void initRepeat(View view) {
        // 重复描述
        mRepeatDescribe = (TextView) view.findViewById(R.id.repeat_describe);

        // 周选择按钮
        // 周一按钮
        ToggleButton monday = (ToggleButton) view.findViewById(R.id.tog_btn_monday);
        // 周二按钮
        ToggleButton tuesday = (ToggleButton) view.findViewById(R.id.tog_btn_tuesday);
        // 周三按钮
        ToggleButton wednesday = (ToggleButton) view.findViewById(R.id.tog_btn_wednesday);
        // 周四按钮
        ToggleButton thursday = (ToggleButton) view.findViewById(R.id.tog_btn_thursday);
        // 周五按钮
        ToggleButton friday = (ToggleButton) view.findViewById(R.id.tog_btn_friday);
        // 周六按钮
        ToggleButton saturday = (ToggleButton) view.findViewById(R.id.tog_btn_saturday);
        // 周日按钮
        ToggleButton sunday = (ToggleButton) view.findViewById(R.id.tog_btn_sunday);

        monday.setOnCheckedChangeListener(this);
        tuesday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);

        mRepeatStr = new StringBuilder();
        mMap = new TreeMap<>();

        String weeks = mAlarmClock.getWeeks();
        // 不是单次响铃时
        if (weeks != null) {
            final String[] weeksValue = weeks.split(",");
            for (String aWeeksValue : weeksValue) {
                int week = Integer.parseInt(aWeeksValue);
                switch (week) {
                    case 1:
                        sunday.setChecked(true);
                        break;
                    case 2:
                        monday.setChecked(true);
                        break;
                    case 3:
                        tuesday.setChecked(true);
                        break;
                    case 4:
                        wednesday.setChecked(true);
                        break;
                    case 5:
                        thursday.setChecked(true);
                        break;
                    case 6:
                        friday.setChecked(true);
                        break;
                    case 7:
                        saturday.setChecked(true);
                        break;
                }

            }
        }
    }
    /**
     * 计算显示倒计时信息
     */
    private void displayCountDown() {
        // 取得下次响铃时间
        long nextTime = MyUtil.calculateNextTime(mAlarmClock.getHour(),
                mAlarmClock.getMinute(), mAlarmClock.getWeeks());
        // 系统时间
        long now = System.currentTimeMillis();
        // 距离下次响铃间隔毫秒数
        long ms = nextTime - now;

        // 单位秒
        int ss = 1000;
        // 单位分
        int mm = ss * 60;
        // 单位小时
        int hh = mm * 60;
        // 单位天
        int dd = hh * 24;

        // 不计算秒，故响铃间隔加一分钟
        ms += mm;
        // 剩余天数
        long remainDay = ms / dd;
        // 剩余小时
        long remainHour = (ms - remainDay * dd) / hh;
        // 剩余分钟
        long remainMinute = (ms - remainDay * dd - remainHour * hh) / mm;

        // 响铃倒计时
        String countDown;
        // 当剩余天数大于0时显示【X天X小时X分】格式
        if (remainDay > 0) {
            countDown = getString(R.string.countdown_day_hour_minute);
            mTimePickerTv.setText(String.format(countDown, remainDay,
                    remainHour, remainMinute));
            // 当剩余小时大于0时显示【X小时X分】格式
        } else if (remainHour > 0) {
            countDown = getResources()
                    .getString(R.string.countdown_hour_minute);
            mTimePickerTv.setText(String.format(countDown, remainHour,
                    remainMinute));
        } else {
            // 当剩余分钟不等于0时显示【X分钟】格式
            if (remainMinute != 0) {
                countDown = getString(R.string.countdown_minute);
                mTimePickerTv.setText(String.format(countDown, remainMinute));
                // 当剩余分钟等于0时，显示【1天0小时0分】
            } else {
                countDown = getString(R.string.countdown_day_hour_minute);
                mTimePickerTv.setText(String.format(countDown, 1, 0, 0));
            }

        }
    }
}
