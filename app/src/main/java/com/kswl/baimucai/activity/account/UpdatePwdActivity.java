package com.kswl.baimucai.activity.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.Call;

public class UpdatePwdActivity extends BaseActivity {

    EditText et_old_pwd, et_new_pwd, et_pwd_repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        showTopTitle(R.string.account_pwd);

        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_pwd_repeat = (EditText) findViewById(R.id.et_pwd_repeat);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                String oldPwd = et_old_pwd.getText().toString();
                String newPwd = et_new_pwd.getText().toString();
                String pwdRepeat = et_pwd_repeat.getText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    ToastUtil.showToast(R.string.toast_input_old_pwd);
                    return;
                }
                if (!Tools.validatePwd(newPwd)) {
                    ToastUtil.showToast(R.string.toast_input_pwd);
                    return;
                }
                if (!newPwd.equals(pwdRepeat)) {
                    ToastUtil.showToast(R.string.toast_repeat_pwd);
                    return;
                }
                uploadPwd(oldPwd, newPwd);
                break;

            default:
                break;
        }
    }

    /**
     * @desc 修改密码
     * @author wangjie
     * @date 2017/2/20 14:19
     */
    private void uploadPwd(String oldPwd, String newPwd) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phone", App.app.getUser().getPhone());
        params.put("password", oldPwd);
        params.put("newPassword", newPwd);
        httpRequest(Constants.Url.UPDATE_PWD, params, httpCallBack, 0, this);
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
                            ToastUtil.showToast(R.string.success);
                            finish();
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
