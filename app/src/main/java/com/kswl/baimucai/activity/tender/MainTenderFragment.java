package com.kswl.baimucai.activity.tender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.bean.FilterBean;
import com.kswl.baimucai.bean.TenderBean;
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
 * @desc 招标大厅
 * @date 2017/1/16 18:35
 */
public class MainTenderFragment extends BaseFragment implements View.OnClickListener {

    ListView lv_content;

    PullToRefreshView v_refresh;

    TextView tv_choose;

    EditText et_search;

    TenderAdapter adapter;

    private int curPage = 1;

    List<TenderBean> data = new ArrayList<>();

    private String startDate, endDate, typeId, statusId, provId, searchKey;

    public static MainTenderFragment newInstance() {
        MainTenderFragment fragment = new MainTenderFragment();
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tender, container, false);
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        v_refresh = (PullToRefreshView) view.findViewById(R.id.v_refresh);
        tv_choose = (TextView) view.findViewById(R.id.tv_choose);
        et_search = (EditText) view.findViewById(R.id.et_search);
        view.findViewById(R.id.tv_sort).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new TenderAdapter(mContext, data);
        lv_content.setAdapter(adapter);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, TenderDetailActivity.class);
                intent.putExtra(Constants.Char.TENDER_ID, adapter.getItem(i).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getTenderData();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getTenderData();
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(searchKey)) {
                    searchKey = et_search.getText().toString();
                    curPage = 1;
                    getTenderData();
                    DialogUtils.getInstance().show(mContext);
                }
                return false;
            }
        });

        getTenderData();
        DialogUtils.getInstance().show(mContext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sort:
                startActivityForResult(new Intent(mContext, TenderFilterActivity.class),
                        Constants.Code.REQUEST_TENDER_FILTER);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.Code.REQUEST_TENDER_FILTER:
                    ArrayList<FilterBean> filterData = (ArrayList<FilterBean>) intent
                            .getSerializableExtra(Constants.Char.TENDER_FILTER);
                    String filter = "";
                    for (int i = 0; i < filterData.size(); i++) {
                        FilterBean bean = filterData.get(i);
                        if (!TextUtils.isEmpty(bean.getKey())) {
                            filter += bean.getValue() + " ";
                        }
                    }
                    tv_choose.setText(mContext.getString(R.string.tender_choose) + filter);
                    typeId = filterData.get(0).getKey();
                    statusId = filterData.get(1).getKey();
                    provId = filterData.get(2).getKey();
                    if (TextUtils.isEmpty(filterData.get(3).getKey())) {
                        startDate = "";
                        endDate = "";
                    } else {
                        String[] date = filterData.get(3).getKey().split(Constants.Char.TENDER_DD);
                        startDate = date[0];
                        endDate = date[1];
                    }
                    curPage = 1;
                    getTenderData();
                    DialogUtils.getInstance().show(mContext);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * @desc 获取招标数据
     * @author wangjie
     * @date 2017/2/20 17:40
     */
    private void getTenderData() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        // 筛选中的起始日期时间戳
        params.put("startStamp", startDate);
        params.put("endStamp", endDate);
        // 分类Id
        params.put("typeId", typeId);
        // 招标状态;00700001:进行中\00700002:已结束
        params.put("tenderStatus", statusId);
        // 地区（110000；210000）
        params.put("address", provId);
        // 关键字
        params.put("nameLike", searchKey);
        mContext.httpRequest(Constants.Url.TENDER_SEARCH, params, httpCallBack, 0, this);
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
                            ArrayList<TenderBean> mList = JsonUtil.JsonToBean(TenderBean.class,
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
