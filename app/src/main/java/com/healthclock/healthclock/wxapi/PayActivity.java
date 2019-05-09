package com.healthclock.healthclock.wxapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.alipay.sdk.app.PayTask;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.wxapi.alipay.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;
import rx.Subscriber;

public class PayActivity extends BaseActivity {
    private IWXAPI api;
    private String WX_APPID;
    private String pay_type;


    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
//                    PayResult  payResult = new PayResult ("1",(String) msg.obj);
//                    /**
//                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
//                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
//                     * docType=1) 建议商户依赖异步通知
//                     */
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                    if (TextUtils.equals(resultStatus, "9000")) {
//
//
//                    } else {
//                        // 判断resultStatus 为非"9000"则代表可能支付失败
//                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
//                            Log.d("TAG", "resultStatus-->" + resultStatus);
//
//                        }
//                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setTitle("收银台");
        setBackView();
        WX_APPID = "";//微信id
        api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
        api.registerApp(WX_APPID);
    }
    @OnClick({R.id.rl_pay_weixin, R.id.rl_pay_zhifubao})
    public void onClick(View v) {
        switch (v.getId()) {
            //微信支付
            case R.id.rl_pay_weixin:
                pay_type = "wxpay";
                wechatPay();
                break;
//            //支付宝支付
            case R.id.rl_pay_zhifubao:
                pay_type = "alipay";
                alipayPay();
                break;

        }
    }
    /**
     * 微信支付
     */
    private void wechatPay() {
//        loaddingDialog.show();
//        final String sign = SignUtils.getSign(mContext);
//        String signs[] = sign.split("##");
//        String signature = signs[0];
//        String newtime = signs[1];
//        String random = signs[2];
//        RetrofitUtil.getInstance().appWechat(token, signature, newtime, random, tid, money, new Subscriber<BaseResponse<WechatPayModel>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                loaddingDialog.dismiss();
//            }
//
//            @Override
//            public void onNext(BaseResponse<WechatPayModel> baseResponse) {
//                loaddingDialog.dismiss();
//                if (baseResponse.code == 200) {
//                    String data = baseResponse.getData().getAppwechat();
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        String appId = object.optString("appid");
//                        String partnerId = object.optString("mch_id");
//                        String prepayId = object.optString("prepay_id");
//                        String nonceStr = object.optString("nonce_str");
//                        String packageValue = "Sign=Wxpay";
//                        long timeMills = System.currentTimeMillis() / 1000;
//                        String timeStamp = String.valueOf(timeMills);
//                        String stringA =
//                                "appid=" + appId
//                                        + "&noncestr=" + nonceStr
//                                        + "&package=" + packageValue
//                                        + "&partnerid=" + partnerId
//                                        + "&prepayid=" + prepayId
//                                        + "&timestamp=" + timeStamp;
//                        String key = "dahgdrh678fdh4sdhtui527gjsdtasaa";
//                        String stringSignTemp = stringA + "&key=" + key;
//                        String sign = MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
//                        LogUtils.d("TAG" + "WX_APPID-->" + WX_APPID + "appId-->" + appId + "partnerId-->" + partnerId + "prepayId-->" + prepayId + "nonceStr-->" + nonceStr + "packageValue-->" + packageValue);
//                        sendPayred(appId, partnerId, prepayId, nonceStr, packageValue, sign, timeStamp);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    showProgress(baseResponse.getMsg());
//                }
//            }
//        });
    }

    /**
     * 支付宝
     */
    private void alipayPay() {
        loaddingDialog.show();
//        RetrofitUtil.getInstance().appAlipay(token, signature, newtime, random, tid, money, new Subscriber<BaseResponse<AlipayPayAModel>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                loaddingDialog.dismiss();
//            }
//
//            @Override
//            public void onNext(BaseResponse<AlipayPayAModel> baseResponse) {
//                loaddingDialog.dismiss();
//                if (baseResponse.code == 200) {
//                    String data = baseResponse.getData().getAppalipay();
//                    alipay(data);
//                } else {
//                    showProgress(baseResponse.getMsg());
//                }
//            }
//        });
    }


    private void alipay(String data) {

        final String payInfo = data;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
//                PayTask alipay = new PayTask(PayActivity.this);
//                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo, true);
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }


    private void sendPayred(String appId, String partnerId, String prepayId, String nonceStr, String packageValue, String sign, String timeStamp) {
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.nonceStr = nonceStr;
        request.packageValue = packageValue;
        request.sign = sign;
        request.timeStamp = timeStamp;
        api.sendReq(request);
    }
}
