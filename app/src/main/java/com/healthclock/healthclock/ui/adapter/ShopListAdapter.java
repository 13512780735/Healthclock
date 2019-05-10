package com.healthclock.healthclock.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.good.ShopListModel;
import com.healthclock.healthclock.util.ImageLoaderUtils;
import com.healthclock.healthclock.widget.RatioImageView;
import com.zzhoujay.richtext.RichText;

import java.util.List;

public class ShopListAdapter extends BaseQuickAdapter<ShopListModel.ListBean, BaseViewHolder> {
    public ShopListAdapter(int layoutResId, @Nullable List<ShopListModel.ListBean> data) {
        super(R.layout.shop_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopListModel.ListBean item) {
        ImageLoaderUtils imageLoaderUtils = ImageLoaderUtils.getInstance(mContext);
        // RichText.from(item.getContent()).into((TextView) helper.getView(R.id.tv_content));
        //helper.setText(R.id.tv_price, item.getPrice());
        helper.setText(R.id.tv_price, "¥ 396");
        helper.setText(R.id.tv_content, "磁力健身球/潜能开发/预防近视保养颈椎提神醒脑");
      //  imageLoaderUtils.displayImage(item.getUrl(), (RatioImageView) helper.getView(R.id.iv_shop_img));
        helper.addOnClickListener(R.id.tv_buy);
    }
}
