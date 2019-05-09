package com.healthclock.healthclock.clock.fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.clock.broadcast.AlarmClockBroadcast;
import com.healthclock.healthclock.clock.common.WeacConstants;
import com.healthclock.healthclock.clock.common.WeacStatus;
import com.healthclock.healthclock.clock.model.AlarmClock;
import com.healthclock.healthclock.clock.activity.AlarmClockNapNotificationActivity;
import com.healthclock.healthclock.clock.util.AudioPlayer;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.clock.view.MySlidingView;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmClockOntimeFragment extends BaseFragment implements
        View.OnClickListener

{


//    public AlarmClockOntimeFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_alarm_clock_ontime, container, false);
//    }


    /**
     * Log tag ：AlarmClockOntimeFragment
     */
    private static final String LOG_TAG = "AlarmClockOntimeFragment";

    /**
     * 当前时间
     */
    private TextView mTimeTv;

    /**
     * 闹钟实例
     */
    private AlarmClock mAlarmClock;

    /**
     * 线程运行flag
     */
    private boolean mIsRun = true;

    /**
     * 线程标记
     */
    private static final int UPDATE_TIME = 1;

    /**
     * 通知消息管理
     */
    private NotificationManagerCompat mNotificationManager;

    /**
     * 小睡间隔
     */
    private int mNapInterval;

    /**
     * 小睡次数
     */
    private int mNapTimes;

    /**
     * 是否点击按钮
     */
    private boolean mIsOnclick = false;

    /**
     * 小睡已执行次数
     */
    private int mNapTimesRan;

    /**
     * 声音管理
     */
    private AudioManager mAudioManager;

    /**
     * 当前音量
     */
    private int mCurrentVolume;

    /**
     * 显示当前时间Handler
     */
    private ShowTimeHandler mShowTimeHandler;

    private ViewGroup mWeatherInfoGroup;
    private ProgressBar mWeatherPbar;
    private TextView mWeatherTypeTv;
    private TextView mUmbrellaTv;
    private String mCurrentTimeDisplay = "";

    /**
     * 显示当前时间
     */
    static class ShowTimeHandler extends Handler {
        private WeakReference<AlarmClockOntimeFragment> mWeakReference;

        public ShowTimeHandler(AlarmClockOntimeFragment alarmClockOntimeFragment) {
            mWeakReference = new WeakReference<>(alarmClockOntimeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AlarmClockOntimeFragment alarmClockOntimeFragment = mWeakReference.get();

            switch (msg.what) {
                case UPDATE_TIME:
                    alarmClockOntimeFragment.mTimeTv.setText(msg.obj.toString());
                    alarmClockOntimeFragment.mCurrentTimeDisplay =
                            alarmClockOntimeFragment.mTimeTv.getText().toString();
                    break;
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(LOG_TAG, getActivity().toString() + "：onCreate");

        // 启动的Activity个数加1
        WeacStatus.sActivityNumber++;

        // 画面出现在解锁屏幕上,显示,常亮
        getActivity().getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAlarmClock = getActivity().getIntent()
                .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        if (mAlarmClock != null) {
            // 取得小睡间隔
            mNapInterval = mAlarmClock.getNapInterval();
            // 取得小睡次数
            mNapTimes = mAlarmClock.getNapTimes();
        }
        // XXX:修正小睡数
        // mNapTimes = 1000;
        // 小睡已执行次数
        mNapTimesRan = getActivity().getIntent().getIntExtra(
                WeacConstants.NAP_RAN_TIMES, 0);
        // 播放铃声
        playRing();

        mNotificationManager = NotificationManagerCompat.from(getActivity());
        if (mAlarmClock != null) {
            // 取消下拉列表通知消息
            mNotificationManager.cancel(mAlarmClock.getId());
        }

        mShowTimeHandler = new ShowTimeHandler(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.d(LOG_TAG, getActivity().toString() + "：onCreateView");

        View view = inflater.inflate(R.layout.fragment_alarm_clock_ontime, container,
                false);
        mTimeTv = (TextView) view.findViewById(R.id.ontime_time);
        // 显示现在时间
        mTimeTv.setText(new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date()));
        mCurrentTimeDisplay = mTimeTv.getText().toString();
        // 启动更新时间线程
        new Thread(new TimeUpdateThread()).start();

        // 标签
        TextView tagTv = (TextView) view.findViewById(R.id.ontime_tag);
        if (mAlarmClock != null) {
            tagTv.setText(mAlarmClock.getTag());
        } else {
            tagTv.setText(getString(R.string.alarm_error));
            tagTv.setTextColor(Color.RED);
        }

        // 小睡按钮
        TextView napTv = (TextView) view.findViewById(R.id.ontime_nap);

        // 小睡开启状态
        if (mAlarmClock != null && mAlarmClock.isNap()) {
            // 当执行X次小睡后隐藏小睡按钮
            if (mNapTimesRan != mNapTimes) {
                // 设置小睡
                napTv.setText(String.format(
                        getString(R.string.touch_here_nap), mNapInterval));
                napTv.setOnClickListener(this);
            } else {
                napTv.setVisibility(View.GONE);
            }
        } else {
            napTv.setVisibility(View.GONE);
        }

        L.i(LOG_TAG, "小睡次数：" + mNapTimes);

        // 滑动提示
        TextView slidingTipIv = (TextView) view.findViewById(R.id.sliding_tip_tv);
        final AnimationDrawable animationDrawable = (AnimationDrawable) slidingTipIv.getCompoundDrawables()[0];
        // 直接启动动画，测试4.0模拟器没有动画效果
        slidingTipIv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });

        MySlidingView mySlidingView = (MySlidingView) view.findViewById(R.id.my_sliding_view);
        mySlidingView.setSlidingTipListener(new MySlidingView.SlidingTipListener() {
            @Override
            public void onSlidFinish() {
                // 执行关闭操作
                finishActivity();
            }
        });

//        // 天气提示
//        if (mAlarmClock != null && mAlarmClock.isWeaPrompt()) {
//            mWeatherInfoGroup = (ViewGroup) view.findViewById(R.id.weather_info_group);
//            mWeatherPbar = (ProgressBar) view.findViewById(R.id.progress_bar);
//            mWeatherTypeTv = (TextView) view.findViewById(R.id.weather_type_tv);
//            mUmbrellaTv = (TextView) view.findViewById(R.id.umbrella_tv);
//            // 初始化天气预报
//            initWeather();
//        }
        return view;
    }

//    private void initWeather() {
//        try {
//            // 判断网络是否可用
//            if (!MyUtil.isNetworkAvailable(getActivity())) {
//                return;
//            }
//
//            SharedPreferences share = getActivity().getSharedPreferences(
//                    WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
//            // 没有默认城市自动定位
//            String weatherCode = share.getString(WeacConstants.DEFAULT_WEATHER_CODE,
//                    getString(R.string.auto_location));
//
//            String cityName;
//            String address;
//            // 自动定位
//            if (weatherCode.equals(getString(R.string.auto_location))) {
//                cityName = share.getString(WeacConstants.DEFAULT_CITY_NAME, null);
//                address = null;
//            } else {
//                cityName = null;
//                address = getString(R.string.address_weather, weatherCode);
//            }
//            mWeatherPbar.setVisibility(View.VISIBLE);
//            HttpUtil.sendHttpRequest(address, cityName,
//                    new HttpCallbackListener() {
//                        @Override
//                        public void onFinish(String response) {
//                            try {
//                                if (!response.contains("error")) {
//                                    WeatherInfo weatherInfo = WeatherUtil.handleWeatherResponse(
//                                            new ByteArrayInputStream(response.getBytes()));
//                                    getActivity().runOnUiThread(new SetWeatherInfoRunnable(weatherInfo));
//                                    // 无法解析当前位置
//                                } else {
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mWeatherPbar.setVisibility(View.GONE);
//                                        }
//                                    });
//                                }
//                            } catch (Exception e) {
//                                LogUtil.e(LOG_TAG, "initWeather(): " + e.toString());
//                            }
//                        }
//
//                        @Override
//                        public void onError(final Exception e) {
//                            try {
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mWeatherPbar.setVisibility(View.GONE);
//                                    }
//                                });
//                            } catch (Exception e1) {
//                                LogUtil.e(LOG_TAG, e1.toString());
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            LogUtil.e(LOG_TAG, "initWeather()" + e.toString());
//            if (mWeatherPbar != null) {
//                mWeatherPbar.setVisibility(View.GONE);
//            }
    //     }
    //   }


    @Override
    public void onStop() {
        super.onStop();
//        LogUtil.d(LOG_TAG, getActivity().toString() + "：onStop");
        // 当第二个闹钟响起时第一个闹钟需要进入小睡或关闭闹钟（启动此Activity时加上
        // 【Intent.FLAG_ACTIVITY_CLEAR_TOP】flag 会自动关闭当前Activity，只有
        // 【Intent.FLAG_ACTIVITY_NEW_TASK】 flag的话，
        // 只是暂停，当第二个Activity结束后后会重新恢复显示）

//        LogUtil.d(LOG_TAG, getActivity().toString() + "：activityNumber: "
//                + WeacStatus.sActivityNumber);

        // 当点击关闭或者小睡按钮或者画面关闭状态时或点击电源键闹钟响起会执行一次onStop()
        // 当点击按钮
        // if (mIsOnclick) {
        // // 点击按钮后，执行程序结束处理，故Activity数减1
        // WeacStatus.activityNumber--;
        // return;
        // }
        // // 第二个闹钟Activity启动
        // if (WeacStatus.activityNumber > 1) {
        // WeacStatus.activityNumber--;
        // // // 停止运行更新时间的线程
        // // mIsRun = false;
        // // // 小睡
        // // nap();
        //
        // }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击小睡
            case R.id.ontime_nap:
                // 执行小睡操作
                onClickNapButton();
                break;
        }
    }

    /**
     * 执行结束当前Activity操作
     */
    private void finishActivity() {
        // 点击按钮标记
        mIsOnclick = true;

        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
    }

    /**
     * 当点击小睡按钮
     */
    private void onClickNapButton() {
        if (!(mNapTimesRan == mNapTimes)) {
            // 小睡
            nap();
        }
        // 执行关闭操作
        finishActivity();
    }

    /**
     * 小睡
     */
    @TargetApi(19)
    private void nap() {
        // 当小睡执行了X次
        if (mNapTimesRan == mNapTimes || mAlarmClock == null) {
            return;
        }
        // 小睡次数加1
        mNapTimesRan++;
        L.d(LOG_TAG, "已执行小睡次数：" + mNapTimesRan);

        // 设置小睡相关信息
        Intent intent = new Intent(getActivity(), AlarmClockBroadcast.class);
        intent.putExtra(WeacConstants.ALARM_CLOCK, mAlarmClock);
        intent.putExtra(WeacConstants.NAP_RAN_TIMES, mNapTimesRan);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(),
                -mAlarmClock.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity()
                .getSystemService(Activity.ALARM_SERVICE);
        // XXX
        // 下次响铃时间
        long nextTime = System.currentTimeMillis() + 1000 * 60 * mNapInterval;

        L.i(LOG_TAG, "小睡间隔:" + mNapInterval + "分钟");

        // 当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }

        // 设置通知相关信息
        Intent it = new Intent(getActivity(),
                AlarmClockNapNotificationActivity.class);
        it.putExtra(WeacConstants.ALARM_CLOCK, mAlarmClock);
        // FLAG_UPDATE_CURRENT 点击通知有时不会跳转！！
        // FLAG_ONE_SHOT 清除列表只响应一个
        PendingIntent napCancel = PendingIntent.getActivity(getActivity(),
                mAlarmClock.getId(), it,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // 下拉列表通知显示的时间
        CharSequence time = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(nextTime);

        // 通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        // 设置PendingIntent
        Notification notification = builder.setContentIntent(napCancel)
                // 当清除下拉列表触发
                .setDeleteIntent(napCancel)
                // 设置下拉列表标题
                .setContentTitle(
                        String.format(getString(R.string.xx_naping),
                                mAlarmClock.getTag()))
                // 设置下拉列表显示内容
                .setContentText(String.format(getString(R.string.nap_to), time))
                // 设置状态栏显示的信息
                .setTicker(
                        String.format(getString(R.string.nap_time),
                                mNapInterval))
                // 设置状态栏（小图标）
                .setSmallIcon(R.drawable.ic_nap_notification)
                // 设置下拉列表（大图标）
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher)).setAutoCancel(true)
                // 默认呼吸灯
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.FLAG_SHOW_LIGHTS)
                .build();
/*        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;*/

        // 下拉列表显示小睡信息
        mNotificationManager.notify(mAlarmClock.getId(), notification);
    }

    /**
     * 播放铃声
     */
    private void playRing() {
        mAudioManager = (AudioManager) getActivity().getSystemService(
                Context.AUDIO_SERVICE);
        mCurrentVolume = mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mAlarmClock != null) {
            // 设置铃声音量
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    mAlarmClock.getVolume(), AudioManager.ADJUST_SAME);

            // 默认铃声
            if (mAlarmClock.getRingUrl().equals(WeacConstants.DEFAULT_RING_URL)
                    || TextUtils.isEmpty(mAlarmClock.getRingUrl())) {
                // 振动模式
                if (mAlarmClock.isVibrate()) {
                    // 播放
                    AudioPlayer.getInstance(getActivity()).playRaw(
                            R.raw.ring_weac_alarm_clock_default, true, true);
                } else {
                    AudioPlayer.getInstance(getActivity()).playRaw(
                            R.raw.ring_weac_alarm_clock_default, true, false);
                }

                // 无铃声
            } else if (mAlarmClock.getRingUrl().equals(WeacConstants.NO_RING_URL)) {
                // 振动模式
                if (mAlarmClock.isVibrate()) {
                    AudioPlayer.getInstance(getActivity()).stop();
                    AudioPlayer.getInstance(getActivity()).vibrate();
                } else {
                    AudioPlayer.getInstance(getActivity()).stop();
                }
            } else {
                // 振动模式
                if (mAlarmClock.isVibrate()) {
                    AudioPlayer.getInstance(getActivity()).play(
                            mAlarmClock.getRingUrl(), true, true);
                } else {
                    AudioPlayer.getInstance(getActivity()).play(
                            mAlarmClock.getRingUrl(), true, false);
                }
            }
        } else {
            AudioPlayer.getInstance(getActivity()).playRaw(
                    R.raw.ring_weac_alarm_clock_default, true, true);
        }
    }

    /**
     * 显示时间的线程类
     */
    private class TimeUpdateThread implements Runnable {
        /**
         * 闹钟响铃时间
         */
        private int startedTime = 0;

        /**
         * 3分钟
         */
        private static final int TIME = 60 * 3;

        @Override
        public void run() {
            // Activity没有结束
            while (mIsRun) {
                L.d(LOG_TAG, "TimeUpdateThread(已启动时间): " + startedTime);

                try {
                    // 响铃XX分钟并且当前Activity没有被销毁进入小睡
                    if (startedTime == TIME) {
                        // 小睡开启状态
                        if (mAlarmClock != null && mAlarmClock.isNap()) {
                            if (!getActivity().isFinishing()) {
                                onClickNapButton();
                                return;
                            } else {
                                L.e(LOG_TAG,
                                        "准备进行自动小睡处理时，闹钟已经为Finishing状态");
                                return;
                            }
                        } else {
                            // 执行关闭操作
                            finishActivity();
                        }
                    }
                    Thread.sleep(1000);
                    startedTime++;
                    // 界面显示的时间
                    CharSequence currentTime = new SimpleDateFormat("HH:mm",
                            Locale.getDefault()).format(System
                            .currentTimeMillis());
                    if (mCurrentTimeDisplay.equals(currentTime)) {
                        continue;
                    }

                    Message msg = mShowTimeHandler.obtainMessage(UPDATE_TIME,
                            currentTime);
                    // 发送消息
                    mShowTimeHandler.sendMessage(msg);
                } catch (InterruptedException | NullPointerException e) {
                    L.e(LOG_TAG, "run方法出现错误：" + e.toString());
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(LOG_TAG, getActivity().toString() + "：onDestroy");
        // 停止运行更新时间的线程
        mIsRun = false;

        // 当没有点击按钮，则当前响铃被新闹钟任务杀死，开启小睡
        if (!mIsOnclick) {
            // 小睡
            nap();
        }

        // 当前只有一个Activity
        if (WeacStatus.sActivityNumber <= 1) {
            // 停止播放
            AudioPlayer.getInstance(getActivity()).stop();
        }

        // 启动的Activity个数减一
        WeacStatus.sActivityNumber--;

        // If null, all callbacks and messages will be removed.
        if (mShowTimeHandler != null) {
            mShowTimeHandler.removeCallbacksAndMessages(null);
        }

        // 复原手机媒体音量
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mCurrentVolume, AudioManager.ADJUST_SAME);
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
