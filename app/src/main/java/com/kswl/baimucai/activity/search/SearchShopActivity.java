package com.kswl.baimucai.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.shop.ShopDetailActivity;
import com.kswl.baimucai.bean.ShopBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.view.PullToRefreshView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

public class SearchShopActivity extends BaseActivity {

    TextView tv_sort;

    EditText et_search;

    PullToRefreshView v_refresh;

    ListView lv_shop;

    SearchShopAdapter adapter;

    List<ShopBean> data = new ArrayList<>();

    private int curPage = 1;

    private String searchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop, true);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        et_search = (EditText) findViewById(R.id.et_search);
        lv_shop = (ListView) findViewById(R.id.lv_shop);
        v_refresh = (PullToRefreshView) findViewById(R.id.v_refresh);

        tv_sort.setText(getString(R.string.shop));
        searchKey = getIntent().getStringExtra(Constants.Char.SEARCH_KEY);

        et_search.setText(searchKey);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(searchKey)) {
                    searchKey = et_search.getText().toString();
                    curPage = 1;
                    getShopBySearch();
                    DialogUtils.getInstance().show(SearchShopActivity.this);
                }
                return false;
            }
        });

        adapter = new SearchShopAdapter(this, data);
        lv_shop.setAdapter(adapter);
        lv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchShopActivity.this, ShopDetailActivity.class);
                intent.putExtra(Constants.Char.SHOP_ID, data.get(i).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getShopBySearch();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getShopBySearch();
            }
        });

        getShopBySearch();
        DialogUtils.getInstance().show(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * @desc 搜索店铺
     * @author wangjie
     * @date 2017/2/20 18:39
     */
    private void getShopBySearch() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        // 搜索关键字
        params.put("searchName", searchKey);
        httpRequest(Constants.Url.SHOP_LIST_BY_SEARCH, params, httpCallBack, 0, this);
    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
            DialogUtils.getInstance().dismiss();
            ToastUtil.showToast(R.string.toast_network_fail);
            v_refresh.onHeaderRefreshComplete();
            v_refresh.onFooterRefreshComplete();
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            DialogUtils.getInstance().dismiss();
            v_refresh.onHeaderRefreshComplete();
            v_refresh.onFooterRefreshComplete();
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            JSONArray arr = jsonObj.optJSONArray("data");
                            ArrayList<ShopBean> mList = JsonUtil.JsonToBean(ShopBean.class,
                                    arr.toString());
                            if (curPage == 1) {
                                data.clear();
                            }
                            if (null != mList) {
                                data.addAll(mList);
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
