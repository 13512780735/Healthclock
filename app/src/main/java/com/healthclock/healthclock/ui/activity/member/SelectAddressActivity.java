package com.healthclock.healthclock.ui.activity.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.indent.AddressModel;
import com.healthclock.healthclock.network.model.indent.ProvincesModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.AppManager;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class SelectAddressActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int pageNum = 1;
    private static final int PAGE_SIZE = 6;//为什么是6呢？
    private boolean isErr = true;
    private int mCurrentCounter = 0;
    int TOTAL_COUNTER = 0;

    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_add)
    BorderTextView tv_add;
    private String province, city, area, address;
    private List<AddressModel.ListBean> data;
    private AddressAdapter mAdapter;
    private AddressModel mAddressesModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("收货地址");
        data = new ArrayList<>();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new AddressAdapter(R.layout.layout_address_item01, data);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.disableLoadMoreIfNotFullPage();
        initData(pageNum, false);
        mCurrentCounter = mAdapter.getData().size();
    }

    private void initData(int pageNum, final boolean isloadmore) {
        RetrofitUtil.getInstance().GetAddressList(token, String.valueOf(pageNum), new Subscriber<BaseResponse<AddressModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(BaseResponse<AddressModel> baseResponse) {

                if (baseResponse.getStatus() == 1) {
                    mAddressesModel = baseResponse.getData();
                    TOTAL_COUNTER = Integer.valueOf(mAddressesModel.getTotal());
                    List<AddressModel.ListBean> list = mAddressesModel.getList();
                    if (list != null && list.size() > 0) {
                        if (!isloadmore) {
                            data = list;
                        } else {
                            data.addAll(list);
                        }
                        mAdapter.setNewData(data);
                        mAdapter.notifyDataSetChanged();
                    }

                } else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    toActivityFinish(LoginActivity.class);
                    AppManager.getAppManager().finishAllActivity();
                } else {
                    mAdapter.setEmptyView(R.layout.notdata_view);
                    showProgress(baseResponse.getMsg());
                }

            }
        });
    }

    public class AddressAdapter extends BaseQuickAdapter<AddressModel.ListBean, BaseViewHolder> {

        public AddressAdapter(int layoutResId, @Nullable List<AddressModel.ListBean> data) {
            super(R.layout.layout_address_item01, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final AddressModel.ListBean item) {
            helper.setText(R.id.tv_name, item.getRemark());
            helper.setText(R.id.tv_phone, item.getCountry());
            if (item.isIsdefault() == true) {
                helper.getView(R.id.tv_default).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.tv_default).setVisibility(View.GONE);
            }
            helper.setText(R.id.tv_address, item.getProvince() + item.getCity() + item.getDistrict() + item.getAddress());
            helper.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SelectAddressActivity.this, EditAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 1);
                    bundle.putString("id", String.valueOf(item.getId()));
                    bundle.putString("address", item.getAddress());
                    bundle.putString("realname", item.getRemark());
                    bundle.putString("mobile", item.getCountry());
                    bundle.putString("province", item.getProvince());
                    bundle.putString("city", item.getCity());
                    bundle.putString("area", item.getDistrict());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1000);
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);//禁止加载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mAdapter.setNewData(data);
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                pageNum = 1;//页数置为1 才能继续重新加载
                initData(pageNum, false);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setEnableLoadMore(true);//启用加载
            }
        }, 2000);
    }

    @Override
    public void onLoadMoreRequested() {

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    //数据全部加载完毕
                    mAdapter.loadMoreEnd();
                } else {
                    if (isErr) {
                        //成功获取更多数据
                        //  mQuickAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                        pageNum += 1;
                        initData(pageNum, true);
                        mCurrentCounter = mAdapter.getData().size();
                        mAdapter.loadMoreComplete();
                    } else {
                        //获取更多数据失败
                        isErr = true;
                        mAdapter.loadMoreFail();

                    }
                }
            }

        }, 3000);
    }

    @OnClick(R.id.tv_add)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_add:
                Intent intent = new Intent(SelectAddressActivity.this, EditAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 0);
                bundle.putString("id", "");
                bundle.putString("address", "");
                bundle.putString("realname", "");
                bundle.putString("mobile", "");
                bundle.putString("province", "");
                bundle.putString("city", "");
                bundle.putString("area", "");
                toActivity(EditAddressActivity.class, bundle);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1000);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            onRefresh();
        }
    }
}
