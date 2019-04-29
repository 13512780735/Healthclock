package com.healthclock.healthclock.ui.activity.indent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.indent.CreateOrderModel;
import com.healthclock.healthclock.network.model.indent.DefaultAddressModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.activity.member.SelectAddressActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.wxapi.PayActivity;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class ConfirmOrderActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.ll_address_default)
    LinearLayout mLlAddressDefault;//有地址显示
    @BindView(R.id.rl_address_default)
    RelativeLayout mRlAddressDefault;//无地址时

    @BindView(R.id.iv_shop_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.tv_shop_size)
    TextView tvShopSize;
    @BindView(R.id.tv_shop_number)
    TextView tvShopNumber;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_expressage)
    TextView tvExpressage;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    private CreateOrderModel mCreateOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        setBackView();
        setTitle("确认订单");
//        initAddress();
//        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAddress();
        initOrder();

    }

    private void initUI() {
        if (mCreateOrderModel == null) {
            tvShopName.setText("");
            tvShopPrice.setText("¥ " + "");
            tvShopSize.setText("");
            tvShopNumber.setText("X " + "");
            tvTotalPrice.setText("¥ " + "");
            tvExpressage.setText("¥ 0.0");
            tvPrice.setText("¥ " + "");
        } else
            tvShopName.setText(mCreateOrderModel.getTitle());
        tvShopPrice.setText("¥ " + mCreateOrderModel.getPrice());
        tvShopSize.setText(mCreateOrderModel.getDescription());
        tvShopNumber.setText("X " + mCreateOrderModel.getNum());
        tvTotalPrice.setText("¥ " + mCreateOrderModel.getTotalFee());
        tvExpressage.setText("¥ 0.0");
        tvPrice.setText("¥ " + mCreateOrderModel.getTotalFee());

    }

    /**
     * 创建订单
     */
    private void initOrder() {
        String token = getToken(mContext);
//        RetrofitUtil.getInstance().CreateOrder(token, new Subscriber<BaseResponse<CreateOrderModel>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(BaseResponse<CreateOrderModel> baseResponse) {
//                if (baseResponse.getStatus() == 1) {
//                    mCreateOrderModel = baseResponse.getData();
//                } else if (baseResponse.getStatus() == -1) {
//                    T.showShort(mContext, baseResponse.getMsg());
//                    Bundle bundle = new Bundle();
//                    bundle.putString("isLogin", "1");
//                    toActivity(LoginActivity.class, bundle);
//                } else {
//                    T.showShort(mContext, baseResponse.getMsg());
//                }
//                initUI();
//            }
//        });
    }

    /**
     * 获取默认地址
     */
    private void initAddress() {
        String token = getToken(mContext);
        RetrofitUtil.getInstance().defaultAddress(token, new Subscriber<BaseResponse<DefaultAddressModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<DefaultAddressModel> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    mLlAddressDefault.setVisibility(View.VISIBLE);
                    mRlAddressDefault.setVisibility(View.GONE);
                    DefaultAddressModel defaultAddressModel = baseResponse.getData();
                    mTvName.setText(defaultAddressModel.getRemark());
                    mTvPhone.setText("  " + defaultAddressModel.getCountry());
                    mTvAddress.setText(defaultAddressModel.getProvince() + defaultAddressModel.getCity() + defaultAddressModel.getDistrict() + defaultAddressModel.getAddress());
                } else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    Bundle bundle = new Bundle();
                    bundle.putString("isLogin", "1");
                    toActivity(LoginActivity.class, bundle);
                } else {
                    T.showShort(mContext, baseResponse.getMsg());
                    mRlAddressDefault.setVisibility(View.VISIBLE);
                    mLlAddressDefault.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.ll_address_default, R.id.rl_address_default, R.id.tv_go_to_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_address_default:
            case R.id.rl_address_default:
                toActivity(SelectAddressActivity.class);
                break;
            case R.id.tv_go_to_pay:
                createIndent();
                break;
        }
    }

    private void createIndent() {
//        if (mCreateOrderModel == null) {
//            showProgress("请选择地址");
//            return;
//        } else {
//            toActivity(PayActivity.class);
//        }
        toActivity(PayActivity.class);
    }


}
