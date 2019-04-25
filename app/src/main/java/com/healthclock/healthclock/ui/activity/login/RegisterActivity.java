package com.healthclock.healthclock.ui.activity.login;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.MainActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.AppManager;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class RegisterActivity extends BaseActivity {

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

    @OnClick({R.id.send_code_btn, R.id.tv_register, R.id.protocol_tv01})
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
                toRegist(phone, code, pwd, referralCode);
                break;
            case R.id.protocol_tv:
                toActivity(RegisterProtocolActivity.class);
                break;
        }
    }

    private void toRegist(final String phone, String code, final String pwd, String referralCode) {
        RetrofitUtil.getInstance().getUserRegister(phone, code, pwd, referralCode, new Subscriber<BaseResponse<LoginRegisterBean>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<LoginRegisterBean> loginRegisterBeanBaseResponse) {
                SharedPreferencesUtils.put(mContext, "phone", phone);
                SharedPreferencesUtils.put(mContext, "pwd", pwd);
                toActivityFinish(MainActivity.class);
                AppManager.getAppManager().finishAllActivity();
            }
        });
    }

    private void sendCode() {

    }


}
