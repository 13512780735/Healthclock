package com.healthclock.healthclock.ui.activity.member;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPhoneActivity extends BaseActivity {
    @BindView(R.id.forget_et_phone)
    EditText etPhone;//手机号
    @BindView(R.id.forget_et_code)
    EditText etCode;//验证码
    @BindView(R.id.forget_et_newPwd)
    EditText etPwd;//登录密码

    @BindView(R.id.send_code_btn)
    BorderTextView sendCode;
    @BindView(R.id.tv_confirm)
    BorderTextView tvConfirm;
    private String phone;
    private String pwd;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("修改手机");
        EditTextSizeCheckUtil.textChangeListener textChangeListener = new EditTextSizeCheckUtil.textChangeListener(tvConfirm);
        textChangeListener.addAllEditText(etPhone, etPwd, etCode);
        EditTextSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    tvConfirm.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    tvConfirm.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    tvConfirm.setEnabled(true);
                } else {
                    tvConfirm.setContentColorResource01(Color.parseColor("#999999"));
                    tvConfirm.setStrokeColor01(Color.parseColor("#999999"));
                    tvConfirm.setEnabled(false);
                }
            }
        });
    }
    @OnClick({R.id.send_code_btn, R.id.tv_confirm})
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
            case R.id.tv_confirm:
                phone = etPhone.getText().toString().trim();
                pwd = etPwd.getText().toString().trim();
                code = etCode.getText().toString().trim();
                //toFindPwd(phone, code, pwd);
                break;
        }
    }
    private void sendCode() {

    }
}
