package com.healthclock.healthclock.ui.activity.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.healthclock.healthclock.R;
import com.healthclock.healthclock.network.model.BaseResponse;
import com.healthclock.healthclock.network.model.EmptyEntity;
import com.healthclock.healthclock.network.model.indent.AddressModel;
import com.healthclock.healthclock.network.util.RetrofitUtil;
import com.healthclock.healthclock.ui.base.BaseActivity;
import com.healthclock.healthclock.util.StringUtil;
import com.healthclock.healthclock.widget.BorderTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class SelectAddressActivity extends BaseActivity {
    @BindView(R.id.lv_address)
    ListView mLvAddress;
    @BindView(R.id.tv_add)
    BorderTextView tv_add;
    private AddressAdapter mAddressAdapter;
    private List<AddressModel.ListBean> mAddresses = null;
    private String province, city, area, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        mAddresses = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(mContext, mAddresses);
       // initData();
        initUI();
    }

    private void initUI() {
        setBackView();
        setTitle("收货地址");
    }
//    private void initData() {
//        loaddingDialog.show();
//        RetrofitUtil.getInstance().getAddressList(openid, new Subscriber<BaseResponse<AddressModel>>() {
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
//            public void onNext(BaseResponse<AddressModel> baseResponse) {
//                loaddingDialog.dismiss();
//                if (baseResponse.code == 200) {
//                    AddressModel mAddressesModel = baseResponse.getData();
//                    List<AddressModel.ListBean> listBeans = mAddressesModel.getList();
//                    // mAddresses = listBeans;
//                    mAddresses.addAll(listBeans);
//                } else {
//                    showProgress(baseResponse.getMsg());
//                }
//                //mAddressAdapter = new AddressAdapter(mContext, mAddresses);
//                mLvAddress.setAdapter(mAddressAdapter);
//                mAddressAdapter.notifyDataSetChanged();
//            }
//        });
//    }

    @OnClick(R.id.tv_add)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_add:
                Intent intent = new Intent(this, EditAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 0);
                bundle.putInt("position", 0);
                bundle.putString("id", "");
                bundle.putString("address", "");
                bundle.putString("realname", "");
                bundle.putString("mobile", "");
                bundle.putString("province", "");
                bundle.putString("city", "");
                bundle.putString("area", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1000);

                break;
        }
    }

    class AddressAdapter extends BaseAdapter {
        private Context mContext;
        private List<AddressModel.ListBean> addresses;

        public AddressAdapter(Context mContext, List<AddressModel.ListBean> mAddresses) {
            this.mContext = mContext;
            this.addresses = mAddresses;
            sortData();
        }

        /**
         * 排序
         */
        private void sortData() {
            //对list进行排序，优先级 是否是默认助理、id
            Collections.sort(addresses, new Comparator<AddressModel.ListBean>() {

                @Override
                public int compare(AddressModel.ListBean lhs, AddressModel.ListBean rhs) {
                    if (lhs.getIsdefault().compareToIgnoreCase(rhs.getIsdefault()) < 0) {
                        return 1;
                    } else if (lhs.getIsdefault().compareToIgnoreCase(rhs.getIsdefault()) == 0) {
                        return lhs.getId().compareToIgnoreCase(rhs.getId());
                    } else {
                        return -1;
                    }
                }
            });
        }

        @Override
        public int getCount() {
            return addresses.size();
        }

        @Override
        public Object getItem(int position) {
            return addresses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * @param position
         * @param convertView
         * @param parent
         * @return
         */


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                //--
                convertView = View.inflate(mContext, R.layout.layout_address_item01, null);
                viewHolder = new ViewHolder();
                viewHolder.tvAddress = convertView.findViewById(R.id.tv_address);
                viewHolder.tvName = convertView.findViewById(R.id.tv_name);
                viewHolder.tvPhone = convertView.findViewById(R.id.tv_phone);
                viewHolder.cbSelected = convertView.findViewById(R.id.cb_selected);
                viewHolder.ivEditAddress01 = convertView.findViewById(R.id.iv_edit_address01);
                viewHolder.ivEditAddress = convertView.findViewById(R.id.iv_edit_address);
                viewHolder.ivDelAddress = convertView.findViewById(R.id.iv_del_address);
                viewHolder.ivDelAddress01 = convertView.findViewById(R.id.iv_del_address01);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //  final AddressModel.ListBean addressModel = (AddressModel.ListBean) getItem(position);
            Typeface iconTypeface = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
            province = addresses.get(position).getProvince();
            city = addresses.get(position).getCity();
            area = addresses.get(position).getArea();
            address = addresses.get(position).getAddress();
            viewHolder.tvAddress.setText(province + city + area + address);
            viewHolder.tvName.setText(addresses.get(position).getRealname());
            viewHolder.tvPhone.setText(addresses.get(position).getMobile());
            viewHolder.cbSelected.setChecked(addresses.get(position).getIsdefault() == "1");
            viewHolder.ivEditAddress.setText("编辑");
            viewHolder.ivDelAddress.setText("删除");
            viewHolder.ivEditAddress01.setTypeface(iconTypeface);
            viewHolder.ivEditAddress01.setText(StringUtil.decode("\\u" + "e649 "));
            viewHolder.ivDelAddress01.setTypeface(iconTypeface);
            viewHolder.ivDelAddress01.setText(StringUtil.decode("\\u" + "e6d5"));
            if (addresses.size() > 0) {
                if (position == 0) {
                    viewHolder.cbSelected.setClickable(false);
                    viewHolder.cbSelected.setChecked(true);
                }
            } else {
                return null;
            }
            viewHolder.cbSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewHolder.cbSelected.isChecked()) {
                        if (addresses.get(0).getIsdefault() == "1") {//之前第一个助理是默认助理
                            addresses.get(0).setIsdefault("0");
                        }
                        addresses.get(position).setIsdefault("1");
                    } else {
                        addresses.get(position).setIsdefault("0");
                    }
                    String id = addresses.get(position).getId();
                    //setDefaultAddress(id);
                    AddressAdapter.this.notifyDataSetChanged();
                }
            });
            viewHolder.ivEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SelectAddressActivity.this, EditAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("flag", 1);
                    bundle.putString("id", addresses.get(position).getId());
                    bundle.putInt("position", position);
                    bundle.putString("address", addresses.get(position).getAddress());
                    bundle.putString("realname", addresses.get(position).getRealname());
                    bundle.putString("mobile", addresses.get(position).getMobile());
                    bundle.putString("province", addresses.get(position).getProvince());
                    bundle.putString("city", addresses.get(position).getCity());
                    bundle.putString("area", addresses.get(position).getArea());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1000);
                }
            });
            viewHolder.ivDelAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //deleteAddress(addresses.get(position).getId(), position);
                }
            });
            return convertView;

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            sortData();
        }
    }

//    private void deleteAddress(String id, final int position) {
//        RetrofitUtil.getInstance().deleteAddress(openid, id, new Subscriber<BaseResponse<EmptyEntity>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
//                if (baseResponse.code == 200) {
//                    showToast("删除成功");
//                    mAddresses.remove(position);
//                    mAddressAdapter.notifyDataSetChanged();
//                } else {
//                    showToast(baseResponse.getMsg());
//                }
//            }
//        });
//    }

//    private void setDefaultAddress(String id) {
//        RetrofitUtil.getInstance().setDefaultAddress(openid, id, new Subscriber<BaseResponse<EmptyEntity>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(BaseResponse<EmptyEntity> baseResponse) {
//                if (baseResponse.code == 200) {
//                    //  showToast(baseResponse.getMsg());
//
//                } else {
//                    showToast(baseResponse.getMsg());
//                }
//            }
//        });
//    }

    class ViewHolder {
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvPhone;
        private CheckBox cbSelected;
        private TextView ivEditAddress01;
        private TextView ivEditAddress;
        private TextView ivDelAddress;
        private TextView ivDelAddress01;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            String result_value = data.getStringExtra("result");
            int position = data.getIntExtra("position", 0);
            if ("1".equals(result_value)) {
                if (mAddresses != null) {
                    mAddresses.clear();
                }
                //initData();
            } else if ("2".equals(result_value)) {
                mAddresses.remove(position);
                mAddressAdapter.notifyDataSetChanged();
            }
        }
    }
}
