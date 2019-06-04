package com.healthclock.healthclock.clock.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.clock.activity.RingSelectActivity;
import com.healthclock.healthclock.clock.common.WeacConstants;
import com.healthclock.healthclock.clock.model.AlarmClock;
import com.healthclock.healthclock.clock.util.MyUtil;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.widget.BorderTextView;
import com.healthclock.healthclock.widget.IconFontTextView;

import java.util.Calendar;
import java.util.Collection;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * 间隔闹
 */
public class AlarmClockNew01Fragment extends BaseFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    /**
     * 铃声选择按钮的requestCode
     */
    private static final int REQUEST_RING_SELECT = 1;
    private TextView mTimePickerTv;
    /**
     * 闹钟实例
     */
    private AlarmClock mAlarmClock;

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
    private IconFontTextView tvBack, tvSave;
    private BorderTextView ibtn_delete;

    //    @Override
//    protected int setContentView() {
//        return R.layout.activity_edit_alarm;
//    }
    CheckBox tog_btn_01, tog_btn_02, tog_btn_03, tog_btn_04;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmClock = new AlarmClock();
        // 闹钟默认开启
        mAlarmClock.setOnOff(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_clock_new01,
                container, false);
        ButterKnife.bind(view);
        initTimeSelect(view);
        // 初始化重复
        initRepeat(view);
        // 初始化标签
        initTag(view);
        // 初始化铃声
        initRing(view);
        // 初始化振动
        initToggleButton(view);
        return view;
    }

    EditText hour01, hour02, hour03, minute01, minute02, minute03;
    BorderTextView tv_getTime01, tv_getTime02;

    /**
     * 设置时间选择
     *
     * @param
     * @param view
     */
    private void initTimeSelect(View view) {
        // 下次响铃提示
        //mTimePickerTv = view.findViewById(R.id.tv_time);
        // 计算倒计时显示
        displayCountDown();
        // 闹钟时间选择器
        // TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        tvBack = view.findViewById(R.id.btn_cancel);
        tvSave = view.findViewById(R.id.btn_save);
        ibtn_delete = view.findViewById(R.id.ibtn_delete);
        hour01 = view.findViewById(R.id.hour01);
        hour02 = view.findViewById(R.id.hour02);
        hour03 = view.findViewById(R.id.hour03);
        minute01 = view.findViewById(R.id.minute01);
        minute02 = view.findViewById(R.id.minute02);
        minute03 = view.findViewById(R.id.minute03);
        tv_getTime01 = view.findViewById(R.id.tv_getTime01);
        tv_getTime02 = view.findViewById(R.id.tv_getTime02);

        ibtn_delete.setVisibility(View.GONE);
        tvBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        //timePicker.setIs24HourView(true);
        // 初始化时间选择器的小时
        //noinspection deprecation
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.HOUR_OF_DAY);
        //   timePicker.setCurrentHour(i);


        // timePicker.setCurrentHour(mAlarmClock.getHour());
        // 初始化时间选择器的分钟
        //noinspection deprecation
        /// timePicker.setCurrentMinute(mAlarmClock.getMinute());

//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                // 保存闹钟实例的小时
//                mAlarmClock.setHour(hourOfDay);
//                // 保存闹钟实例的分钟
//                mAlarmClock.setMinute(minute);
//                // 计算倒计时显示
//                displayCountDown();
//            }
//
//        });
        tv_getTime01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour01.setText(StringUtil.getHour());
                minute01.setText(StringUtil.getMinute());
            }
        });
        tv_getTime02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour03.setText(StringUtil.getHour());
                minute03.setText(StringUtil.getMinute());
            }
        });
    }

    /**
     * 设置重复信息
     *
     * @param
     * @param view
     */
    private void initRepeat(View view) {
        // 重复描述
        mRepeatDescribe = view.findViewById(R.id.repeat_describe);

        // 周选择按钮
        // 周一按钮
        ToggleButton monday = view.findViewById(R.id.tog_btn_monday);
        // 周二按钮
        ToggleButton tuesday = view.findViewById(R.id.tog_btn_tuesday);
        // 周三按钮
        ToggleButton wednesday = view.findViewById(R.id.tog_btn_wednesday);
        // 周四按钮
        ToggleButton thursday = view.findViewById(R.id.tog_btn_thursday);
        // 周五按钮
        ToggleButton friday = view.findViewById(R.id.tog_btn_friday);
        // 周六按钮
        ToggleButton saturday = view.findViewById(R.id.tog_btn_saturday);
        // 周日按钮

        ToggleButton sunday = view.findViewById(R.id.tog_btn_sunday);
        tog_btn_01 = view.findViewById(R.id.tog_btn_01);
        tog_btn_02 = view.findViewById(R.id.tog_btn_02);
        tog_btn_03 = view.findViewById(R.id.tog_btn_03);
        tog_btn_04 = view.findViewById(R.id.tog_btn_04);
        tog_btn_04 = view.findViewById(R.id.tog_btn_04);
        tog_btn_01.setOnClickListener(this);
        tog_btn_02.setOnClickListener(this);
        tog_btn_03.setOnClickListener(this);
        tog_btn_04.setOnClickListener(this);
        monday.setOnCheckedChangeListener(this);
        tuesday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);
        mRepeatStr = new StringBuilder();
        mMap = new TreeMap<>();
        // tog_btn_01.setChecked(true);
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
     * @param view
     */
    private void initTag(View view) {
        // 初始化闹钟实例的标签
        mAlarmClock.setTag(getString(R.string.alarm_clock));

        // 标签描述控件
        EditText tag = (EditText) view.findViewById(R.id.tag_edit_text);
        tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("")) {
                    mAlarmClock.setTag(s.toString());
                } else {
                    mAlarmClock.setTag(getString(R.string.alarm_clock));
                }

                L.e("TAG", mAlarmClock.getTag());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 设置铃声
     *
     * @param view
     */
    private void initRing(View view) {
        // 取得铃声选择配置信息
        SharedPreferences share = getActivity().getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        String ringName = share.getString(WeacConstants.RING_NAME,
                getString(R.string.default_ring));
        String ringUrl = share.getString(WeacConstants.RING_URL,
                WeacConstants.DEFAULT_RING_URL);

        // 初始化闹钟实例的铃声名
        mAlarmClock.setRingName(ringName);
        // 初始化闹钟实例的铃声播放地址
        mAlarmClock.setRingUrl(ringUrl);
        // 铃声控件
        ViewGroup ring = (ViewGroup) view.findViewById(R.id.ring_llyt);
        mRingDescribe = (TextView) view.findViewById(R.id.ring_describe);
        mRingDescribe.setText(ringName);
        L.e("RingName", mAlarmClock.getRingName());
        ring.setOnClickListener(this);
    }

    /**
     * 设置振动
     *
     * @param view
     */
    private void initToggleButton(View view) {
        // 初始化闹钟实例的振动，默认振动
        mAlarmClock.setVibrate(true);

        // 初始化闹钟实例的小睡信息
        // 默认小睡
        mAlarmClock.setNap(true);
        // 小睡间隔10分钟
        mAlarmClock.setNapInterval(10);
        // 小睡3次
        mAlarmClock.setNapTimes(3);

        // 初始化闹钟实例的天气提示，默认开启
        mAlarmClock.setWeaPrompt(true);

        // 振动
        Switch vibrateBtn = (Switch) view.findViewById(R.id.vibrate_btn);

        vibrateBtn.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.ring_llyt})
    public void onClick(View v) {
        switch (v.getId()) {
            // 当点击取消按钮
            case R.id.btn_cancel:
                L.e("点击了");
                drawAnimation();
                break;
            // 当点击确认按钮
            case R.id.btn_save:
                L.e("点击了2");
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
            case R.id.tog_btn_01:
                tog_btn_01.setChecked(true);
                tog_btn_02.setChecked(false);
                tog_btn_03.setChecked(false);
                tog_btn_04.setChecked(false);
                mAlarmClock.setHint("真视明操");
                break;
            case R.id.tog_btn_02:
                tog_btn_01.setChecked(false);
                tog_btn_02.setChecked(true);
                tog_btn_03.setChecked(false);
                tog_btn_04.setChecked(false);
                mAlarmClock.setHint("喝杯水");
                break;
            case R.id.tog_btn_03:
                tog_btn_01.setChecked(false);
                tog_btn_02.setChecked(false);
                tog_btn_03.setChecked(true);
                tog_btn_04.setChecked(false);
                mAlarmClock.setHint("喝咖啡");
                break;
            case R.id.tog_btn_04:
                tog_btn_01.setChecked(false);
                tog_btn_02.setChecked(false);
                tog_btn_03.setChecked(false);
                tog_btn_04.setChecked(true);
                mAlarmClock.setHint("其他事务");
                break;

        }
    }

    /**
     * 结束新建闹钟界面时开启渐变缩小效果动画
     */
    private void drawAnimation() {
        L.e("点击了3");
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

//            case R.id.tog_btn_01:
//                tog_btn_01.setChecked(true);
//                tog_btn_02.setChecked(false);
//                tog_btn_03.setChecked(false);
//                tog_btn_04.setChecked(false);
//                break;
//            case R.id.tog_btn_02:
//                tog_btn_01.setChecked(false);
//                tog_btn_02.setChecked(true);
//                tog_btn_03.setChecked(false);
//                tog_btn_04.setChecked(false);
//                break;
//            case R.id.tog_btn_03:
//                tog_btn_01.setChecked(false);
//                tog_btn_02.setChecked(false);
//                tog_btn_03.setChecked(true);
//                tog_btn_04.setChecked(false);
//                break;
//            case R.id.tog_btn_04:
//                tog_btn_01.setChecked(false);
//                tog_btn_02.setChecked(false);
//                tog_btn_03.setChecked(false);
//                tog_btn_04.setChecked(true);
//                break;
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
//            mTimePickerTv.setText(String.format(countDown, remainDay,
//                    remainHour, remainMinute));
            // 当剩余小时大于0时显示【X小时X分】格式
        } else if (remainHour > 0) {
            countDown = getResources()
                    .getString(R.string.countdown_hour_minute);
//            mTimePickerTv.setText(String.format(countDown, remainHour,
//                    remainMinute));
        } else {
            // 当剩余分钟不等于0时显示【X分钟】格式
            if (remainMinute != 0) {
                countDown = getString(R.string.countdown_minute);
                // mTimePickerTv.setText(String.format(countDown, remainMinute));
                // 当剩余分钟等于0时，显示【1天0小时0分】
            } else {
                countDown = getString(R.string.countdown_day_hour_minute);
                // mTimePickerTv.setText(String.format(countDown, 1, 0, 0));
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
                L.e("name:->" + name);
                L.e("url->" + url);
                L.e("ringPager:->" + ringPager);
                mRingDescribe.setText(name);

                mAlarmClock.setRingName(name);
                mAlarmClock.setRingUrl(url);
                mAlarmClock.setRingPager(ringPager);
                L.e("name:->" + mAlarmClock.getRingName());
                L.e("url->" + mAlarmClock.getRingUrl());
                L.e("ringPager:->" + mAlarmClock.getRingPager());
                break;
        }
    }
}