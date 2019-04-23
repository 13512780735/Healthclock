package com.healthclock.healthclock.ui.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.presenter.LoginPresenter;
import com.healthclock.healthclock.ui.view.LoginView;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {
    @BindView(R.id.login_et_phone)
    EditText etPhone;
    @BindView(R.id.login_et_pwd)
    EditText etPwd;
    @BindView(R.id.tb_re_pwd)
    ToggleButton tb_password;
    @BindView(R.id.tv_login)
    BorderTextView tvLogin;
    private String phone, pwd;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initView();
        addListeners();
    }


    private void initUI() {
        phone = SharedPreferencesUtils.getString(this, "phone");
        pwd = SharedPreferencesUtils.getString(this, "pwd");
        if (!StringUtil.isBlank(phone) && !StringUtil.isBlank(pwd)) {
            tvLogin.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
            tvLogin.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
            tvLogin.setEnabled(true);
        }


    }

    public void initView() {
        etPhone.setText(phone);
        etPwd.setText(pwd);
        EditTextSizeCheckUtil.textChangeListener textChangeListener = new EditTextSizeCheckUtil.textChangeListener(tvLogin);
        textChangeListener.addAllEditText(etPhone, etPwd);
        EditTextSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    tvLogin.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    tvLogin.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    tvLogin.setEnabled(true);
                } else {
                    tvLogin.setContentColorResource01(Color.parseColor("#999999"));
                    tvLogin.setStrokeColor01(Color.parseColor("#999999"));
                    tvLogin.setEnabled(false);
                }
            }
        });
    }

    private void addListeners() {
        tb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //普通文本框
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码框
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPwd.postInvalidate();//刷新View
                //将光标置于文字末尾
                CharSequence charSequence = etPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
    }

    @Override
    public void showProgress(String tipString) {
        showWaitingDialog(tipString);
    }

    @Override
    public void hideProgress() {
        hideWaitingDialog();
    }


    @OnClick({R.id.tv_login, R.id.tv_register_account, R.id.tv_forget_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                phone = etPhone.getText().toString().trim();
                pwd = etPwd.getText().toString().trim();
                mPresenter.toLogin(phone, pwd, "0");
                break;
            case R.id.tv_register_account:
                toActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                break;
        }
    }

    @Override
    public void loginSuccess(LoginRegisterBean user) {
        SharedPreferencesUtils.put(mContext, "phone", phone);
        SharedPreferencesUtils.put(mContext, "pwd", pwd);
        SharedPreferencesUtils.put(mContext, "token", user.getToken());
        toActivity(MainActivity.class);
    }

    @Override
    public void loginFail() {
        hideProgress();
        T.showShort(mContext, "登录失败");
    }
}
