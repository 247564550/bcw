package com.kswl.baimucai.activity.collect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.activity.goods.GoodsDetailActivity;
import com.kswl.baimucai.activity.main.GoodsAdapter;
import com.kswl.baimucai.app.App;
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

/**
 * @author wangjie
 * @desc 我的收藏-商品
 * @date 2017/2/10 10:48
 */
public class CollectGoodsFragment extends BaseFragment {

    ListView lv_collect;

    PullToRefreshView v_refresh;

    GoodsAdapter adapter;

    List<GoodsBean> data = new ArrayList<>();

    private int curPage = 1;

    public static CollectGoodsFragment newInstance() {
        CollectGoodsFragment fragment = new CollectGoodsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        lv_collect = (ListView) view.findViewById(R.id.lv_collect);
        v_refresh = (PullToRefreshView) view.findViewById(R.id.v_refresh);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new GoodsAdapter(mContext, data);
        lv_collect.setAdapter(adapter);
        lv_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra(Constants.Char.GOODS_ID, data.get(i).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getGoodsByCollect();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getGoodsByCollect();
            }
        });

        getGoodsByCollect();
        DialogUtils.getInstance().show(mContext);
    }

    /**
     * @desc 收藏商品列表
     * @author wangjie
     * @date 2017/2/20 18:39
     */
    private void getGoodsByCollect() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        params.put("type", "01900001");
        params.put("userId", App.app.getUser().getId());
        mContext.httpRequest(Constants.Url.COLLECT_LIST_OWN, params, httpCallBack, 0, this);
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
