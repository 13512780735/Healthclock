package com.healthclock.healthclock.ui.activity.login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterProtocolActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("注册协议");
        mWebView.loadUrl("file:///android_asset/Registeragreement.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LoaddingShow();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LoaddingDismiss();
                super.onPageFinished(view, url);
            }
        });
    }

    @OnClick({R.id.tv_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                finish();
                break;
        }
    }
}
