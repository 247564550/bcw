package com.kswl.baimucai.activity.goods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.filter.AddressFilterActivity;
import com.kswl.baimucai.activity.main.GoodsAdapter;
import com.kswl.baimucai.activity.search.SortingFragment;
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

public class GoodsListActivity extends BaseActivity implements
        SortingFragment.OnFragmentFilterListener {

    ListView lv_goods;

    PullToRefreshView v_refresh;

    GoodsAdapter adapter;

    List<GoodsBean> data = new ArrayList<>();

    private int curPage = 1;

    /**
     * 分类id
     */
    private String classifyId;

    /**
     * sortord 排序方式<br>
     * city 城市
     */
    private String sortord = "", city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        lv_goods = (ListView) findViewById(R.id.lv_goods);
        v_refresh = (PullToRefreshView) findViewById(R.id.v_refresh);

        classifyId = getIntent().getStringExtra(Constants.Char.CLASSIFY_ID);
        String classifyName = getIntent().getStringExtra(Constants.Char.CLASSIFY_NAME);
        showTopTitle(classifyName);
        sortord = SortingFragment.SORTING_DEFAULT;

        adapter = new GoodsAdapter(this, data);
        lv_goods.setAdapter(adapter);
        lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GoodsListActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constants.Char.GOODS_ID, data.get(i).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getGoodsByClassify();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getGoodsByClassify();
            }
        });

        getGoodsByClassify();
        DialogUtils.getInstance().show(this);
    }

    @Override
    public void onSorting(String sortord) {
        this.sortord = sortord;
        curPage = 1;
        getGoodsByClassify();
        DialogUtils.getInstance().show(this);
    }

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
            getGoodsByClassify();
            DialogUtils.getInstance().show(this);
        }
    }

    /**
     * @desc 根据分类获取商品
     * @author wangjie
     * @date 2017/2/20 16:54
     */
    private void getGoodsByClassify() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        // 分类id
        params.put("type", classifyId);
        params.put("city", city);
        // 排序方式
        params.put("sortAccording", sortord);
        httpRequest(Constants.Url.GOODS_LIST_BY_TYPE, params, httpCallBack, 0, this);
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
