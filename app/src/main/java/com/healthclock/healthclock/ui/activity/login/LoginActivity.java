package com.healthclock.healthclock.ui.activity.login;


import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.user.LoginRegisterBean;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.MainActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.PermissionUtil;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.widget.BorderTextView;

import java.security.acl.Permission;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;


public class LoginActivity extends BaseActivity {
    public static final String TAG = "HL";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        addListeners();
    }

    private final int REQUEST_CONTACT = 50;

    private void initUI() {
        phone = SharedPreferencesUtils.getString(this, "phone");
        pwd = SharedPreferencesUtils.getString(this, "pwd");
        if (!StringUtil.isBlank(phone) && !StringUtil.isBlank(pwd)) {
            tvLogin.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
            tvLogin.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
            tvLogin.setEnabled(true);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
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


    @OnClick({R.id.tv_login, R.id.tv_register_account, R.id.tv_forget_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                L.e("点击了");
                phone = etPhone.getText().toString().trim();
                pwd = etPwd.getText().toString().trim();
                login(phone, pwd, "0");
                break;
            case R.id.tv_register_account:
                toActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                //toActivity(ForgetPwdActivity.class);
                toActivity(EditInformationActivity.class);
                break;
        }
    }

    private void login(final String phone, final String pwd, String openid) {
        RetrofitUtil.getInstance().getUserLogin(phone, pwd, openid, new Subscriber<BaseResponse<LoginRegisterBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                L.e("错误：" + e + "");
            }

            @Override
            public void onNext(BaseResponse<LoginRegisterBean> baseResponse) {
                // L.json(3, TAG, baseResponse.toString());
                L.e(baseResponse.msg);
                if (baseResponse.getStatus() == 1) {
                    SharedPreferencesUtils.put(mContext, "phone", phone);
                    SharedPreferencesUtils.put(mContext, "pwd", pwd);
                    SharedPreferencesUtils.put(mContext, "token", baseResponse.getData().getToken());
                    toActivity(MainActivity.class);
                }
            }
        });
    }


}
