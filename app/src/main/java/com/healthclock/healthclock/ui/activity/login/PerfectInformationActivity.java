package com.healthclock.healthclock.ui.activity.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.activity.MainActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.AppManager;
import com.healthclock.healthclock.util.L;
import com.healthclock.healthclock.util.PopupWindowUtil;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.photo.FileUtils;
import com.healthclock.healthclock.util.photo.callback.OnItemClickListener;
import com.healthclock.healthclock.util.photo.callback.PhotoCallBack;
import com.healthclock.healthclock.util.photo.view.AlertView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.CAMERA;

public class PerfectInformationActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    private String time;
    private String sex;

    /**
     * 拍照相册
     */
    public PhotoCallBack callBack;
    public String path = "";
    public Uri photoUri;
    private File file;

    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_information);
        sex = "1";
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("完善健康信息");
        setRightText("跳过", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivityFinish(MainActivity.class);
                AppManager.getAppManager().finishAllActivity();
            }
        });
    }

    @OnClick({R.id.ll_birthday, R.id.ll_sex, R.id.iv_avatar})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                changeAvater();
                break;
            case R.id.ll_sex:
                showPopupWindow(ll_sex);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.ll_birthday:
                showDatePickDialog(DateType.TYPE_YMD);
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


    private void changeAvater() {
        callBack = new PhotoCallBack() {
            @Override
            public void doSuccess(String path) {
                //将图片传给服务器
//                File file = new File(path);
//                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                MultipartBody.Builder builder = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)//表单类型
//                        .addFormDataPart("id", SaveUserInfo.getUid());
//                builder.addFormDataPart("head_portrait", file.getName(), imageBody);
//                List<MultipartBody.Part> partList=builder.build().parts();
//                mPresenter.startUpdateHeadRequest(partList,SaveUserInfo.getUid());
            }

            @Override
            public void doError() {

            }
        };
        comfireImgSelection(getApplicationContext(), ivAvatar);
    }

    String[] takePhotoPerms = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA};

    // 拍照
    public void comfireImgSelection(Context context, CircleImageView my_info) {
        ivAvatar = my_info;
        new AlertView(null, null, "取消", null, new String[]{"从手机相册选择", "拍照"}, this, AlertView.Style.ActionSheet,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            // 从相册中选择
                            checkPermission(takePhotoPerms, 2);
                        } else if (position == 1) {
                            // 拍照
                            checkPermission(takePhotoPerms, 1);
                        }
                    }
                }
        ).show();
    }

    private void checkPermission(String[] perms, int requestCode) {
        if (EasyPermissions.hasPermissions(this, perms)) {//已经有权限了
            switch (requestCode) {
                case 1:
                    photo();
                    break;
                case 2:
                    Intent i = new Intent(
                            // 相册
                            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
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
                photo();
                break;
            case 2:
                Intent i = new Intent(
                        // 相册
                        Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
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


    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.e("checkPermission", "PERMISSION_GRANTED" + ContextCompat.checkSelfPermission(this, permission));
                return true;
            } else {
                Log.e("checkPermission", "PERMISSION_DENIED" + ContextCompat.checkSelfPermission(this, permission));
                return false;
            }
        } else {
            Log.e("checkPermission", "M以下" + ContextCompat.checkSelfPermission(this, permission));
            return true;
        }
    }

    public void photo() {

        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
            file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                // 有sd卡，是否有myImage文件夹
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 是否有headImg文件
                long l = System.currentTimeMillis();
                file = new File(sdcardPathDir + l + ".JPEG");
            }
            if (file != null) {
                path = file.getPath();

                photoUri = Uri.fromFile(file);
                if (Build.VERSION.SDK_INT >= 24) {
                    photoUri = FileProvider.getUriForFile(this, "com.barnettwong.changeavaterview.fileProvider", file);
                } else {
                    photoUri = Uri.fromFile(file);
                }
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (file != null && file.exists())
                    startPhotoZoom(photoUri);
                break;
            case RESULT_LOAD_IMAGE:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case CUT_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    if (path != null && path.length() != 0) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //给头像设置图片源
                        ivAvatar.setImageBitmap(bitmap);
                        if (callBack != null)
                            callBack.doSuccess(path);
                    }
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        try {
            // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String address = sDateFormat.format(new Date());
            if (!FileUtils.isFileExist("")) {
                FileUtils.createSDDir("");
            }

            Uri imageUri = Uri.parse("file:///sdcard/formats/" + address + ".JPEG");
            final Intent intent = new Intent("com.android.camera.action.CROP");

            // 照片URL地址
            intent.setDataAndType(uri, "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 480);
            // 输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // 不启用人脸识别
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("return-data", false);
            intent.putExtra("fileurl", FileUtils.SDPATH + address + ".JPEG");
            path = FileUtils.SDPATH + address + ".JPEG";
            startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
