package com.kswl.baimucai.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.activity.setting.TextActivity;
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
 * @desc 输入密码
 * @date 2017/1/16 18:35
 */
public class UserInputPwdFragment extends BaseFragment implements View.OnClickListener {

    EditText et_pwd, et_pwd_repeat;

    CheckBox cb_protocol;

    public static final String IS_REGISTER = "isRegister";
    public static final String PHONE = "phone";
    public static final String CODE = "code";

    /**
     * true 注册
     * false 忘记密码
     */
    private boolean isRegister = true;

    public UserInputPwdFragment() {
    }

    /**
     * @param isRegister true 注册，false 忘记密码
     * @return Fragment实例
     * @author wangjie
     * @date 2017/1/18 9:30
     * @desc 新建一个Fragment实例
     */
    public static UserInputPwdFragment newInstance(boolean isRegister, String phone, String code) {
        UserInputPwdFragment fragment = new UserInputPwdFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_REGISTER, isRegister);
        args.putString(PHONE, phone);
        args.putString(CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_input_pwd, container, false);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        et_pwd_repeat = (EditText) view.findViewById(R.id.et_pwd_repeat);
        cb_protocol = (CheckBox) view.findViewById(R.id.cb_protocol);
        TextView tv_protocol = (TextView) view.findViewById(R.id.tv_protocol);
        LinearLayout ll_protocol = (LinearLayout) view.findViewById(R.id.ll_protocol);
        Button btn_submit = (Button) view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);

        if (null != getArguments())
            isRegister = getArguments().getBoolean(IS_REGISTER, true);
        if (isRegister) {
            ll_protocol.setVisibility(View.VISIBLE);
            tv_protocol.setOnClickListener(this);
        } else {
            ll_protocol.setVisibility(View.GONE);
        }
        return showTopTitle(view, R.string.input_pwd_title, 0, 0, 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                // 提交
                String pwd = et_pwd.getText().toString();
                String repeat = et_pwd_repeat.getText().toString();
                if (!Tools.validatePwd(pwd)) {
                    ToastUtil.showToast(R.string.toast_input_pwd);
                    return;
                }
                if (!pwd.equals(repeat)) {
                    ToastUtil.showToast(R.string.toast_repeat_pwd);
                    return;
                }
                if (isRegister) {
                    // 注册
                    if (cb_protocol.isChecked()) {
                        register(pwd);
                    } else {
                        ToastUtil.showToast(R.string.toast_check_protocol);
                    }
                } else {
                    // 忘记密码
                    forgetPwd(pwd);
                }
                break;

            case R.id.tv_protocol:
                // 用户注册协议
                startActivity(new Intent(mContext, TextActivity.class)
                        .putExtra(Constants.Char.INTENT_TEXT_TYPE, Constants.Code.TEXT_PROTOCOL));
                break;

            default:
                break;
        }
    }

    /**
     * @param pwd 密码
     * @desc 注册
     * @author wangjie
     * @date 2017/2/17 10:09
     */
    private void register(String pwd) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phone", null != getArguments() ? getArguments().getString(PHONE, "") : "");
        params.put("verificationCode", null != getArguments() ?
                getArguments().getString(CODE, "") : "");
        params.put("password", pwd);
        mContext.httpRequest(Constants.Url.REGISTER, params, httpCallBack, 0, this);
        DialogUtils.getInstance().show(mContext);
    }

    /**
     * @param pwd 密码
     * @desc 忘记密码
     * @author wangjie
     * @date 2017/2/17 10:09
     */
    private void forgetPwd(String pwd) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phone", null != getArguments() ? getArguments().getString(PHONE, "") : "");
        params.put("verificationCode", null != getArguments() ?
                getArguments().getString(CODE, "") : "");
        params.put("password", pwd);
        mContext.httpRequest(Constants.Url.REGISTER, params, httpCallBack, 1, this);
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
                            // 注册
                            ToastUtil.showToast(R.string.toast_register_success);
                            JSONObject obj = jsonObj.optJSONObject("data");
                            UserBean user = JsonUtil.JsonToBean(UserBean.class, obj.toString());
                            App.app.setUser(user);
                            mContext.finish();
                            break;

                        case 1:
                            // 忘记密码
                            startActivity(new Intent(mContext, UserActivity.class));
                            mContext.finish();
                            ToastUtil.showToast(R.string.toast_pwd_success);
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

//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                //注册失败会抛出HyphenateException
//                                try {
//                                    EMClient.getInstance().createAccount(App.app.getUser()
//                                            .getId(), "a123456");
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ToastUtil.showToast("注册成功");
//                                        }
//                                    });
//                                } catch (HyphenateException e) {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ToastUtil.showToast("注册失败");
//                                        }
//                                    });
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();

}
