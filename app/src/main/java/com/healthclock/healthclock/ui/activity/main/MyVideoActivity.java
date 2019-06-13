package com.healthclock.healthclock.ui.activity.main;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.other.VideoModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.indent.ConfirmOrderActivity;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.adapter.ViedeoAdapter;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.T;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class MyVideoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private int pageNum = 1;
    private static final int PAGE_SIZE = 6;//为什么是6呢？
    private boolean isErr = true;
    private int mCurrentCounter = 0;
    int TOTAL_COUNTER = 0;

    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    private List<VideoModel.ListBean> data;
    private ViedeoAdapter mAdapter;
    private VideoModel mVideoModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        intUI();
    }

    private void intUI() {
        setBackView();
        setTitle("现在做操");
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initAdapter();
    }
    private void initAdapter() {
        mAdapter = new ViedeoAdapter(R.layout.video_items, data);
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
        String token = getToken(mContext);
        RetrofitUtil.getInstance().VideoList(token, "", String.valueOf(pageNum), new Subscriber<BaseResponse<VideoModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(BaseResponse<VideoModel> baseResponse) {

                if (baseResponse.getStatus() == 1) {
                    mVideoModel = baseResponse.getData();
                    TOTAL_COUNTER = Integer.valueOf(mVideoModel.getTotal());
                    List<VideoModel.ListBean> list = mVideoModel.getList();
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
