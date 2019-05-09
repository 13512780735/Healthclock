package com.healthclock.healthclock.ui.fragment.clock;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.common.WeacConstants;
import com.healthclock.healthclock.network.model.main.AlarmClock;
import com.healthclock.healthclock.ui.activity.clock.RingSelectActivity;
import com.healthclock.healthclock.ui.base.BaseFragment;
import com.healthclock.healthclock.util.MyUtil;

import java.util.Collection;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmClockEditFragment extends BaseFragment implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    /**
     * Log tag ：AlarmClockEditFragment
     */
    private static final String LOG_TAG = "AlarmClockEditFragment";

    /**
     * 铃声选择按钮的requestCode
     */
    private static final int REQUEST_RING_SELECT = 1;

    /**
     * 小睡按钮的requestCode
     */
    private static final int REQUEST_NAP_EDIT = 2;

    /**
     * 闹钟实例
     */
    private AlarmClock mAlarmClock;

    /**
     * 下次响铃时间提示控件
     */
    private TextView mTimePickerTv;

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
     * 保存重复描述信息String
     */
    private StringBuilder mRepeatStr;

    /**
     * 重复描述组件
     */
    private TextView mRepeatDescribe;

    /**
     * 按键值顺序存放重复描述信息
     */
    private TreeMap<Integer, String> mMap;

    /**
     * 铃声描述
     */
    private TextView mRingDescribe;


    @Override
    protected int setContentView() {
        return R.layout.activity_edit_alarm;
    }

    @Override
    protected void lazyLoad() {
        // 初始化时间选择
        initTimeSelect();
        // 初始化重复
        initRepeat();
        // 初始化标签
        initTag();
        // 初始化铃声
        initRing();
        // 初始化音量
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
        mTimePickerTv = findViewById(R.id.tv_time);
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
     * @param
     */
    private void initRepeat() {
        // 重复描述
        mRepeatDescribe = findViewById(R.id.repeat_describe);

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
     * 设置标签
     *
     * @param
     */
    private void initTag() {
        // 初始化闹钟实例的标签
        mAlarmClock.setTag(getString(R.string.alarm_clock));

        // 标签描述控件
        EditText tag = findViewById(R.id.tag_edit_text);
        tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("")) {
                    mAlarmClock.setTag(s.toString());
                } else {
                    mAlarmClock.setTag(getString(R.string.alarm_clock));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

        });
    }

    /**
     * 设置铃声
     */
    private void initRing() {
        // 取得铃声选择配置信息
        ViewGroup ring = (ViewGroup)findViewById(R.id.ring_llyt);
        ring.setOnClickListener(this);
        mRingDescribe = (TextView) findViewById(R.id.ring_describe);
        mRingDescribe.setText(mAlarmClock.getRingName());
    }

    /**
     * 设置振动
     */
    private void initToggleButton() {
        // 初始化闹钟实例的振动，默认振动
//        mAlarmClock.setVibrate(true);
//
//        // 初始化闹钟实例的小睡信息
//        // 默认小睡
//        mAlarmClock.setNap(true);
//        // 小睡间隔10分钟
//        mAlarmClock.setNapInterval(10);
//        // 小睡3次
//        mAlarmClock.setNapTimes(3);
//
//        // 初始化闹钟实例的天气提示，默认开启
//        mAlarmClock.setWeaPrompt(true);

        // 振动
        Switch vibrateBtn = (Switch) findViewById(R.id.vibrate_btn);

        vibrateBtn.setOnCheckedChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 当点击取消按钮
            case R.id.btn_cancel:
                drawAnimation();
                break;
            // 当点击确认按钮
            case R.id.btn_save:
                saveDefaultAlarmTime();

                Intent data = new Intent();
                data.putExtra(WeacConstants.ALARM_CLOCK, mAlarmClock);
                getActivity().setResult(Activity.RESULT_OK, data);
                drawAnimation();
                break;
            // 当点击铃声
            case R.id.ring_llyt:
                // 不响应重复点击
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                // 铃声选择界面
                Intent i = new Intent(getActivity(), RingSelectActivity.class);
                i.putExtra(WeacConstants.RING_REQUEST_TYPE, 0);
                startActivityForResult(i, REQUEST_RING_SELECT);
                break;
        }
    }

    /**
     * 结束新建闹钟界面时开启渐变缩小效果动画
     */
    private void drawAnimation() {
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.zoomout);
    }

    private void saveDefaultAlarmTime() {
        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(WeacConstants.DEFAULT_ALARM_HOUR, mAlarmClock.getHour());
        editor.putInt(WeacConstants.DEFAULT_ALARM_MINUTE, mAlarmClock.getMinute());
        editor.apply();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            // 选中周一
            case R.id.tog_btn_monday:
                if (isChecked) {
                    isMondayChecked = true;
                    mMap.put(1, getString(R.string.one_h));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isMondayChecked = false;
                    mMap.remove(1);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 选中周二
            case R.id.tog_btn_tuesday:
                if (isChecked) {
                    isTuesdayChecked = true;
                    mMap.put(2, getString(R.string.two_h));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isTuesdayChecked = false;
                    mMap.remove(2);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 选中周三
            case R.id.tog_btn_wednesday:
                if (isChecked) {
                    isWednesdayChecked = true;
                    mMap.put(3, getString(R.string.three_h));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isWednesdayChecked = false;
                    mMap.remove(3);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 选中周四
            case R.id.tog_btn_thursday:
                if (isChecked) {
                    isThursdayChecked = true;
                    mMap.put(4, getString(R.string.four_h));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isThursdayChecked = false;
                    mMap.remove(4);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 选中周五
            case R.id.tog_btn_friday:
                if (isChecked) {
                    isFridayChecked = true;
                    mMap.put(5, getString(R.string.five_h));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isFridayChecked = false;
                    mMap.remove(5);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 选中周六
            case R.id.tog_btn_saturday:
                if (isChecked) {
                    isSaturdayChecked = true;
                    mMap.put(6, getString(R.string.six_h));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isSaturdayChecked = false;
                    mMap.remove(6);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 选中周日
            case R.id.tog_btn_sunday:
                if (isChecked) {
                    isSundayChecked = true;
                    mMap.put(7, getString(R.string.day));
                    setRepeatDescribe();
                    displayCountDown();
                } else {
                    isSundayChecked = false;
                    mMap.remove(7);
                    setRepeatDescribe();
                    displayCountDown();
                }
                break;
            // 振动
            case R.id.vibrate_btn:
                if (isChecked) {
                    MyUtil.vibrate(getActivity());
                    mAlarmClock.setVibrate(true);
                } else {
                    mAlarmClock.setVibrate(false);
                }
                break;
            // 小睡
        }

    }

    /**
     * 设置重复描述的内容
     */
    private void setRepeatDescribe() {
        // 全部选中
        if (isMondayChecked & isTuesdayChecked & isWednesdayChecked
                & isThursdayChecked & isFridayChecked & isSaturdayChecked
                & isSundayChecked) {
            mRepeatDescribe.setText(getResources()
                    .getString(R.string.every_day));
            mAlarmClock.setRepeat(getString(R.string.every_day));
            // 响铃周期
            mAlarmClock.setWeeks("2,3,4,5,6,7,1");
            // 周一到周五全部选中
        } else if (isMondayChecked & isTuesdayChecked & isWednesdayChecked
                & isThursdayChecked & isFridayChecked & !isSaturdayChecked
                & !isSundayChecked) {
            mRepeatDescribe.setText(getString(R.string.week_day));
            mAlarmClock.setRepeat(getString(R.string.week_day));
            mAlarmClock.setWeeks("2,3,4,5,6");
            // 周六、日全部选中
        } else if (!isMondayChecked & !isTuesdayChecked & !isWednesdayChecked
                & !isThursdayChecked & !isFridayChecked & isSaturdayChecked
                & isSundayChecked) {
            mRepeatDescribe.setText(getString(R.string.week_end));
            mAlarmClock.setRepeat(getString(R.string.week_end));
            mAlarmClock.setWeeks("7,1");
            // 没有选中任何一个
        } else if (!isMondayChecked & !isTuesdayChecked & !isWednesdayChecked
                & !isThursdayChecked & !isFridayChecked & !isSaturdayChecked
                & !isSundayChecked) {
            mRepeatDescribe.setText(getString(R.string.repeat_once));
            mAlarmClock.setRepeat(getResources()
                    .getString(R.string.repeat_once));
            mAlarmClock.setWeeks(null);

        } else {
            mRepeatStr.setLength(0);
            mRepeatStr.append(getString(R.string.week));
            Collection<String> col = mMap.values();
            for (String aCol : col) {
                mRepeatStr.append(aCol).append(getResources().getString(R.string.caesura));
            }
            // 去掉最后一个"、"
            mRepeatStr.setLength(mRepeatStr.length() - 1);
            mRepeatDescribe.setText(mRepeatStr.toString());
            mAlarmClock.setRepeat(mRepeatStr.toString());

            mRepeatStr.setLength(0);
            if (isMondayChecked) {
                mRepeatStr.append("2,");
            }
            if (isTuesdayChecked) {
                mRepeatStr.append("3,");
            }
            if (isWednesdayChecked) {
                mRepeatStr.append("4,");
            }
            if (isThursdayChecked) {
                mRepeatStr.append("5,");
            }
            if (isFridayChecked) {
                mRepeatStr.append("6,");
            }
            if (isSaturdayChecked) {
                mRepeatStr.append("7,");
            }
            if (isSundayChecked) {
                mRepeatStr.append("1,");
            }
            mAlarmClock.setWeeks(mRepeatStr.toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 铃声选择界面返回
            case REQUEST_RING_SELECT:
                // 铃声名
                String name = data.getStringExtra(WeacConstants.RING_NAME);
                // 铃声地址
                String url = data.getStringExtra(WeacConstants.RING_URL);
                // 铃声界面
                int ringPager = data.getIntExtra(WeacConstants.RING_PAGER, 0);

                mRingDescribe.setText(name);

                mAlarmClock.setRingName(name);
                mAlarmClock.setRingUrl(url);
                mAlarmClock.setRingPager(ringPager);
                break;
        }
    }
}
