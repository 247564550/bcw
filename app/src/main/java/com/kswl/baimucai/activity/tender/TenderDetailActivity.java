package com.kswl.baimucai.activity.tender;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.bean.TenderBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.Call;

public class TenderDetailActivity extends BaseActivity {

    WebView wb_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_detail);
        showTopTitle(R.string.tender_detail_title);
        showTopLine();
        wb_detail = (WebView) findViewById(R.id.wb_detail);

        String tenderId = getIntent().getStringExtra(Constants.Char.TENDER_ID);
        getTenderData(tenderId);
    }

    /**
     * @desc 初始化webview相关配置
     * @author wangjie
     * @date 2017/2/14 13:23
     */
    private void initWebview(String data) {
        wb_detail.clearCache(true);
        wb_detail.getSettings().setJavaScriptEnabled(true);
        wb_detail.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 设置可以支持缩放
        wb_detail.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        // wv.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放(WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置)
        wb_detail.getSettings().setUseWideViewPort(true);
        // 缓存模式
        wb_detail.getSettings().setLoadWithOverviewMode(true);
        // 自适应屏幕
        wb_detail.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置不使用缓存，只从网络获取数据.
        wb_detail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置

        String html = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\"" +
                " content=\"width=device-width, initial-scale=1.0\">"
                + "<style type=\"text/css\">body { font-family: Arial,\"microsoft yahei\"," +
                "Verdana; word-wrap:break-word; padding:0;	margin:0; font-size:14px; " +
                "color:#000; background: #fff; " +
                "overflow-x:hidden; }img {padding:0; margin:0; vertical-align:top; border: " +
                "none}li,ul {list-style: none outside none; padding: 0; margin: " +
                "0}input[type=text],select {-webkit-appearance:none; -moz-appearance: none; " +
                "margin:0; padding:0; background:none; border:none; font-size:14px; " +
                "text-indent:3px; font-family: Arial,\"microsoft yahei\",Verdana;}.wrapper { " +
                "width:95%; padding-right:10px;padding-top:10px;padding-bottom:10px;" +
                "padding-left:10px; box-sizing:border-box;}p { color:#666; line-height:1.0em;}" +
                ".wrapper img { display:block; max-width:100%; height:auto !important; " +
                "margin-bottom:10px;} p {margin:0; margin-bottom:1px;}</style>"
                + "</head><body><div class=\"wrapper\">" + data + "</div></body></html>";

        wb_detail.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }


    /**
     * @desc 获取招标详情数据
     * @author wangjie
     * @date 2017/2/20 17:40
     */
    private void getTenderData(String tenderId) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", tenderId);
        httpRequest(Constants.Url.TENDER_CONTENT, params, httpCallBack, 0, this);
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
                            String data = jsonObj.optString("data");
                            initWebview(data);
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
