package com.kswl.baimucai.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.bean.ProblemBean;
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

import okhttp3.Call;

public class ProblemsActivity extends BaseActivity {

    ListView lv_content;

    ArrayList<ProblemBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        showTopTitle(R.string.about_problem);
        lv_content = (ListView) findViewById(R.id.lv_content);

        getProblems();
    }

    /**
     * @desc 获取常见问题列表
     * @author wangjie
     * @date 2017/2/21 15:10
     */
    private void getProblems() {
        DialogUtils.getInstance().show(this);
        httpRequest(Constants.Url.PROBLEM_LIST, null, httpCallBack, 0, this);
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
                            data = JsonUtil.JsonToBean(ProblemBean.class,
                                    arr.toString());
                            if (null != data && data.size() > 0) {
                                lv_content.setAdapter(new ArrayAdapter<ProblemBean>(ProblemsActivity
                                        .this, R.layout.item_about, R.id.tv_name, data));
                                lv_content.setOnItemClickListener(new AdapterView
                                        .OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view,
                                                            int i, long l) {
                                        Intent intent = new Intent(ProblemsActivity.this,
                                                ProblemDetailActivity.class);
                                        intent.putExtra(Constants.Char.PROBLEM_ID, data.get(i)
                                                .getId());
                                        startActivity(intent);
                                    }
                                });
                            }
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
