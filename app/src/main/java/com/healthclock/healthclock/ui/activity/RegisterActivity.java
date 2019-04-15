package com.healthclock.healthclock.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.AppConst;
import com.healthclock.healthclock.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.presenter.LoginRegistPresenter;
import com.healthclock.healthclock.ui.view.RegisterView;
import com.healthclock.healthclock.util.PrefUtils;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.util.UIUtils;
import com.healthclock.healthclock.widget.IconFontTextView;


import butterknife.Bind;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterView, LoginRegistPresenter> implements RegisterView {
    @Bind(R.id.tv_back)
    IconFontTextView tvBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected LoginRegistPresenter createPresenter() {
        return new LoginRegistPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        tvTitle.setText("立即注册");
        tvTitle.setTextColor(UIUtils.getColor(R.color.white));
    }

    @OnClick({R.id.tv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;

        }
    }

    @Override
    public void showProgress(String tipString) {
        showWaitingDialog(tipString);
    }

    @Override
    public void hideProgress() {
        hideWaitingDialog();
    }


    @Override
    public void registerSuccess(LoginRegisterBean user) {
        T.showShort(RegisterActivity.this, "注册成功");
        PrefUtils.setBoolean(RegisterActivity.this, AppConst.IS_LOGIN_KEY, true);
        //PrefUtils.setString(RegisterActivity.this,AppConst.USERNAME_KEY,etName.getText().toString());
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void registerFail() {
        T.showShort(RegisterActivity.this, "注册失败");
    }
}
