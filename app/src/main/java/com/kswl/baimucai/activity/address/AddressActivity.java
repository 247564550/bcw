package com.kswl.baimucai.activity.address;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.AddressBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.view.CustomAlertDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

public class AddressActivity extends BaseActivity {

    ListView lv_address;

    AddressAdapter adapter;

    List<AddressBean> data = new ArrayList<>();

    private int selectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        showTopTitle(R.string.address_title);
        showTopLine();
        lv_address = (ListView) findViewById(R.id.lv_address);

        adapter = new AddressAdapter(this, data, mOnItemIndicateListener);
        lv_address.setAdapter(adapter);

        if (getIntent().getBooleanExtra(Constants.Char.ADDRESS_TYPE, false)) {
            lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.Char.ADDRESS_DATA, adapter.getItem(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        getAddressInfo();
    }

    AddressAdapter.OnItemIndicateListener mOnItemIndicateListener = new AddressAdapter
            .OnItemIndicateListener() {
        /**
         * 删除按钮点击事件
         */
        @Override
        public void onDeleteClick(int position) {
            selectPosition = position;
            new CustomAlertDialog.Builder(AddressActivity.this)
                    .setMessage(R.string.toast_address_delete_confirm)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            deleteAddress(data.get(selectPosition).getId());
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).create().show();
        }

        /**
         * 编辑按钮点击事件
         */
        @Override
        public void onEditClick(int position) {
            Intent intent = new Intent(AddressActivity.this, AddressInsertActivity.class);
            intent.putExtra(Constants.Char.ADDRESS_DATA, data.get(position));
            startActivityForResult(intent, 0);
        }

        /**
         * 默认地址按钮点击事件
         */
        @Override
        public void onDefaultClick(int position) {
            if (!"1".equals(data.get(position).getIsDefault())) {
                selectPosition = position;
                setDefaultAddress(data.get(selectPosition).getId());
            }
        }
    };

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                startActivityForResult(new Intent(this, AddressInsertActivity.class), 0);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getAddressInfo();
        }
    }

    /**
     * @desc 获取地址列表
     * @author wangjie
     * @date 2017/2/20 13:25
     */
    private void getAddressInfo() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.ADDRESS_LIST, params, httpCallBack, 0, this);
    }

    /**
     * @param id 要删除的地址id
     * @desc 删除地址
     * @author wangjie
     * @date 2017/3/20 12:34
     */
    private void deleteAddress(String id) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        httpRequest(Constants.Url.ADDRESS_DELETE, params, httpCallBack, 1, this);
    }

    /**
     * @param id 当前被设置的默认地址id
     * @desc 设置默认地址
     * @author wangjie
     * @date 2017/3/20 12:35
     */
    private void setDefaultAddress(String id) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        httpRequest(Constants.Url.ADDRESS_SET_DEFAULT, params, httpCallBack, 2, this);
    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
            DialogUtils.getInstance().dismiss();
            ToastUtil.showToast(R.string.toast_network_fail);
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            DialogUtils.getInstance().dismiss();
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            // 地址列表
                            JSONArray arr = jsonObj.optJSONArray("data");
                            ArrayList<AddressBean> mList = JsonUtil.JsonToBean(AddressBean.class,
                                    arr.toString());
                            if (null != mList) {
                                data.clear();
                                data.addAll(mList);
                                adapter.notifyDataSetChanged();
                            }
                            break;

                        case 1:
                            // 删除地址
                            data.remove(selectPosition);
                            adapter.notifyDataSetChanged();
                            break;

                        case 2:
                            // 设置默认地址
                            for (int i = 0; i < data.size(); i++) {
                                if (i == selectPosition) {
                                    data.get(i).setIsDefault("1");
                                } else {
                                    data.get(i).setIsDefault("0");
                                }
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        default:
                            break;
                    }
                } else {
                    ToastUtil.showToast(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
