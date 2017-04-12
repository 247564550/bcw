package com.kswl.baimucai.activity.setting;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.Call;

public class WebviewActivity extends BaseActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        showTopLine();
        webView = (WebView) findViewById(R.id.webView);
        initWebview();
        int type = getIntent().getIntExtra(Constants.Char.INTENT_WEB_TYPE, 0);
        String[] about = getResources().getStringArray(R.array.about_content);
        switch (type) {
            case Constants.Code.WEB_SITES:
                // 网站介绍
                showTopTitle(about[0]);
                getAboutDetail("00800005");
                break;

            case Constants.Code.WEB_COMPANY:
                // 公司介绍
                showTopTitle(about[1]);
                getAboutDetail("00800006");
                break;

            case Constants.Code.WEB_LEGAL_NOTICES:
                // 法律声明
                showTopTitle(about[2]);
                getAboutDetail("00800007");
                break;

            default:
                break;
        }
    }

    /**
     * @desc 初始化webview相关配置
     * @author wangjie
     * @date 2017/2/14 13:23
     */
    private void initWebview() {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setUseWideViewPort(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        // wv.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放(WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置)
        webView.getSettings().setUseWideViewPort(false);
        // 自适应屏幕
        webView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置不使用缓存，只从网络获取数据.
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置
        // 缓存模式
        webView.getSettings().setLoadWithOverviewMode(true);
    }

    /**
     * @desc 获取关于我们
     * @author wangjie
     * @date 2017/2/21 15:51
     */
    private void getAboutDetail(String id) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("type", id);
        httpRequest(Constants.Url.ABOUT_DETAIL, params, httpCallBack, 0, this);
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
                            JSONObject obj = jsonObj.optJSONObject("data");
                            String content = obj.optString("content");
                            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
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
