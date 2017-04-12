package com.kswl.baimucai.activity.search;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.kswl.baimucai.view.FlowViewGroup;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SearchActivity extends BaseActivity {

    public static final String fileName = "search.history";

    PopupWindow popupWindow;

    TextView tv_sort;

    FlowViewGroup fvg_hot, fvg_history;

    EditText et_search;

    private enum SearchType {
        GOODS, SHOP
    }

    private SearchType searchType = SearchType.GOODS;

    ArrayList<String> dataHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search, true);
        tv_sort = (TextView) findViewById(R.id.tv_sort);
        fvg_hot = (FlowViewGroup) findViewById(R.id.fvg_hot);
        fvg_history = (FlowViewGroup) findViewById(R.id.fvg_history);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String searchKey = et_search.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(searchKey)) {
                    if (dataHistory.contains(searchKey)) {
                        dataHistory.remove(searchKey);
                        dataHistory.add(0, searchKey);
                    } else {
                        dataHistory.add(0, searchKey);
                    }
                    Tools.saveObject(App.app, fileName, dataHistory);
                    search(searchKey);
                }
                return false;
            }
        });

        // 获取本地历史记录信息
        ArrayList<String> history = Tools.getObject(App.app, fileName);
        if (null != history) {
            dataHistory.addAll(history);
            showItemView(fvg_history, dataHistory);
        }

        getHotSearch();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_sort:
                showSortView();
                break;

            case R.id.btn_clear:
                dataHistory.clear();
                Tools.saveObject(App.app, fileName, dataHistory);
                fvg_history.removeAllViews();
                break;

            default:
                break;
        }
    }

    private void search(String searchKey) {
        switch (searchType) {
            case GOODS:
                startActivity(new Intent(this, SearchGoodsActivity.class)
                        .putExtra(Constants.Char.SEARCH_KEY, searchKey));
                break;

            case SHOP:
                startActivity(new Intent(this, SearchShopActivity.class)
                        .putExtra(Constants.Char.SEARCH_KEY, searchKey));
                break;

            default:
                break;
        }
        finish();
    }

    /**
     * @param ll_parent
     * @param data
     * @desc 展示热门搜索和历史搜索
     * @author wangjie
     * @date 2017/2/10 18:15
     */
    private void showItemView(ViewGroup ll_parent, List<String> data) {
        ll_parent.removeAllViews();
        if (null != data) {
            for (int i = 0; i < data.size(); i++) {
                final String str = data.get(i);
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout
                        .item_search_record, ll_parent, false);
                textView.setText(str);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        search(str);
                    }
                });
                ll_parent.addView(textView);
            }
        }
    }

    /**
     * @desc 展示搜索类型选择
     * @author wangjie
     * @date 2017/2/10 17:44
     */
    private void showSortView() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_search_type, null);
        TextView tv_goods = (TextView) contentView.findViewById(R.id.tv_goods);
        TextView tv_shop = (TextView) contentView.findViewById(R.id.tv_shop);
        tv_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                searchType = SearchType.GOODS;
                tv_sort.setText(getString(R.string.goods));
            }
        });
        tv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                searchType = SearchType.SHOP;
                tv_sort.setText(getString(R.string.shop));
            }
        });
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(contentView);

        popupWindow.setBackgroundDrawable(new ColorDrawable(0x804B4B4B));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        /**
         * popupWindow.showAsDropDown（View view）弹出对话框，位置在紧挨着view组件
         * showAsDropDown(View anchor, int xoff, int yoff)弹出对话框，位置在紧挨着view组件，x y
         * 代表着偏移量 showAtLocation(View parent, int gravity, int x, int y)弹出对话框
         * parent 父布局 gravity 依靠父布局的位置如Gravity.CENTER x y 坐标值
         */
        popupWindow.showAsDropDown(tv_sort);
    }

    private void getHotSearch() {
        httpRequest(Constants.Url.SEARCH_HOT, null, httpCallBack, 0, this);
        DialogUtils.getInstance().show(this);
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
                            ArrayList<String> dataHot = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                dataHot.add(arr.optJSONObject(i).optString("name"));
                            }
                            showItemView(fvg_hot, dataHot);
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
