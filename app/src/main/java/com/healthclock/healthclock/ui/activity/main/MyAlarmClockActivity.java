package com.healthclock.healthclock.ui.activity.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupWindow;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.common.WeacConstants;
import com.healthclock.healthclock.db.AlarmClockOperate;
import com.healthclock.healthclock.listener.MyClickListener;
import com.healthclock.healthclock.listener.OnItemClickListener;
import com.healthclock.healthclock.network.model.main.AlarmClock;
import com.healthclock.healthclock.network.model.main.AlarmInfo;
import com.healthclock.healthclock.ui.adapter.AlarmAdapter;
import com.healthclock.healthclock.ui.adapter.AlarmClockAdapter;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.MyUtil;
import com.healthclock.healthclock.util.OttoAppConfig;
import com.healthclock.healthclock.util.PopupWindowUtil;
import com.healthclock.healthclock.widget.IconFontTextView;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

public class MyAlarmClockActivity extends BaseActivity implements MyClickListener {
    @BindView(R.id.tv_right)
    IconFontTextView tvRight;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;


    /**
     * 新建闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_NEW = 1;

    /**
     * 修改闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_EDIT = 2;

    boolean isDelete;
    int position;
    AlarmInfo alarmInfo;
    private Intent intent;
    private AlarmAdapter mAlarmAdapter;
    public List<AlarmInfo> mDatas = new ArrayList<>();
    private List<AlarmClock> mAlarmClockList;
    private AlarmClockAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alarm_clock);
        setBackView();
        initUI();
    }

    @OnClick({R.id.tv_right})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                showPopupWindow(tvRight);
                setBackgroundAlpha(0.7f);
                break;
        }
    }

    private void initUI() {
        intent = new Intent(mContext, EditAlarmActivity.class);

        mAlarmAdapter = new AlarmAdapter(mContext, mDatas);

        //初始化 RecyclerView
        mRecyclerView.setAdapter(mAlarmAdapter);
        mAlarmAdapter.setMyClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        //设置 RecyclerView 分割线
        DividerItemDecoration divider = new DividerItemDecoration(Objects.requireNonNull(mContext), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.my_divider)));
        mRecyclerView.addItemDecoration(divider);


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
                        intent.putExtra("isAdd", true);
                        startActivityForResult(intent, 1);
                        break;
                    case R.id.menu_item2:
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            position = data.getIntExtra("position", 0);
            isDelete = data.getBooleanExtra("isDelete", false);
            alarmInfo = (AlarmInfo) data.getSerializableExtra("alarmInfo");
            //添加 闹钟 (Item)
            if (requestCode == 1) {
                mDatas.add(alarmInfo);
                mAlarmAdapter.setData(mDatas);
            }
            //更新 闹钟 数据
            if (requestCode == 2) {
                mDatas.set(position, alarmInfo);
                mAlarmAdapter.setData(mDatas);
            }
            //删除 闹钟
            if (isDelete) {
                mDatas.remove(position);
                mAlarmAdapter.setData(mDatas);
            }
        }
    }

    // 点击 闹钟 Item 跳转 编辑 界面
    @Override
    public void onClick(View v, int position) {
        //传入 需要修改的 数据
        intent.putExtra("isAdd", false);
        intent.putExtra("updateAlarmInfo", mDatas.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, 2);
    }
}
