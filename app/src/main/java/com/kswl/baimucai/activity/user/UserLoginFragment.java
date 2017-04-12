package com.kswl.baimucai.activity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.UserBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.Call;

/**
 * @author wangjie
 * @desc 登录
 * @date 2017/1/16 18:35
 */
public class UserLoginFragment extends BaseFragment implements View.OnClickListener {

    EditText et_phone, et_pwd;

    public static UserLoginFragment newInstance() {
        UserLoginFragment fragment = new UserLoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        Button btn_login = (Button) view.findViewById(R.id.btn_login);
        TextView tv_register = (TextView) view.findViewById(R.id.tv_register);
        TextView tv_forget = (TextView) view.findViewById(R.id.tv_forget);
        LinearLayout ll_wechat = (LinearLayout) view.findViewById(R.id.ll_wechat);
        LinearLayout ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq);

        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        return showTopTitle(view, R.string.login, 0, 0, 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                // 登录
                String phone = et_phone.getText().toString();
                String pwd = et_pwd.getText().toString();
                if (!Tools.validatePhone(phone)) {
                    ToastUtil.showToast(R.string.toast_input_phone);
                    return;
                }
                if (!Tools.validatePwd(pwd)) {
                    ToastUtil.showToast(R.string.toast_input_pwd);
                    return;
                }
                login(phone, pwd);
                break;

            case R.id.tv_register:
                // 注册
                startFragment(UserInputPhoneFragment.newInstance(true));
                break;

            case R.id.tv_forget:
                // 忘记密码
                startFragment(UserInputPhoneFragment.newInstance(false));
                break;

            case R.id.ll_wechat:
                // 微信登录
                ToastUtil.showToast("微信登录");
                break;

            case R.id.ll_qq:
                // QQ登录
                ToastUtil.showToast("QQ登录");
                break;

            default:
                break;
        }
    }

    private void login(String phone, String pwd) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phone", phone);
        params.put("password", pwd);
        mContext.httpRequest(Constants.Url.LOGIN, params, httpCallBack, 0, this);
        DialogUtils.getInstance().show(mContext);
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
                            UserBean user = JsonUtil.JsonToBean(UserBean.class, obj.toString());
                            DialogUtils.getInstance().show(mContext);
                            loginEM(user);
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

    /**
     * 登录环信
     */
    private void loginEM(final UserBean user) {
        EMClient.getInstance().login(user.getId(), "123456", new EMCallBack() {
            // 回调为异步线程
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.getInstance().dismiss();
                        App.app.setUser(user);
                        mContext.finish();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.getInstance().dismiss();
                        ToastUtil.showToast(R.string.toast_network_fail);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
