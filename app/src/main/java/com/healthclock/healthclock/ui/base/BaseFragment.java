package com.healthclock.healthclock.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.widget.IconFontTextView;

import butterknife.ButterKnife;

/**
 * user：lqm
 * desc：BaseFragment
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;
    private View rootView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        //判断是否使用MVP模式
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);//因为之后所有的子类都要实现对应的View接口
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(provideContentViewId(), container, false);
        ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public void init() {

    }

    public void initView(View rootView) {
    }

    public void initData() {

    }

    public void initListener() {

    }


    /**
     * 设置显示右侧返回按钮
     */
    public void setBackView() {
        IconFontTextView backView = findViewById(R.id.tv_back);
        if (backView == null) {
            return;
        }
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
    /**
     * 设置显示标题
     *
     * @param txt
     */
    public void setTitle(String txt) {
        TextView title =findViewById(R.id.tv_title);
        if (title == null) {
            return;
        }
        title.setVisibility(View.VISIBLE);
        title.setText(txt);
    }
    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }
    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return rootView;
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract T createPresenter();

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    protected void toFinish() {
        getActivity().finish();
    }

    public void toActivityFinish(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
        toFinish();
    }

    public void toActivity(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
    }

    public void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
