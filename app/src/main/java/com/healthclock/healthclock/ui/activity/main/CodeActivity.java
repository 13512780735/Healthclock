package com.healthclock.healthclock.ui.activity.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.fragment.main.MemberFragment;
import com.king.zxing.util.CodeUtils;

public class CodeActivity extends BaseActivity {
    private ImageView ivCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ivCode = (ImageView)findViewById(R.id.ivCode);
        boolean isQRCode = getIntent().getBooleanExtra(MemberFragment.KEY_IS_QR_CODE,false);
        if(isQRCode){
            createQRCode(getString(R.string.app_name));
        }else{
            createBarCode("1234567890");
        }
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("我的二维码");

    }
    /**
     * 生成二维码
     * @param content
     */
    private void createQRCode(String content){
        //生成二维码最好放子线程生成防止阻塞UI，这里只是演示
        Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Bitmap bitmap =  CodeUtils.createQRCode(content,600,logo);
        //显示二维码
        ivCode.setImageBitmap(bitmap);
    }

    /**
     * 生成条形码
     * @param content
     */
    private void createBarCode(String content){
        //生成条形码最好放子线程生成防止阻塞UI，这里只是演示
//        Bitmap bitmap = CodeUtils.createBarCode(content, BarcodeFormat.CODE_128,800,200,null,true);
//        //显示条形码
//        ivCode.setImageBitmap(bitmap);
    }

}
