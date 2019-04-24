package com.healthclock.healthclock.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.presenter.RegistPresenter;
import com.healthclock.healthclock.ui.view.RegisterView;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;


import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity<RegisterView, RegistPresenter> implements RegisterView {

    @BindView(R.id.register_et_phone)
    EditText etPhone;//手机号
    @BindView(R.id.register_et_code)
    EditText etCode;//验证码
    @BindView(R.id.register_et_pwd)
    EditText etPwd;//密码
    @BindView(R.id.register_et_referralCode)
    EditText etReferralCode;//邀请码
    private String phone, pwd, code, referralCode;

    @BindView(R.id.send_code_btn)
    BorderTextView sendCode;
    @BindView(R.id.tv_register)
    BorderTextView tvRegister;
    @BindView(R.id.checkbox01)
    CheckBox checkBox;

    @Override
    protected RegistPresenter createPresenter() {
        return new RegistPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }


    public void initView() {
        setTitle("立即注册");
        setBackView();
        EditTextSizeCheckUtil.textChangeListener textChangeListener = new EditTextSizeCheckUtil.textChangeListener(tvRegister);
        textChangeListener.addAllEditText(etPhone, etPwd, etCode);
        EditTextSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    tvRegister.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    tvRegister.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    tvRegister.setEnabled(true);
                } else {
                    tvRegister.setContentColorResource01(Color.parseColor("#999999"));
                    tvRegister.setStrokeColor01(Color.parseColor("#999999"));
                    tvRegister.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.send_code_btn, R.id.tv_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code_btn:
                phone = etPhone.getText().toString().trim();
                if (StringUtil.isBlank(phone)) {
                    T.showShort(mContext, "手机号不能为空");
                    return;
                }
                sendCode();
                break;
            case R.id.tv_register:
                if (!checkBox.isChecked()) {
                    showProgress("請同意條款");
                    return;
                }
                phone = etPhone.getText().toString().trim();
                pwd = etPwd.getText().toString().trim();
                code = etCode.getText().toString().trim();
                referralCode = etReferralCode.getText().toString().trim();
                mPresenter.toRegist(phone, code, pwd, referralCode);
                break;
        }
    }

    private void sendCode() {

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
        // T.showShort(RegisterActivity.this, user.);
        SharedPreferencesUtils.put(RegisterActivity.this, "token", user.getToken());
        SharedPreferencesUtils.put(mContext, "phone", phone);
        SharedPreferencesUtils.put(mContext, "pwd", pwd);
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void getDataError(String message) {
        T.showShort(RegisterActivity.this, message);
    }

    @Override
    public void registerFail() {
        hideProgress();
        T.showShort(RegisterActivity.this, "注册失败");
    }
}
