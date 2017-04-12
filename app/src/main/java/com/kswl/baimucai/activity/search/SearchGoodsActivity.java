package com.kswl.baimucai.activity.search;

import android.content.Intent;
import android.net.Uri;
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
import com.kswl.baimucai.activity.filter.AddressFilterActivity;
import com.kswl.baimucai.activity.goods.GoodsDetailActivity;
import com.kswl.baimucai.activity.goods.GoodsListActivity;
import com.kswl.baimucai.activity.main.GoodsAdapter;
import com.kswl.baimucai.bean.CityBean;
import com.kswl.baimucai.bean.GoodsBean;
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

public class SearchGoodsActivity extends BaseActivity implements
        SortingFragment.OnFragmentFilterListener {

    TextView tv_sort;

    EditText et_search;

    PullToRefreshView v_refresh;

    ListView lv_goods;

    GoodsAdapter adapter;

    List<GoodsBean> data = new ArrayList<>();

    private int curPage = 1;

    /**
     * sortord 排序方式<br>
     * city 城市
     */
    private String sortord = "", city = "", searchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods, true);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        et_search = (EditText) findViewById(R.id.et_search);
        lv_goods = (ListView) findViewById(R.id.lv_goods);
        v_refresh = (PullToRefreshView) findViewById(R.id.v_refresh);

        tv_sort.setText(getString(R.string.goods));
        searchKey = getIntent().getStringExtra(Constants.Char.SEARCH_KEY);
        sortord = SortingFragment.SORTING_DEFAULT;

        et_search.setText(searchKey);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(searchKey)) {
                    searchKey = et_search.getText().toString();
                    curPage = 1;
                    getGoodsBySearch();
                    DialogUtils.getInstance().show(SearchGoodsActivity.this);
                }
                return false;
            }
        });

        adapter = new GoodsAdapter(this, data);
        lv_goods.setAdapter(adapter);
        lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchGoodsActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constants.Char.GOODS_ID, data.get(i).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getGoodsBySearch();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getGoodsBySearch();
            }
        });

        getGoodsBySearch();
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
     * @desc 排序
     * @author wangjie
     * @date 2017/2/13 13:17
     */
    @Override
    public void onSorting(String sortord) {
        this.sortord = sortord;
        curPage = 1;
        getGoodsBySearch();
        DialogUtils.getInstance().show(this);
    }

    /**
     * @desc 筛选
     * @author wangjie
     * @date 2017/2/13 13:17
     */
    @Override
    public void onFilter() {
        startActivityForResult(new Intent(this, AddressFilterActivity.class),
                Constants.Code.REQUEST_CHOOSE_CITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == Constants.Code.REQUEST_CHOOSE_CITY) {
            CityBean cityInfo = (CityBean) intent.getSerializableExtra(Constants.Char.CITY_DATA);
            city = cityInfo.getCode();
            curPage = 1;
            getGoodsBySearch();
            DialogUtils.getInstance().show(this);
        }
    }

    /**
     * @desc 根据分类获取商品
     * @author wangjie
     * @date 2017/2/20 16:54
     */
    private void getGoodsBySearch() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        // 搜索关键字
        params.put("searchName", searchKey);
        // 分类id
        params.put("type", "");
        // 店铺ID ，搜索店铺活动时可用
        params.put("id", "");
        params.put("city", city);
        // 排序方式
        params.put("sortAccording", sortord);
        httpRequest(Constants.Url.GOODS_LIST_BY_SEARCH, params, httpCallBack, 0, this);
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
                            ArrayList<GoodsBean> mList = JsonUtil.JsonToBean(GoodsBean.class,
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
