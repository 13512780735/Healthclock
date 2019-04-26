package com.healthclock.healthclock.ui.activity.member;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.guoqi.actionsheet.ActionSheet;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.photos.PhotoUtils;
import com.healthclock.healthclock.widget.BorderTextView;
import com.healthclock.healthclock.widget.custom.GridViewAddImgesAdpter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FeedbackActivity extends BaseActivity implements ActionSheet.OnActionSheetSelected, EasyPermissions.PermissionCallbacks{
    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.mGridView)
    GridView mGridView;
    @BindView(R.id.tv_confirm)
    BorderTextView mTvConfirm;


    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    /**
     * 图片
     */
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源
    private List<Map<String, Object>> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initUI();
        PhotoUtils.getInstance().init(this, true, new PhotoUtils.OnSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                photoPath(outputFile.getAbsolutePath());
            }
        });
    }

    private void initUI() {
        setBackView();
        setTitle("意见反馈");
        /**
         * 图片
         */
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        mGridView.setAdapter(gridViewAddImgesAdpter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ActionSheet.showSheet(FeedbackActivity.this, FeedbackActivity.this, null);
            }
        });
    }

    public void photoPath(String path) {
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        //upload(path);
        datas.add(map);
        gridViewAddImgesAdpter.notifyDataSetChanged();
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
