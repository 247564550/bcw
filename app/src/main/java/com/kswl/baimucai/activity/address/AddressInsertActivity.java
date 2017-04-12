package com.kswl.baimucai.activity.address;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.AddressBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DBCityHelper;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.kswl.baimucai.view.CustomBottomDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;

public class AddressInsertActivity extends BaseActivity {

    EditText et_contact_name, et_contact_phone, et_address;

    TextView tv_area;

    Button btn_insert;

    AddressBean addressBean;

    /**
     * 省市区
     */
    NumberPicker np_prov, np_city, np_area;

    /**
     * 选中省市区下标
     */
    private int selectProv, selectCity, selectArea;

    private String provId, cityId, areaId;

    /**
     * 省市区数据
     */
    String[] provData;
    String[][] cityData;
    String[][][] areaData;

    Dialog dialog;

    /**
     * true:是新增地址, false:编辑
     */
    boolean isInsert = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_insert);
        showTopLine();
        et_contact_name = (EditText) findViewById(R.id.et_contact_name);
        et_contact_phone = (EditText) findViewById(R.id.et_contact_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        tv_area = (TextView) findViewById(R.id.tv_area);
        btn_insert = (Button) findViewById(R.id.btn_insert);

        DBCityHelper.getInstance().openDB();

        addressBean = (AddressBean) getIntent().getSerializableExtra(Constants.Char
                .ADDRESS_DATA);
        if (null != addressBean) {
            // 编辑地址
            showTopTitle(R.string.address_edit);
            btn_insert.setText(R.string.save);
            isInsert = false;
            et_contact_name.setText(addressBean.getName());
            et_contact_phone.setText(addressBean.getPhone());
            et_address.setText(addressBean.getAddress());
            tv_area.setText(addressBean.getProvince() + addressBean.getCity()
                    + addressBean.getArea());
            provId = addressBean.getProvinceId();
            cityId = addressBean.getCityId();
            areaId = addressBean.getAreaId();
        } else {
            // 新建地址
            showTopTitle(R.string.address_insert);
            isInsert = true;
            btn_insert.setText(R.string.address_insert);
        }
        initAllCityData();
    }

    @Override
    protected void onDestroy() {
        DBCityHelper.getInstance().closeDB();
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_area:
                showChooseAreaView();
                break;

            case R.id.btn_insert:
                String name = et_contact_name.getText().toString().trim();
                String phone = et_contact_phone.getText().toString().trim();
                String address = et_address.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast(R.string.toast_address_input_name);
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(R.string.toast_address_input_phone);
                    return;
                }
                if (TextUtils.isEmpty(tv_area.getText().toString())) {
                    ToastUtil.showToast(R.string.toast_address_input_area);
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.showToast(R.string.toast_address_input_address);
                    return;
                }
                if (isInsert) {
                    // 新建地址
                    insertAddress(name, phone, address);
                } else {
                    // 编辑地址
                    updateAddress(name, phone, address);
                }
                break;

            default:
                break;
        }
    }

    public void initAllCityData() {
        // DialogUtils.getInstance().show(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = DBCityHelper.getInstance().getAllDataArr();
                provData = (String[]) map.get("prov");
                cityData = (String[][]) map.get("city");
                areaData = (String[][][]) map.get("area");
//                final String jsonString = Tools.getStringFromAssets("allArea.json");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            if (null != jsonArray) {
//                                provData = new String[jsonArray.length()];
//                                cityData = new String[jsonArray.length()][];
//                                areaData = new String[jsonArray.length()][][];
//                            }
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject provObj = jsonArray.optJSONObject(i);
//                                // String provId = provObj.optString("provId");
//                                String provName = provObj.optString("provName");
//                                JSONArray cityArr = provObj.optJSONArray("cityList");
//                                if (null != addressBean
//                                        && provName.equals(addressBean.getProvince())) {
//                                    selectProv = i;
//                                }
//                                provData[i] = provName;
//                                cityData[i] = new String[cityArr.length()];
//                                areaData[i] = new String[cityArr.length()][];
//                                for (int j = 0; j < cityArr.length(); j++) {
//                                    JSONObject cityObj = cityArr.optJSONObject(j);
//                                    // String cityId = cityObj.optString("cityId");
//                                    String cityName = cityObj.optString("cityName");
//                                    JSONArray areaArr = cityObj.optJSONArray("areaList");
//
//                                    if (null != addressBean
//                                            && cityName.equals(addressBean.getCity())) {
//                                        selectCity = j;
//                                    }
//                                    cityData[i][j] = cityName;
//                                    areaData[i][j] = new String[areaArr.length()];
//                                    for (int k = 0; k < areaArr.length(); k++) {
//                                        JSONObject areaObj = areaArr.optJSONObject(k);
//                                        // String areaId = areaObj.optString("areaId");
//                                        String areaName = areaObj.optString("areaName");
//                                        if (null != addressBean
//                                                && areaName.equals(addressBean.getArea())) {
//                                            selectArea = k;
//                                        }
//                                        areaData[i][j][k] = areaName;
//                                    }
//                                }
//                            }
//                            DialogUtils.getInstance().dismiss();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        }).start();

    }

    /**
     * @desc 选择省市区
     * @author wangjie
     * @date 2017/2/20 13:46
     */
    private void showChooseAreaView() {
        if (provData == null || provData.length == 0) {
            return;
        }
        if (null != dialog) {
            dialog.show();
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.v_choose_area, null);
        np_prov = (NumberPicker) view.findViewById(R.id.np_province);
        np_city = (NumberPicker) view.findViewById(R.id.np_city);
        np_area = (NumberPicker) view.findViewById(R.id.np_district);

        np_prov.setDisplayedValues(provData);
        np_prov.setMinValue(0);
        np_prov.setMaxValue(provData.length - 1);
        np_prov.setValue(selectProv);
        // 设置是否循环滚动。而且必须在setMaxValue()和setMinValue()的后面调用
        np_prov.setWrapSelectorWheel(false);
        np_prov.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectProv = newVal;
                LogUtil.e("selectProv:" + selectProv);
                np_city.setDisplayedValues(null);
                np_city.setMinValue(0);
                np_city.setMaxValue(cityData[selectProv].length - 1);
                np_city.setDisplayedValues(cityData[selectProv]);
                np_city.setValue(0);

                np_area.setDisplayedValues(null);
                np_area.setMinValue(0);
                np_area.setMaxValue(areaData[selectProv][0].length - 1);
                np_area.setDisplayedValues(areaData[selectProv][0]);
                np_area.setValue(0);
            }
        });

        np_city.setDisplayedValues(cityData[0]);
        np_city.setMinValue(0);
        np_city.setMaxValue(cityData[0].length - 1);
        np_city.setValue(selectCity);
        // 设置是否循环滚动。而且必须在setMaxValue()和setMinValue()的后面调用
        np_city.setWrapSelectorWheel(false);
        np_city.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectCity = newVal;
                LogUtil.e("selectCity:" + selectCity);
                np_area.setDisplayedValues(null);
                np_area.setMinValue(0);
                np_area.setMaxValue(areaData[selectProv][selectCity].length - 1);
                np_area.setDisplayedValues(areaData[selectProv][selectCity]);
                np_area.setValue(0);
            }
        });

        np_area.setDisplayedValues(areaData[0][0]);
        np_area.setMinValue(0);
        np_area.setMaxValue(areaData[0][0].length - 1);
        np_area.setValue(selectArea);
        // 设置是否循环滚动。而且必须在setMaxValue()和setMinValue()的后面调用
        np_area.setWrapSelectorWheel(false);
        np_area.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectArea = newVal;
                LogUtil.e("selectArea:" + selectArea);
            }
        });

        dialog = new CustomBottomDialog.Builder(this).setContentView(view)
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        String provName = provData[selectProv];
                        String cityName = cityData[selectProv][selectCity];
                        String areaName = areaData[selectProv][selectCity][selectArea];
                        tv_area.setText(provName + cityName + areaName);
                        long t = System.currentTimeMillis();
                        provId = DBCityHelper.getInstance().getIdWithName(provName, "0");
                        cityId = DBCityHelper.getInstance().getIdWithName(cityName, provId);
                        areaId = DBCityHelper.getInstance().getIdWithName(areaName, cityId);
                        LogUtil.e(System.currentTimeMillis() - t + "");
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * @desc 新建地址
     * @author wangjie
     * @date 2017/2/20 13:37
     */
    private void insertAddress(String name, String phone, String address) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        params.put("name", name);
        params.put("phone", phone);
        params.put("province", provId);
        params.put("city", cityId);
        params.put("area", areaId);
        params.put("address", address);
        httpRequest(Constants.Url.ADDRESS_INSERT, params, httpCallBack, 0, this);
    }

    /**
     * @desc 修改地址
     * @author wangjie
     * @date 2017/3/20 11:44
     */
    private void updateAddress(String name, String phone, String address) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        params.put("id", addressBean.getId());
        params.put("name", name);
        params.put("phone", phone);
        params.put("province", provId);
        params.put("city", cityId);
        params.put("area", areaId);
        params.put("address", address);
        httpRequest(Constants.Url.ADDRESS_UPDATE, params, httpCallBack, 1, this);
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
                            // 新增地址
                        case 1:
                            // 编辑地址
                            setResult(RESULT_OK);
                            finish();
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
