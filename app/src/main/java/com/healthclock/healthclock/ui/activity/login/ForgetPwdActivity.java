package com.healthclock.healthclock.ui.activity.login;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.forget_et_phone)
    EditText etPhone;//手机号
    @BindView(R.id.forget_et_code)
    EditText etCode;//验证码
    @BindView(R.id.forget_et_newPwd)
    EditText etPwd;//新密码
    @BindView(R.id.forget_et_newPwd01)
    EditText etPwd01;//邀请码
    private String phone, pwd, code, pwd01;

    @BindView(R.id.send_code_btn)
    BorderTextView sendCode;
    @BindView(R.id.tv_confirm)
    BorderTextView tvConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initUI();
    }

    private void initUI() {
        setTitle("找回密码");
        setBackView();
        EditTextSizeCheckUtil.textChangeListener textChangeListener = new EditTextSizeCheckUtil.textChangeListener(tvConfirm);
        textChangeListener.addAllEditText(etPhone, etPwd, etCode, etPwd01);
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
                pwd01 = etPwd01.getText().toString().trim();
                if (!pwd01.equals(pwd)) {
                    T.showShort(mContext, "两次密码不一样，请重新输入");
                    etPwd.setText("");
                    etPwd01.setText("");
                    return;
                }
                toFindPwd(phone, code, pwd);
                break;
        }
    }

    private void toFindPwd(final String phone, String code, final String pwd) {
        L.e("点击了");
        RetrofitUtil.getInstance().getUserForgetPwd(phone, code, pwd, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> emptyEntityBaseResponse) {
                SharedPreferencesUtils.put(mContext, "phone", phone);
                SharedPreferencesUtils.put(mContext, "pwd", pwd);
                toActivityFinish(LoginActivity.class);
            }
        });
    }

    private void sendCode() {

    }


}
