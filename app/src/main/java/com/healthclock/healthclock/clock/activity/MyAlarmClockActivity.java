package com.healthclock.healthclock.clock.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.clock.adapter.AlarmClockAdapter;
import com.healthclock.healthclock.clock.common.WeacConstants;
import com.healthclock.healthclock.clock.db.AlarmClockOperate;
import com.healthclock.healthclock.clock.listener.OnItemClickListener;
import com.healthclock.healthclock.clock.model.AlarmClock;
import com.healthclock.healthclock.clock.model.AlarmClockDeleteEvent;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.clock.util.MyUtil;
import com.healthclock.healthclock.clock.util.OttoAppConfig;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.PopupWindowUtil;
import com.healthclock.healthclock.clock.view.ErrorCatchLinearLayoutManager;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.widget.IconFontTextView;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

public class MyAlarmClockActivity extends BaseActivity {
    @BindView(R.id.tv_right)
    IconFontTextView tvRight;


    /**
     * 新建闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_NEW = 1;

    /**
     * 修改闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_EDIT = 2;

    /**
     * 闹钟列表
     */
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    /**
     * 保存闹钟信息的list
     */
    private List<AlarmClock> mAlarmClockList;

    /**
     * 保存闹钟信息的adapter
     */
    private AlarmClockAdapter mAdapter;

    /**
     * 操作栏编辑按钮
     */
    private ImageView mEditAction;

    /**
     * 操作栏编辑完成按钮
     */
    private ImageView mAcceptAction;

    /**
     * List内容为空时的视图
     */
    private LinearLayout mEmptyView;
    private int position1;
    private AlarmClock mDeletedAlarmClock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alarm_clock);
        OttoAppConfig.getInstance().register(this);
        mAlarmClockList = new ArrayList<>();
        mAdapter = new AlarmClockAdapter(this, mAlarmClockList);
        initUI();
    }

    @OnClick({R.id.tv_right, R.id.tv_back})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                showPopupWindow(tvRight);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private void initUI() {
        mEmptyView = (LinearLayout) findViewById(R.id.alarm_clock_empty);

        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new ErrorCatchLinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new ScaleInLeftAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(300);
        mRecyclerView.getItemAnimator().setRemoveDuration(300);
        mRecyclerView.getItemAnimator().setMoveDuration(300);
        mRecyclerView.getItemAnimator().setChangeDuration(300);
        mRecyclerView.setAdapter(mAdapter);

//        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // 监听闹铃item点击事件Listener
        OnItemClickListener onItemClickListener = new OnItemClickListenerImpl();
        mAdapter.setOnItemClickListener(onItemClickListener);


        updateList();


    }


    class OnItemClickListenerImpl implements OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            // 不响应重复点击
            if (MyUtil.isFastDoubleClick()) {
                return;
            }
            SharedPreferencesUtils.put(mContext,"position",position);
            position1 = position;
            AlarmClock alarmClock = mAlarmClockList.get(position);
            Intent intent = new Intent(mContext,
                    AlarmClockEditActivity.class);
            intent.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);

            L.e("position-->"+position);
            // 开启编辑闹钟界面
            startActivityForResult(intent, REQUEST_ALARM_CLOCK_EDIT);
            // 启动移动进入效果动画
            overridePendingTransition(R.anim.move_in_bottom,
                    0);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            // 显示删除，完成按钮，隐藏修改按钮
            //  displayDeleteAccept();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        AlarmClock ac = data
                .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        switch (requestCode) {
            // 新建闹钟
            case REQUEST_ALARM_CLOCK_NEW:
                // 插入新闹钟数据
//                TabAlarmClockOperate.getInstance(getActivity()).insert(ac);
                AlarmClockOperate.getInstance().saveAlarmClock(ac);
                addList(ac);

                showAlarmExplain();
                break;
            // 修改闹钟
            case REQUEST_ALARM_CLOCK_EDIT:
                // 更新闹钟数据
//                TabAlarmClockOperate.getInstance(getActivity()).update(ac);
                if ("0".equals(data.getStringExtra("flag"))) {
                    AlarmClockOperate.getInstance().updateAlarmClock(ac);
                    updateList();
                } else {
                    //AlarmClock alarmClock = data.getParcelableExtra(WeacConstants.ALARM_CLOCK);
                   deleteList(ac);
                }
                break;

        }
    }
    @Subscribe
    public void OnAlarmClockDelete(AlarmClockDeleteEvent event) {
        L.e("执行1……");
        //deleteList(event);

        mDeletedAlarmClock = event.getAlarmClock();

    }

    private void showAlarmExplain() {
        if (isShow()) {
            new AlertDialogWrapper.Builder(mContext)
                    .setTitle(mContext.getString(R.string.warm_tips_title))
                    .setMessage(mContext.getString(R.string.warm_tips_detail))
                    .setPositiveButton(R.string.roger, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(R.string.no_tip, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences share = mContext.getSharedPreferences(
                                    WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = share.edit();
                            editor.putBoolean(WeacConstants.ALARM_CLOCK_EXPLAIN, false);
                            editor.apply();
                        }
                    })
                    .show();
        }
    }

    private void addList(AlarmClock ac) {
        mAlarmClockList.clear();

        int id = ac.getId();
        int count = 0;
        int position = 0;
        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            mAlarmClockList.add(alarmClock);
            L.e("执行了3333！！！！");
            L.e("闹钟数据：-->" + alarmClock.getRingName());

            if (id == alarmClock.getId()) {
                position = count;
                if (alarmClock.isOnOff()) {
                    MyUtil.startAlarmClock(mContext, alarmClock);

                }
            }
            count++;
        }

        checkIsEmpty(list);

        mAdapter.notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
    }

    private void updateList() {
        mAlarmClockList.clear();

        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            mAlarmClockList.add(alarmClock);
            // 当闹钟为开时刷新开启闹钟
            if (alarmClock.isOnOff()) {
                MyUtil.startAlarmClock(mContext, alarmClock);
            }
        }

        checkIsEmpty(list);

        mAdapter.notifyDataSetChanged();
    }

    private void deleteList(AlarmClock alarmClock) {
       // mAlarmClockList.clear();
        AlarmClockOperate.getInstance().deleteAlarmClock(mAlarmClockList.get(position1));
        // 关闭闹钟
        MyUtil.cancelAlarmClock(mContext,
                alarmClock.getId());
        // 关闭小睡
        MyUtil.cancelAlarmClock(mContext,
                -alarmClock.getId());

        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        // 取消下拉列表通知消息
        notificationManager.cancel(alarmClock.getId());

        //mAlarmClockList.remove(position1);

        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
//        for (AlarmClock alarmClock1 : list) {
//            mAlarmClockList.add(alarmClock1);
//        }

        checkIsEmpty(list);

        mAdapter.notifyItemRemoved(position1);
        mAdapter.notifyDataSetChanged();
//        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
    }

    private void checkIsEmpty(List<AlarmClock> list) {
        L.e("size:-->"+list.size());
        if (list.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        }
    }

    private boolean isShow() {
        SharedPreferences share = mContext.getSharedPreferences(
                WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
        return share.getBoolean(WeacConstants.ALARM_CLOCK_EXPLAIN, true);
    }


    private PopupWindow mPopupWindow;

    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_set_clock_layout;   // 布局ID
        View contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.menu_item1:
                        //跳转到编辑闹钟界面
                        if (MyUtil.isFastDoubleClick()) {
                            return;
                        }
                        Intent intent = new Intent(mContext,
                                AlarmClockNewActivity.class);
                        // 开启新建闹钟界面
                        startActivityForResult(intent, REQUEST_ALARM_CLOCK_NEW);
                        // 启动渐变放大效果动画
                        overridePendingTransition(R.anim.zoomin, 0);
                        break;
                    case R.id.menu_item2:
                        //跳转到编辑闹钟界面
                        if (MyUtil.isFastDoubleClick()) {
                            return;
                        }
                        Intent intent1 = new Intent(mContext,
                                AlarmClockNew01Activity.class);
                        // 开启新建闹钟界面
                        startActivityForResult(intent1, REQUEST_ALARM_CLOCK_NEW);
                        // 启动渐变放大效果动画
                        overridePendingTransition(R.anim.zoomin, 0);
                        break;
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    setBackgroundAlpha(1f);
                }
            }
        };
        contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item2).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;//设置透明度（这是窗体本身的透明度，非背景）
        lp.dimAmount = bgAlpha;//设置灰度
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为 红米米手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    private void showPopupWindow(View anchorView) {
        View contentView = getPopupWindowContentView();
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }
}
