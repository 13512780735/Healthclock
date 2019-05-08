package com.healthclock.healthclock.ui.activity.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.main.AlarmInfo;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.MyUtil;
import com.healthclock.healthclock.widget.BorderTextView;
import com.healthclock.healthclock.widget.IconFontTextView;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

import java.util.Collection;
import java.util.TreeMap;

public class EditAlarmActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    int position;
    static int i;
    boolean isDelete = false;
    String display_time;
    AlarmInfo alarmInfo;

    int hour, minute;
    private PendingIntent pendingIntent;
    private AlarmManager mAlarmManager;

    private TimePicker mTimePicker;
    private IconFontTextView mBtnSave, mBtnCancel;
    private LinearLayout allLayout, mRepeat;
    private BorderTextView mImageButton;
    private int cycle;
    private int ring;
    private ViewGroup mRingTone;

    /**
     * 保存重复描述信息String
     */
    private StringBuilder mRepeatStr;
    /**
     * 周一按钮状态，默认未选中
     */
    private Boolean isMondayChecked = false;

    /**
     * 周二按钮状态，默认未选中
     */
    private Boolean isTuesdayChecked = false;

    /**
     * 周三按钮状态，默认未选中
     */
    private Boolean isWednesdayChecked = false;

    /**
     * 周四按钮状态，默认未选中
     */
    private Boolean isThursdayChecked = false;

    /**
     * 周五按钮状态，默认未选中
     */
    private Boolean isFridayChecked = false;

    /**
     * 周六按钮状态，默认未选中
     */
    private Boolean isSaturdayChecked = false;

    /**
     * 周日按钮状态，默认未选中
     */
    private Boolean isSundayChecked = false;
    /**
     * 按键值顺序存放重复描述信息
     */
    private TreeMap<Integer, String> mMap;
    /**
     * 重复描述组件
     */
    private TextView mRepeatDescribe;
    private Switch vibrateBtn;
    EditText tag;
    private TextView mTimePickerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm2);

        initView();
        initAlarmInfo();
        clickListener();
    }

    //初始化 View
    private void initView() {
        mBtnSave = findViewById(R.id.btn_save);
        mBtnCancel = findViewById(R.id.btn_cancel);
        allLayout = findViewById(R.id.allLayout);
        mTimePickerTv = findViewById(R.id.tv_time);

        mRepeat = findViewById(R.id.linear_repeat);
        mTimePicker = findViewById(R.id.time_picker);
        mImageButton = findViewById(R.id.ibtn_delete);
        mTimePicker.setIs24HourView(true);
        mRepeatDescribe = findViewById(R.id.repeat_describe);
        mRingTone = (ViewGroup) findViewById(R.id.ring_llyt);
        vibrateBtn = (Switch) findViewById(R.id.vibrate_btn);
        tag = findViewById(R.id.tag_edit_text);
        // 周选择按钮
        // 周一按钮
        ToggleButton monday = findViewById(R.id.tog_btn_monday);
        // 周二按钮
        ToggleButton tuesday = findViewById(R.id.tog_btn_tuesday);
        // 周三按钮
        ToggleButton wednesday = findViewById(R.id.tog_btn_wednesday);
        // 周四按钮
        ToggleButton thursday = findViewById(R.id.tog_btn_thursday);
        // 周五按钮
        ToggleButton friday = findViewById(R.id.tog_btn_friday);
        // 周六按钮
        ToggleButton saturday = findViewById(R.id.tog_btn_saturday);
        // 周日按钮
        ToggleButton sunday = findViewById(R.id.tog_btn_sunday);

        monday.setOnCheckedChangeListener(this);
        tuesday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);
    }

    //初始化 alarmInfo
    private void initAlarmInfo() {

        //判断是 添加闹钟 还是修改 闹钟
        boolean isAdd = getIntent().getBooleanExtra("isAdd", false);

        if (isAdd) {
            //添加
            alarmInfo = new AlarmInfo();
            position = i++;
        } else {
            //修改
            alarmInfo = (AlarmInfo) getIntent().getSerializableExtra("updateAlarmInfo");
            display_time = alarmInfo.getAlarm_time();
            tag.setText(alarmInfo.getNote());
            mRepeatDescribe.setText(alarmInfo.getRepeat());
            position = getIntent().getIntExtra("position", 0);
        }

        //初始化闹钟
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent("com.wei.BC_ACTION");
        pendingIntent = PendingIntent.getBroadcast(this, position, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    //点击事件
    private void clickListener() {
        //取消按钮
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAlarmActivity.this.finish();
            }
        });
        //保存按钮
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                setAlarm();
                returnInfo();
                EditAlarmActivity.this.finish();
            }
        });
        //重复选择
//        mRepeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectRemindCycle();
//            }
//        });
        //铃声选择
        mRingTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EditAlarmActivity.this, RingToneActivity.class);
//                startActivity(intent);
            }
        });
        //振动
        vibrateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ring = 0;
                } else {
                    ring = 1;
                }
            }
        });
        //删除按钮
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
                isDelete = true;
                returnInfo();
                finish();
            }
        });
    }

    //设置闹钟时间
    private void setAlarm() {

        if (Build.VERSION.SDK_INT >= 23) {
            hour = mTimePicker.getHour();
            minute = mTimePicker.getMinute();
        } else {
            hour = mTimePicker.getCurrentHour();
            minute = mTimePicker.getCurrentMinute();
        }

/*        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);*/

        //long ring_time = calendar.getTimeInMillis();

        display_time = String.valueOf(hour) + " : " + String.valueOf(minute);

/*        mAlarmManager.set(AlarmManager.RTC_WAKEUP, ring_time, pendingIntent);
        Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_SHORT).show();*/

        if (display_time.length() > 0) {
            if (cycle == 0) {//是每天的闹钟
                AlarmManagerUtil.setAlarm(this, 0, hour, minute, position, 0, "闹钟响了", ring);
            }
            if (cycle == -1) {//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, hour, minute, position, 0, "闹钟响了", ring);
            } else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, hour, minute, i, Integer.parseInt(weeks[i]), "闹钟响了", ring);
                }
            }
            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show();
        }
    }

    //取消闹钟
    public void cancelAlarm() {
        mAlarmManager.cancel(pendingIntent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            // 选中周一
            case R.id.tog_btn_monday:
                int repeat = Integer.valueOf(1);
                mRepeatDescribe.setText(parseRepeat(repeat, 0));
                cycle = repeat;
                break;
            // 选中周二
            case R.id.tog_btn_tuesday:
                break;
            // 选中周三
            case R.id.tog_btn_wednesday:
                break;
            // 选中周四
            case R.id.tog_btn_thursday:
                break;
            // 选中周五
            case R.id.tog_btn_friday:
                break;
            // 选中周六
            case R.id.tog_btn_saturday:
                break;
            // 选中周日
            case R.id.tog_btn_sunday:
                break;
        }

    }
    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }



    //通过 intent 返回数据
    public void returnInfo() {

        int position = getIntent().getIntExtra("position", 0);
        String note = tag.getText().toString();
        String repeat = mRepeatDescribe.getText().toString();

        alarmInfo.setAlarm_time(display_time);
        alarmInfo.setRepeat(repeat);
        alarmInfo.setNote(note);
        alarmInfo.setCheckBox(true);
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("alarmInfo", alarmInfo);
        intent.putExtra("isDelete", isDelete);
        intent.putExtra("position", position);
        //设置返回数据
        EditAlarmActivity.this.setResult(RESULT_OK, intent);
    }


    //保存数据到本地
    private void saveData() {
    }
}
