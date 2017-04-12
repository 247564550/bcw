package com.kswl.baimucai.activity.tender;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.FilterBean;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

public class TenderFilterActivity extends BaseActivity {

    ListView lv_content, lv_child;

    LinearLayout ll_child;

    TextView tv_child_confirm, tv_child_title;

    /**
     * 筛选类型的下标
     */
    private int selectPosition = 0;

    TenderFilterAdapter adapter;

    TenderFilterAdapter adapterChild;

    ArrayList<FilterBean> data = new ArrayList<>();
    ArrayList<FilterBean> typeData = new ArrayList<>();
    ArrayList<FilterBean> statusData = new ArrayList<>();
    ArrayList<FilterBean> provData = new ArrayList<>();
    ArrayList<FilterBean> dateData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_filter, true);
        lv_content = (ListView) findViewById(R.id.lv_content);
        lv_child = (ListView) findViewById(R.id.lv_child);
        ll_child = (LinearLayout) findViewById(R.id.ll_child);
        tv_child_confirm = (TextView) findViewById(R.id.tv_child_confirm);
        tv_child_title = (TextView) findViewById(R.id.tv_child_title);

        initData();
        adapter = new TenderFilterAdapter(this, data);
        lv_content.setAdapter(adapter);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_child_title.setText(data.get(i).getName());
                List<FilterBean> dataChild = new ArrayList<>();
                selectPosition = i;
                switch (i) {
                    case 0:
                        tv_child_confirm.setVisibility(View.GONE);
                        dataChild.addAll(typeData);
                        break;

                    case 1:
                        tv_child_confirm.setVisibility(View.GONE);
                        dataChild.addAll(statusData);
                        break;

                    case 2:
                        tv_child_confirm.setVisibility(View.GONE);
                        dataChild.addAll(provData);
                        break;

                    case 3:
                        tv_child_confirm.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(data.get(selectPosition).getKey())) {
                            for (int j = 0; j < dateData.size(); j++) {
                                dateData.get(j).setKey("");
                                dateData.get(j).setValue("");
                            }
                        }
                        dataChild.addAll(dateData);
                        break;

                    default:
                        break;
                }
                adapterChild = new TenderFilterAdapter(TenderFilterActivity.this, dataChild);
                lv_child.setAdapter(adapterChild);
                ll_child.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(ll_child, "translationX",
                        App.app.getScreenWidth(), 0f);
                anim.setDuration(200);
                anim.start();
            }
        });

        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (selectPosition) {
                    case 0:
                    case 1:
                    case 2:
                        last();
                        FilterBean bean = (FilterBean) lv_child.getAdapter().getItem(i);
                        data.get(selectPosition).setKey(bean.getKey());
                        data.get(selectPosition).setValue(bean.getValue());
                        adapter.notifyDataSetChanged();
                        break;

                    case 3:
                        showReserveDate(i);
                        break;

                    default:
                        break;
                }
            }
        });
        getTenderTypes();
    }

    private void initData() {
        // 初始化筛选条件
        String[] filterArr = getResources().getStringArray(R.array.tender_filter);
        for (int i = 0; i < filterArr.length; i++) {
            data.add(new FilterBean(filterArr[i], "", ""));
        }
        // 初始化投标状态
        String[] statusKeyArr = getResources().getStringArray(R.array.tender_status_key);
        String[] statusValueArr = getResources().getStringArray(R.array.tender_status_value);
        for (int i = 0; i < statusKeyArr.length; i++) {
            statusData.add(new FilterBean("", statusKeyArr[i], statusValueArr[i]));
        }
        // 初始化时间筛选条件
        String[] dateArr = getResources().getStringArray(R.array.tender_date);
        for (int i = 0; i < dateArr.length; i++) {
            dateData.add(new FilterBean(dateArr[i], "", ""));
        }
        provData.addAll(DBCityHelper.getInstance().getTenderProvList());
    }

    @Override
    protected void onDestroy() {
        DBCityHelper.getInstance().closeDB();
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_transparent:
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.iv_child_last:
                last();
                break;

            case R.id.tv_confirm:
                Intent intent = new Intent();
                intent.putExtra(Constants.Char.TENDER_FILTER, data);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.tv_child_confirm:
                if (TextUtils.isEmpty(dateData.get(0).getKey()) ||
                        TextUtils.isEmpty(dateData.get(1).getKey())) {
                    break;
                }
                last();
                data.get(3).setKey(dateData.get(0).getKey() + Constants.Char.TENDER_DD +
                        dateData.get(1).getKey());
                data.get(3).setValue(dateData.get(0).getValue() + getString(R.string.to) +
                        dateData.get(1).getValue());
                adapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    private void last() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(ll_child, "translationX",
                0f, App.app.getScreenWidth());
        anim.setDuration(200);
        anim.start();
        ll_child.setVisibility(View.GONE);
    }

    private void showReserveDate(final int position) {
        View view = getLayoutInflater().inflate(R.layout.v_datepicker, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        new CustomBottomDialog.Builder(this)
                .setContentView(view).setPositiveButton(null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        String date = datePicker.getYear() + "-" + mFormatter.format(datePicker
                                .getMonth() + 1) + "-" + mFormatter.format(datePicker
                                .getDayOfMonth());
                        long dateMis = Tools.dateStringToLong(date + " 00:00:00",
                                "yyyy-MM-dd HH:mm:ss");
                        adapterChild.getItem(position).setKey(dateMis + "");
                        adapterChild.getItem(position).setValue(date);
                        adapterChild.notifyDataSetChanged();
                    }
                }).create().show();
    }

    NumberPicker.Formatter mFormatter = new NumberPicker.Formatter() {
        @Override
        public String format(int value) {
            String tmpStr = String.valueOf(value);
            if (value < 10) {
                tmpStr = "0" + tmpStr;
            }
            return tmpStr;
        }
    };

    private void getTenderTypes() {
        DialogUtils.getInstance().show(this);
        httpRequest(Constants.Url.TENDER_TYPES, null, httpCallBack, 0, this);
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
                            JSONArray arr = jsonObj.optJSONArray("data");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.optJSONObject(i);
                                String key = obj.optString("id");
                                String value = obj.optString("name");
                                typeData.add(new FilterBean("", key, value));
                            }
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
