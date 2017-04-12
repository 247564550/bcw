package com.kswl.baimucai.activity.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.bean.EventBean;
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

public class EventListActivity extends BaseActivity {

    GridView gv_event;

    PullToRefreshView v_refresh;

    EventAdapter adapter;

    List<EventBean> data = new ArrayList<>();

    private int curPage = 1;

    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        showTopTitle(R.string.shop_event);
        shopId = getIntent().getStringExtra(Constants.Char.SHOP_ID);
        gv_event = (GridView) findViewById(R.id.gv_event);
        v_refresh = (PullToRefreshView) findViewById(R.id.v_refresh);

        adapter = new EventAdapter(this, data);
        gv_event.setAdapter(adapter);
        gv_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
                intent.putExtra(Constants.Char.EVENT_DATA, data.get(i));
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getEvent();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getEvent();
            }
        });

        getEvent();
        DialogUtils.getInstance().show(this);
    }

    /**
     * @desc 获取活动
     * @author wangjie
     * @date 2017/2/20 14:28
     */
    private void getEvent() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        // 0:商家活动 1:系统活动
        params.put("type", "0");
        params.put("id", shopId);
        httpRequest(Constants.Url.GET_EVENT, params, httpCallBack, 0, this);
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
                            JSONArray eventArr = jsonObj.optJSONArray("data");
                            ArrayList<EventBean> eventList = JsonUtil.JsonToBean(EventBean.class,
                                    eventArr.toString());
                            if (curPage == 1) {
                                data.clear();
                            }
                            if (null != eventList) {
                                data.addAll(eventList);
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
