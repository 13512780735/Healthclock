package com.healthclock.healthclock.wxapi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.SharedPreferencesUtils;
import com.healthclock.healthclock.widget.CustomDialog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private WXPayEntryActivity mContext;
    private CustomDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        mContext=this;
        String WX_APPID = "wx53ba9da9956a74aa";
        api = WXAPIFactory.createWXAPI(this, WX_APPID);
        api.handleIntent(getIntent(), this);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(getApplicationContext(), "onReq", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            Log.d("TAg", code + "");
            Log.d("TAG", +resp.getType() + "");
            SharedPreferencesUtils.put(mContext,"type","WXPay");
            SharedPreferencesUtils.put(mContext,"code",code);
            if (code == 0) {
                new AlertDialog.Builder(this).setMessage("支付订单成功！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  EventBus.getDefault().post(new PayEventMessage("1"));
                        dialog.dismiss();
                        onBackPressed();
                    }
                }).setTitle("微信支付结果").setCancelable(false).show();

            } else if (code == -2) {
                new AlertDialog.Builder(this).setMessage("取消支付").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  EventBus.getDefault().post(new PayEventMessage("2"));
                        dialog.dismiss();

                        onBackPressed();
                    }
                }).setTitle("微信支付结果").setCancelable(false).show();

            } else {
                new AlertDialog.Builder(this).setMessage("交易出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  EventBus.getDefault().post(new PayEventMessage("2"));
                        dialog.dismiss();
                        onBackPressed();
                    }
                }).setTitle("微信支付结果").setCancelable(false).show();
            }

        }

    }
    /**
     * 显示字符串消息
     *
     * @param message
     */
    public void showProgress(String message) {
        // dialog = new CustomDialog(getActivity());
        dialog = new CustomDialog(this).builder()
                .setGravity(Gravity.CENTER).setTitle("提示", getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                .setSubTitle(message);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }
}
