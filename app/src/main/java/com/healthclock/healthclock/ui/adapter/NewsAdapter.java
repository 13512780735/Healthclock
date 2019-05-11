package com.healthclock.healthclock.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.FalseModel;

import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<FalseModel, BaseViewHolder> {
    public NewsAdapter(int layoutResId,  List<FalseModel> data) {
        super(R.layout.news_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FalseModel item) {
        helper.setText(R.id.tv_news, "测试数据123");
    }
}
