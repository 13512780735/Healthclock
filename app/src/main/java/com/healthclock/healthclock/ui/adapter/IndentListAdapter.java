package com.healthclock.healthclock.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.indent.OrderListModel;
import com.healthclock.healthclock.util.ImageLoaderUtils;
import com.healthclock.healthclock.widget.BorderTextView;

import java.util.List;

public class IndentListAdapter extends BaseQuickAdapter<OrderListModel.ListBean, BaseViewHolder> {
    public IndentListAdapter(int layoutResId, @Nullable List<OrderListModel.ListBean> data) {
        super(R.layout.indent_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListModel.ListBean item) {
        ImageLoaderUtils imageLoaderUtils = ImageLoaderUtils.getInstance(mContext);
        helper.setText(R.id.tv_indent_number, "订单编号：" + item.getPayment());
        imageLoaderUtils.displayImage(item.getOrderInfo().getPicPath(), (ImageView) helper.getView(R.id.iv_shop_avatar));
        helper.setText(R.id.tv_shop_name, item.getOrderInfo().getTitle());
        helper.setText(R.id.tv_shop_price, "¥ " + item.getOrderInfo().getPrice());
        helper.setText(R.id.tv_shop_size, "规格：大");
        helper.setText(R.id.tv_shop_number, "x " + item.getOrderInfo().getNum());
        helper.setText(R.id.tv_total_number, "实付：");
        helper.setText(R.id.tv_total_price, "¥ " + item.getOrderInfo().getTotalFee());
        LinearLayout ll_indent_button = helper.getView(R.id.ll_indent_button);//底部按钮
        BorderTextView tvCancelDelIndent = helper.getView(R.id.tv_cancel_del_indent);
        BorderTextView tvAppraiseIndent = helper.getView(R.id.tv_appraise_indent);
        BorderTextView tvPay = helper.getView(R.id.tv_pay);
        int status = item.getStatus();//状态0.已取消 1.待付款 2.已付款 3.待发货 4.已完成

        if (status == 0) {
            helper.setText(R.id.tv_indent_status, "已取消");
            ll_indent_button.setVisibility(View.VISIBLE);
            tvAppraiseIndent.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvCancelDelIndent.setVisibility(View.VISIBLE);
            tvCancelDelIndent.setText("删除订单");
        } else if (status == 1) {
            helper.setText(R.id.tv_indent_status, "待付款");
            ll_indent_button.setVisibility(View.VISIBLE);
            tvAppraiseIndent.setVisibility(View.GONE);
            tvPay.setVisibility(View.VISIBLE);
            tvCancelDelIndent.setVisibility(View.VISIBLE);
            tvCancelDelIndent.setText("取消订单");
            tvPay.setText("支付订单");
        } else if (status == 2) {
            helper.setText(R.id.tv_indent_status, "待发货");
            ll_indent_button.setVisibility(View.GONE);
        } else if (status == 3) {
            helper.setText(R.id.tv_indent_status, "待收货");
            ll_indent_button.setVisibility(View.VISIBLE);
            tvAppraiseIndent.setVisibility(View.GONE);
            tvPay.setVisibility(View.VISIBLE);
            tvCancelDelIndent.setVisibility(View.GONE);
            tvPay.setText("确认收货");
        } else if (status == 4) {
            helper.setText(R.id.tv_indent_status, "已完成");
            ll_indent_button.setVisibility(View.VISIBLE);
            tvAppraiseIndent.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.GONE);
            tvCancelDelIndent.setVisibility(View.VISIBLE);
            tvCancelDelIndent.setText("删除订单");
            tvAppraiseIndent.setText("评价");
        }
    }
}
