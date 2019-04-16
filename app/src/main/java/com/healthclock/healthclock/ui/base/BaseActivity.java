package com.healthclock.healthclock.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.app.App;
import com.healthclock.healthclock.widget.CustomDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;


public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AutoLayoutActivity {


    protected T mPresenter;
    private CustomDialog mDialogWaiting;
    public Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.activities.add(this);
        mContext = this;
        init();

        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }

        //子类不再需要设置布局ID，也不再需要使用ButterKnife.bind()
        // setContentView(provideContentViewId());
       // ButterKnife.bind(this);

        excuteStatesBar();

        initListener();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }


    /**
     * 解决4.4设置状态栏颜色之后，布局内容嵌入状态栏位置问题
     */
    private void excuteStatesBar() {
        ViewGroup mContentView = (ViewGroup) getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows,
            // 而是设置 ContentView 的第一个子 View ，预留出系统 View 的空间.
            mChildView.setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    //在setContentView()调用之前调用，可以设置WindowFeature(如：this.requestWindowFeature(Window.FEATURE_NO_TITLE);)
    public void init() {
        //底部导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.TRANSLUCENT));
            //底部导航栏
            getWindow().setNavigationBarColor(this.getResources().getColor(R.color.TRANSLUCENT));


        }

    }

//    public void initView() {
//    }
//
//    public void initData() {
//    }

    public void initListener() {
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    //  protected abstract int  provideContentViewId();

    /**
     * 显示等待提示框
     */
    public Dialog showWaitingDialog(String tip) {
        hideWaitingDialog();
        View view = View.inflate(this, R.layout.dialog_waiting, null);
        if (!TextUtils.isEmpty(tip))
            ((TextView) view.findViewById(R.id.tvTip)).setText(tip);
        mDialogWaiting = new CustomDialog(this, view, R.style.MyDialog);
        mDialogWaiting.show();
        mDialogWaiting.setCancelable(false);
        return mDialogWaiting;
    }

    /**
     * 隐藏等待提示框
     */
    public void hideWaitingDialog() {
        if (mDialogWaiting != null) {
            mDialogWaiting.dismiss();
            mDialogWaiting = null;
        }
    }
    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    /**
     * 文本View
     */
    public TextView textView(int textview) {
        return (TextView) findViewById(textview);
    }

    /**
     * 文本button
     */
    public Button button(int id) {
        return (Button) findViewById(id);
    }

    /**
     * 文本button
     */
    public ImageView imageView(int id) {
        return (ImageView) findViewById(id);
    }

    /**
     * 文本editText
     */
    public EditText editText(int id) {
        return (EditText) findViewById(id);
    }
    protected void toFinish() {
        finish();
    }

    public void toActivityFinish(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
        toFinish();
    }

    public void toActivity(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
    }

    public void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(mContext, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
