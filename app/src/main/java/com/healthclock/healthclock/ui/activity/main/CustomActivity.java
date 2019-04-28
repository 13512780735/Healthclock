package com.healthclock.healthclock.ui.activity.main;

import android.hardware.Camera;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.ui.fragment.main.AlarmClockFragment;
import com.king.zxing.CaptureHelper;
import com.king.zxing.Intents;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;
import com.king.zxing.camera.CameraManager;

public class CustomActivity extends BaseActivity implements OnCaptureCallback {

    private boolean isContinuousScan;

    private CaptureHelper mCaptureHelper;

    private SurfaceView surfaceView;

    private ViewfinderView viewfinderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("二维码");
        surfaceView = findViewById(R.id.surfaceView);
        viewfinderView = findViewById(R.id.viewfinderView);

        isContinuousScan = getIntent().getBooleanExtra(AlarmClockFragment.KEY_IS_CONTINUOUS,false);

        mCaptureHelper = new CaptureHelper(this,surfaceView,viewfinderView);
        mCaptureHelper.onCreate();
        mCaptureHelper.vibrate(true)
                .continuousScan(isContinuousScan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 关闭闪光灯（手电筒）
     */
    private void offFlash(){
        Camera camera = mCaptureHelper.getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    /**
     * 开启闪光灯（手电筒）
     */
    public void openFlash(){
        Camera camera = mCaptureHelper.getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }


    /**
     * 扫码结果回调
     * @param result 扫码结果
     * @return
     */
    @Override
    public boolean onResultCallback(String result) {
        if(isContinuousScan){
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void clickFlash(View v){
        if(v.isSelected()){
            offFlash();
            v.setSelected(false);
        }else{
            openFlash();
            v.setSelected(true);
        }

    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.ivFlash:
                clickFlash(v);
                break;
        }
    }
}