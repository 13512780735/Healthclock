package com.healthclock.healthclock.ui.activity.member;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.github.iron.library.linkage.LinkageDialog;
import com.github.iron.library.linkage.LinkageItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.healthclock.healthclock.R;
import com.healthclock.healthclock.listener.IEditTextChangeListener;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.indent.ProvincesModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.activity.login.LoginActivity;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.AppManager;
import com.healthclock.healthclock.util.EditTextSizeCheckUtil;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.util.T;
import com.healthclock.healthclock.widget.BorderTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class EditAddressActivity extends BaseActivity {

    @BindView(R.id.ed_recipients_name)
    EditText mEdRecipientsName;
    @BindView(R.id.ed_recipients_phone)
    EditText mEdRecipientsPhone;
    @BindView(R.id.tv_recipients_address)
    TextView mTvRecipientsAddress;
    @BindView(R.id.ll_recipients_address)
    LinearLayout mLlRecipientsAddress;
    @BindView(R.id.ed_recipients_detailed_address)
    EditText mEdRecipientsDetailedAddress;
    @BindView(R.id.tv_save)
    BorderTextView mTvSave;
    @BindView(R.id.ll_set_default)
    LinearLayout ll_set_default;
    @BindView(R.id.sw_onOff)
    Switch sw_onOff;
    private int flag, position;
    private String id, address, realname, mobile, province, city, area;
    private LinkageDialog dialog;
    private List<LinkageItem> cityList;
    private boolean isdefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        flag = getIntent().getExtras().getInt("flag");  //0  新建  1 编辑
        id = getIntent().getExtras().getString("id");
        address = getIntent().getExtras().getString("address");
        realname = getIntent().getExtras().getString("realname");
        mobile = getIntent().getExtras().getString("mobile");
        province = getIntent().getExtras().getString("province");
        city = getIntent().getExtras().getString("city");
        area = getIntent().getExtras().getString("area");
        isdefault = getIntent().getExtras().getBoolean("isdefault");


    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
        initCityBtn();
    }

    private void initCityBtn() {
        //创建对话框
        final LinkageDialog dialog = new LinkageDialog.Builder(EditAddressActivity.this, 3).setLinkageData(getCityList())
                .setOnLinkageSelectListener(new LinkageDialog.IOnLinkageSelectListener() {
                    @Override
                    public void onLinkageSelect(LinkageItem... items) {
                        toastLinkageItem(items);
                    }
                }).build();

        //按钮点击
        findViewById(R.id.ll_recipients_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    //
//    private void initData() {
//        String json = getJson("city1.json");
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<ProvincesModel>>() {
//        }.getType();
//        List<ProvincesModel> citys = gson.fromJson(json, type);
//        List<LinkageItem> cityList = new ArrayList<>();
//        cityList.addAll(citys);
//        // return cityList;
//    }

    //
    public String getJson(String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //
    private void initUI() {
        setBackView();
        //  setTitle("添加地址");
        if (flag == 0) {
            setTitle("添加地址");
            ll_set_default.setVisibility(View.GONE);
        } else if (flag == 1) {
            setTitle("编辑地址");
            ll_set_default.setVisibility(View.VISIBLE);
            setRightText("删除", 14,new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAddress();
                }
            });
            mTvSave.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
            mTvSave.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
            mTvSave.setEnabled(true);
        }
        if(isdefault){
            sw_onOff.setChecked(true);
        }else {
            sw_onOff.setChecked(false);
        }
        mEdRecipientsName.setText(realname);
        mEdRecipientsPhone.setText(mobile);
        mTvRecipientsAddress.setText(province + city + area);
        mEdRecipientsDetailedAddress.setText(address);
        EditTextSizeCheckUtil.textChangeListener textChangeListener = new EditTextSizeCheckUtil.textChangeListener(mTvSave);
        textChangeListener.addAllEditText(mEdRecipientsPhone, mEdRecipientsName, mEdRecipientsDetailedAddress);
        EditTextSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    // mTvSave.setBackgroundResource(R.drawable.shape_round_blue_bg_5);
                    mTvSave.setContentColorResource01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    mTvSave.setStrokeColor01(StringUtil.getColor(mContext, R.styleable.Theme_title_text_color));
                    mTvSave.setEnabled(true);
                } else {
                    //  mTvSave.setBackgroundResource(R.drawable.shape_round_grey_bg_5);
                    mTvSave.setContentColorResource01(Color.parseColor("#999999"));
                    mTvSave.setStrokeColor01(Color.parseColor("#999999"));
                    mTvSave.setEnabled(false);
                }
            }
        });
        sw_onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setDefault();
                } else {
                    //Todo
                }
            }
        });
    }

    @OnClick({R.id.tv_save})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_save:
                /**
                 * 0:添加地址
                 * 1：编辑地址
                 */
                if (flag == 0) {
                    addAdress();
                } else if (flag == 1) {
                    editAdress();
                }
                break;
        }
    }

    /**
     * 设置默认地址
     */
    private void setDefault() {
        String token=getToken(mContext);
        RetrofitUtil.getInstance().isDefaultAddress(token, id, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    showToast(baseResponse.getMsg());
                }  else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    toActivity(LoginActivity.class);
                }else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    /**
     * 删除地址
     */
    private void deleteAddress() {
        String token=getToken(mContext);
        RetrofitUtil.getInstance().deleteAddress(token, id, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    showToast(baseResponse.getMsg());
                    Intent intent = new Intent();
                    intent.putExtra("result", "2");
                    setResult(1000, intent);
                    finish();
                }  else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    Bundle bundle=new Bundle();
                    bundle.putString("isLogin","1");
                    toActivity(LoginActivity.class,bundle);
                }else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    //


    /**
     * 编辑地址
     */
    private void editAdress() {
        String token=getToken(mContext);
        RetrofitUtil.getInstance().editAddress(token, id, realname, mobile, province, city, area, address, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    showToast(baseResponse.getMsg());
                    Intent intent = new Intent();
                    setResult(1000, intent);
                    finish();
                } else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    Bundle bundle=new Bundle();
                    bundle.putString("isLogin","1");
                    toActivity(LoginActivity.class,bundle);
                }else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    /**
     * 添加地址
     */
    private void addAdress() {
        String token=getToken(mContext);
        realname = mEdRecipientsName.getText().toString().trim();
        mobile = mEdRecipientsPhone.getText().toString().trim();
        address = mEdRecipientsDetailedAddress.getText().toString().trim();
        RetrofitUtil.getInstance().addAddress(token, realname, mobile, province, city, area, address, new Subscriber<BaseResponse<EmptyEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
                if (baseResponse.getStatus() == 1) {
                    showToast(baseResponse.getMsg());
                    Intent intent = new Intent();
                    setResult(1000, intent);
                    finish();
                } else if (baseResponse.getStatus() == -1) {
                    T.showShort(mContext, baseResponse.getMsg());
                    Bundle bundle=new Bundle();
                    bundle.putString("isLogin","1");
                    toActivity(LoginActivity.class,bundle);
                }else {
                    showToast(baseResponse.getMsg());
                }

            }
        });
    }

    private void toastLinkageItem(LinkageItem... items) {
        StringBuilder str = new StringBuilder(" ");
        for (int i = 0; i < items.length && items[i] != null; i++) {
            str.append(items[i].getLinkageName());
            province = items[0].getLinkageName();
            city = items[1].getLinkageName();
            area = items[2].getLinkageName();
            //str.append(" ");
        }
        mTvRecipientsAddress.setText(province + city + area);
        //  Toast.makeText(this, province + city + area, Toast.LENGTH_SHORT).show();
    }


    public List<LinkageItem> getCityList() {
        String json = getJson("city1.json");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProvincesModel>>() {
        }.getType();
        List<ProvincesModel> citys = gson.fromJson(json, type);
        List<LinkageItem> cityList = new ArrayList<>();
        cityList.addAll(citys);
        return cityList;
    }
}
