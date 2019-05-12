package com.healthclock.healthclock.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.FalseModel;

import java.util.List;

public class HealthScoreAdapter extends BaseQuickAdapter<FalseModel, BaseViewHolder> {
    public HealthScoreAdapter(int layoutResId, @Nullable List<FalseModel> data) {
        super(R.layout.health_score_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FalseModel item) {
        helper.setText(R.id.tv_time, "05月01日");
        helper.setText(R.id.tv_content, "这是测试数据" + item.getTitle());
    }
}
