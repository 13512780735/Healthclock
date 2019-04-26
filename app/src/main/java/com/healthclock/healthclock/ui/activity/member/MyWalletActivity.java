package com.healthclock.healthclock.ui.activity.member;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;

public class MyWalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("我的钱包");
    }
}
