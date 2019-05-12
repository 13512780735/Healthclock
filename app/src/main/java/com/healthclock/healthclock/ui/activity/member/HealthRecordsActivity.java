package com.healthclock.healthclock.ui.activity.member;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.FalseModel;
import com.healthclock.healthclock.ui.adapter.HealthScoreAdapter;
import com.healthclock.healthclock.ui.adapter.NewsAdapter;
import com.healthclock.healthclock.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class HealthRecordsActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<FalseModel> data;
    private HealthScoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);
        initData();
        initUI();
    }

    public void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FalseModel falseModel = new FalseModel();
            falseModel.setTitle("" + i);
            data.add(falseModel);
        }
    }

    private void initUI() {
        setBackView();
        setTitle("我的健康记录");
        mRecyclerView=findView(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new HealthScoreAdapter(R.layout.health_score_items, data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }
}
