package com.healthclock.healthclock.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.indent.OrderListModel;

import java.util.List;

public class IndentListAdapter extends BaseQuickAdapter<OrderListModel.ListBean,BaseViewHolder>{
    public IndentListAdapter(int layoutResId, @Nullable List<OrderListModel.ListBean> data) {
        super(R.layout.indent_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListModel.ListBean item) {

    }
}
