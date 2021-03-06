package com.healthclock.healthclock.ui.fragment.main;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.good.ShopListModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.indent.ConfirmOrderActivity;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.adapter.ShopListAdapter;
import com.healthclock.healthclock.ui.base.BaseFragment;
import com.healthclock.healthclock.util.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int pageNum = 1;
    private static final int PAGE_SIZE = 6;//为什么是6呢？
    private boolean isErr = true;
    private int mCurrentCounter = 0;
    int TOTAL_COUNTER = 0;

    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;

    private List<ShopListModel.ListBean> data;
    private ShopListAdapter mAdapter;
    private ShopListModel mShopListModel;
    private ConvenientBanner mBanner;

    private List<String> networkImage = new ArrayList<>();
    String[] images = {
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://img2.3lian.com/2014/f2/37/d/41.jpg",
    };


    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void lazyLoad() {
        //setBackView();
        setTitle("真视明");
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void initUI() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.shop_header_items, null);
        mBanner = header.findViewById(R.id.banner);
        data = new ArrayList<>();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        initAdapter();
        mAdapter.addHeaderView(header);
        mBanner.startTurning(4000);
        networkImage = Arrays.asList(images);

        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImage).setPageIndicator(new int[]{R.drawable.indicator_gray, R.drawable.indicator_red}).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).setScrollDuration(1500);

    }
    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(getActivity()).load(data).into(imageView);
        }
    }
    private void initAdapter() {
        mAdapter = new ShopListAdapter(R.layout.shop_list_item, data);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.disableLoadMoreIfNotFullPage();
        initData(pageNum, false);
        mCurrentCounter = mAdapter.getData().size();

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                toActivity(ConfirmOrderActivity.class);
            }
        });
    }

    private void initData(int pageNum, final boolean isloadmore) {
        String token = getToken(getActivity());
        RetrofitUtil.getInstance().GetShopList(token, "", String.valueOf(pageNum), new Subscriber<BaseResponse<ShopListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(BaseResponse<ShopListModel> baseResponse) {

                if (baseResponse.getStatus() == 1) {
                    mShopListModel = baseResponse.getData();
                    TOTAL_COUNTER = Integer.valueOf(mShopListModel.getTotal());
                    List<ShopListModel.ListBean> list = mShopListModel.getList();
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
                    T.showShort(getActivity(), baseResponse.getMsg());
                    Bundle bundle = new Bundle();
                    bundle.putString("isLogin", "1");
                    toActivity(LoginActivity.class, bundle);
                } else {
                    mAdapter.setEmptyView(R.layout.notdata_view);
                    // T.showShort(mContext, baseResponse.getMsg());

                }

            }
        });
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

}
