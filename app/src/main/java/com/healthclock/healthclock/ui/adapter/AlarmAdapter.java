package com.healthclock.healthclock.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.MyClickListener;
import com.healthclock.healthclock.network.model.main.AlarmInfo;

import java.util.List;

/**
 * 闹钟适配器
 */
// 创建 Adapter
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.VH> {

    //定义一个集合，接受Activity 中传递的数据和上下文
    private Context mContext;
    private List<AlarmInfo> mDatas;
    private MyClickListener myClickListener;

    public AlarmAdapter(Context context, List<AlarmInfo> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public void setData(List<AlarmInfo> pDatas) {
        mDatas = pDatas;
        this.notifyDataSetChanged();
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    //创建 Item View
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        VH vh = new VH(LayoutInflater.from(mContext).inflate(R.layout.alarm_item, viewGroup, false));
        return vh;
    }

    //将数据绑定到 Item 中
    @Override
    public void onBindViewHolder(@NonNull final VH vh, final int i) {

        // 设置闹钟信息
        AlarmInfo alarmInfo = mDatas.get(i);
        vh.alarm_time.setText(alarmInfo.getAlarm_time());
        vh.repeat.setText(alarmInfo.getRepeat());
        vh.note.setText(alarmInfo.getNote());
        vh.checkBox.setChecked(alarmInfo.isCheckBox());

        //Item 点击事件
        vh.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑 闹钟
                if (myClickListener != null) {
                    myClickListener.onClick(v, i);
                }
            }
        });

        vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 开关 是否打开
                if (isChecked) {
                   // setDefault();
                } else {
                    //Todo
                }
            }
        });
    }

    //获得 Item 数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //创建 ViewHolder 初始化 每个 Item 内容
    public static class VH extends RecyclerView.ViewHolder {
        public RelativeLayout item;
        TextView alarm_time, repeat, note;
        Switch checkBox;

        VH(View v) {
            super(v);

            item = v.findViewById(R.id.item);
            checkBox = v.findViewById(R.id.alarm_on_off);
            alarm_time = v.findViewById(R.id.alarm_time);
            repeat = v.findViewById(R.id.repeat);
            note = v.findViewById(R.id.note);
        }
    }
}
