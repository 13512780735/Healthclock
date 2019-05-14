package com.healthclock.healthclock.ui.fragment.main;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.clock.activity.MyAlarmClockActivity;
import com.healthclock.healthclock.network.FalseModel;
import com.healthclock.healthclock.ui.activity.main.CustomActivity;
import com.healthclock.healthclock.ui.activity.main.MyVideoActivity;
import com.healthclock.healthclock.ui.adapter.NewsAdapter;
import com.healthclock.healthclock.ui.base.BaseFragment;
import com.healthclock.healthclock.util.PopupWindowUtil;
import com.healthclock.healthclock.widget.IconFontTextView;
import com.king.zxing.Intents;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmClockFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tv_right)
    IconFontTextView tvRight;
    @BindView(R.id.banner)
    ConvenientBanner mBanner;
  //  @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_QR_CODE = "key_code";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    public static final int REQUEST_CODE_SCAN = 0X01;
    public static final int REQUEST_CODE_PHOTO = 0X02;

    public static final int RC_CAMERA = 0X01;

    public static final int RC_READ_PHOTO = 0X02;
    private Class<?> cls;
    private String title;
    private boolean isContinuousScan;

    private List<String> networkImage = new ArrayList<>();
    String[] images = {
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://img2.3lian.com/2014/f2/37/d/41.jpg",
    };

    ArrayList<FalseModel> data;
    private NewsAdapter mAdapter;

    public static AlarmClockFragment newInstance() {
        return new AlarmClockFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_alarm_clock;
    }

    @Override
    protected void lazyLoad() {
        initData();
        initUI();
    }

    private void initUI() {
        mRecyclerView=findView(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new NewsAdapter(R.layout.news_items, data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mBanner.startTurning(4000);
        networkImage = Arrays.asList(images);

        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImage).setPageIndicator(new int[]{R.drawable.indicator_gray, R.drawable.indicator_red}).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL).setScrollDuration(1500);

//        convenientBanner.setManualPageable(false);//设置不能手动影响

        //网络加载例子


    }

    @Override
    public void onPause() {
        super.onPause();
        mBanner.stopTurning();
    }

    public void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FalseModel falseModel = new FalseModel();
            falseModel.setTitle("" + i);
            data.add(falseModel);
        }
    }

    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(getActivity()).load(data).into(imageView);
        }
    }

    @OnClick({R.id.tv_scan, R.id.tv_right})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_scan:
                isContinuousScan = false;
                this.cls = CustomActivity.class;
                this.title = ((IconFontTextView) v).getText().toString();
                checkCameraPermissions();

                break;
            case R.id.tv_right:
                showPopupWindow(tvRight);
                setBackgroundAlpha(0.7f);
                break;
        }
    }


    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;//设置透明度（这是窗体本身的透明度，非背景）
        lp.dimAmount = bgAlpha;//设置灰度
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为 红米米手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

    private PopupWindow mPopupWindow;

    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_set_layout;   // 布局ID
        View contentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                // showProgress("已举报成功！");
                //ToastUtils.showToast(mContext, "已举报成功！");
                switch (v.getId()) {
                    case R.id.menu_item1:
                        toActivity(MyAlarmClockActivity.class);
                        break;
                    case R.id.menu_item2:
                        toActivity(MyVideoActivity.class);
                        break;
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    setBackgroundAlpha(1f);
                }
            }
        };
        contentView.findViewById(R.id.menu_item1).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_item2).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private void showPopupWindow(View anchorView) {
        View contentView = getPopupWindowContentView();
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    /**
     * 扫码
     *
     * @param cls
     * @param title
     */
    private void startScan(Class<?> cls, String title) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(getActivity(), R.anim.in, R.anim.out);
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_IS_CONTINUOUS, isContinuousScan);
        ActivityCompat.startActivityForResult(getActivity(), intent, REQUEST_CODE_SCAN, optionsCompat.toBundle());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_CODE_PHOTO:
                    //  parsePhoto(data);
                    break;
            }

        }
    }

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {//有权限
            startScan(cls, title);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
