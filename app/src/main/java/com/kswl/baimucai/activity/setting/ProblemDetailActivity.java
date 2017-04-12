package com.kswl.baimucai.activity.setting;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

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

public class ProblemDetailActivity extends BaseActivity {

    TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        showTopTitle(R.string.vip_problem);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());

        String problemId = getIntent().getStringExtra(Constants.Char.PROBLEM_ID);
        getProblemDetail(problemId);
    }

    /**
     * @desc 获取常见问题答复
     * @author wangjie
     * @date 2017/2/21 15:10
     */
    private void getProblemDetail(String id) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        httpRequest(Constants.Url.PROBLEM_DETAIL, params, httpCallBack, 0, this);
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
                            String detail = jsonObj.optString("data");
                            tv_content.setText(detail);
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
