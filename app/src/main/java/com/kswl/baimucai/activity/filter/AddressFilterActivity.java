package com.kswl.baimucai.activity.filter;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.CityBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DBCityHelper;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressFilterActivity extends BaseActivity {

    ListView lv_content, lv_child;

    LinearLayout ll_child;

    TextView tv_child_title;

    ArrayList<CityBean> data = new ArrayList<>();

    ArrayList<CityBean> dataChild = new ArrayList<>();

    AddressFilterAdapter adapter, adapterChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_filter, true);
        lv_content = (ListView) findViewById(R.id.lv_content);
        lv_child = (ListView) findViewById(R.id.lv_child);
        ll_child = (LinearLayout) findViewById(R.id.ll_child);
        tv_child_title = (TextView) findViewById(R.id.tv_child_title);

        adapter = new AddressFilterAdapter(this, data);
        lv_content.setAdapter(adapter);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataChild.clear();
                dataChild.addAll(data.get(i).getChild());
                adapterChild.setSelectPosition(-1);
                adapterChild.notifyDataSetChanged();
                tv_child_title.setText(data.get(i).getName());
                ll_child.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(ll_child, "translationX",
                        App.app.getScreenWidth(), 0f);
                anim.setDuration(200);
                anim.start();
            }
        });

        adapterChild = new AddressFilterAdapter(this, dataChild);
        lv_child.setAdapter(adapterChild);
        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterChild.setSelectPosition(i);
                adapterChild.notifyDataSetChanged();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<CityBean> mList = DBCityHelper.getInstance().getAllProvCityList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data.addAll(mList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
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

            case R.id.tv_child_confirm:
                if (adapterChild.getSelectPosition() >= 0) {
                    CityBean bean = data.get(adapterChild.getSelectPosition());
                    setResult(RESULT_OK, new Intent().putExtra(Constants.Char.CITY_DATA, bean));
                    finish();
                } else {
                    ToastUtil.showToast(R.string.address_filter_toast);
                }
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

    @Override
    protected void onDestroy() {
        DBCityHelper.getInstance().closeDB();
        super.onDestroy();
    }
}
