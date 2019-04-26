package com.healthclock.healthclock.ui.activity.member;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.guoqi.actionsheet.ActionSheet;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.login.PerfectInformationActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.PopupWindowUtil;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.util.photos.PhotoUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UserInfoActivity extends BaseActivity implements ActionSheet.OnActionSheetSelected, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.ed_nickName)
    EditText edNickName;//昵称
    @BindView(R.id.ed_profession)
    EditText edProfession;//职业
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    private String time;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initUI();
        PhotoUtils.getInstance().init(this, true, new PhotoUtils.OnSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                photoPath(outputFile.getAbsolutePath());
                Glide.with(mContext).load(outputUri).into(ivAvatar);

            }
        });
    }
    public static final String MULTIPART_FORM_DATA = "image/jpg";

    public void photoPath(String path) {
        L.e("path->" + path);
        File file = new File(path);
        //  String token = "97ca6b3aabf14217bebd812f2711cd41";
        RequestBody requestToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
        RequestBody requestFile =               // 根据文件格式封装文件
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        MultipartBody.Part requestImgPart =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        L.e("requestApiKey->" + requestToken);
        L.e("requestImgPart->" + requestImgPart);
        RetrofitUtil.getInstance().UserUploadImg(requestToken, requestImgPart, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                L.e("msg->"+baseResponse.getMsg());
                if (baseResponse.getStatus() == 1) {
                    T.showShort(mContext, baseResponse.getMsg());
                } else {
                    T.showShort(mContext, baseResponse.getMsg());
                }
            }
        });
    }
    private void initUI() {
        setBackView();
        setTitle("个人资料");
    }
    @OnClick({R.id.iv_avatar,R.id.rl_bind_phone,R.id.ll_sex,R.id.ll_birthday,R.id.tv_edit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_avatar:
                ActionSheet.showSheet(UserInfoActivity.this, UserInfoActivity.this, null);
                break;
            case R.id.rl_bind_phone:
                toActivity(EditPhoneActivity.class);
                break;
            case R.id.ll_sex:
                showPopupWindow(ll_sex);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.ll_birthday:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.tv_edit:
                break;
        }
    }


    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;//设置透明度（这是窗体本身的透明度，非背景）
        lp.dimAmount = bgAlpha;//设置灰度
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为 红米米手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 选择性别
     *
     * @param anchorView
     */
    private PopupWindow mPopupWindow;

    private void showPopupWindow(View anchorView) {
        View contentView = getPopupWindowContentView();
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, contentView);
        int xOff = 0; // 可以自己调整偏移
        windowPos[0] -= xOff;
        windowPos[1] -= xOff;
        mPopupWindow.showAsDropDown(anchorView, 40, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        View contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                // showProgress("已举报成功！");
                // ToastUtils.showToast(mContext, "已举报成功！");
                switch (v.getId()) {
                    case R.id.menu_item1:
                        sex = "1";
                        tvSex.setText("男");
                        break;
                    case R.id.menu_item2:
                        sex = "0";
                        tvSex.setText("女");
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

    /**
     * 选中时间
     *
     * @param type
     */
    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                tvBirthday.setText(StringUtil.getDate(date, "yyyy-MM-dd"));
                time = String.valueOf((StringUtil.getStringToDate(StringUtil.getDate(date, "yyyy-MM-dd"), "yyyy-MM-dd")) / 1000);
                L.e("time-->" + time);
            }
        });
        dialog.show();
    }

    String[] takePhotoPerms = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA};
    String[] selectPhotoPerms = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    @Override
    public void onClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.CHOOSE_PICTURE:
                //相册
                checkPermission(selectPhotoPerms, 2);
                break;
            case ActionSheet.TAKE_PICTURE:
                //拍照
                checkPermission(takePhotoPerms, 1);
                break;
            case ActionSheet.CANCEL:
                //取消
                break;
        }
    }

    private void checkPermission(String[] perms, int requestCode) {
        if (EasyPermissions.hasPermissions(this, perms)) {//已经有权限了
            switch (requestCode) {
                case 1:
                    PhotoUtils.getInstance().takePhoto();
                    break;
                case 2:
                    PhotoUtils.getInstance().selectPhoto();
                    break;
            }
        } else {//没有权限去请求
            EasyPermissions.requestPermissions(this, "权限", requestCode, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {//设置成功
        switch (requestCode) {
            case 1:
                PhotoUtils.getInstance().takePhoto();
                break;
            case 2:
                PhotoUtils.getInstance().selectPhoto();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限设置")
                    .setPositiveButton("设置")
                    .setRationale("当前应用缺少必要权限,可能会造成部分功能受影响！请点击\"设置\"-\"权限\"-打开所需权限。最后点击两次后退按钮，即可返回")
                    .build()
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);
    }

}
