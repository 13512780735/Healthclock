package com.healthclock.healthclock.ui.activity.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.main.AlarmInfo;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.widget.popwindows.SelectRemindCyclePopup;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

public class EditAlarmActivity extends BaseActivity {
    int position;
    static int i;
    boolean isDelete = false;
    String display_time;
    AlarmInfo alarmInfo;

    int hour, minute;
    private PendingIntent pendingIntent;
    private AlarmManager mAlarmManager;

    private CheckBox mCheckBox;
    private TimePicker mTimePicker;
    private Button mBtnSave, mBtnCancel;
    private LinearLayout mRingTone,allLayout,mRepeat;
    private ImageButton mImageButton;
    private EditText mEditText;
    private TextView tv_repeat_value;
    private int cycle;
    private int ring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
    }
    //初始化 alarmInfo
    private void initAlarmInfo() {

        //判断是 添加闹钟 还是修改 闹钟
        boolean isAdd = getIntent().getBooleanExtra("isAdd",false);

        if(isAdd) {
            //添加
            alarmInfo = new AlarmInfo();
            position = i++;
        } else {
            //修改
            alarmInfo = (AlarmInfo) getIntent().getSerializableExtra("updateAlarmInfo");
            display_time = alarmInfo.getAlarm_time();
            mEditText.setText(alarmInfo.getNote());
            tv_repeat_value.setText(alarmInfo.getRepeat());
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
        mRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRemindCycle();
            }
        });
        //铃声选择
        mRingTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent = new Intent(EditAlarmActivity.this, RingToneActivity.class);
               // startActivity(intent);
            }
        });
        //振动
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
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
            } if(cycle == -1){//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, hour, minute, position, 0, "闹钟响了", ring);
            }else {//多选，周几的闹钟
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

    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("每天");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
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
        String note = mEditText.getText().toString();
        String repeat = tv_repeat_value.getText().toString();

        alarmInfo.setAlarm_time(display_time);
        alarmInfo.setRepeat(repeat);
        alarmInfo.setNote(note);
        alarmInfo.setCheckBox(true);
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("alarmInfo",alarmInfo);
        intent.putExtra("isDelete", isDelete);
        intent.putExtra("position", position);
        //设置返回数据
        EditAlarmActivity.this.setResult(RESULT_OK, intent);
    }

    //保存数据到本地
    private void saveData() {
    }
}
