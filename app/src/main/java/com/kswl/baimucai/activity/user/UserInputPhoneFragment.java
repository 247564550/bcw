package com.kswl.baimucai.activity.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
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

/**
 * @author wangjie
 * @desc 注册-忘记密码
 * @date 2017/1/16 18:35
 */
public class UserInputPhoneFragment extends BaseFragment implements View.OnClickListener {

    public static final String IS_REGISTER = "isRegister";

    EditText et_phone, et_code;

    TextView tv_verify;

    /**
     * true 注册
     * false 忘记密码
     */
    private boolean isRegister = true;

    public UserInputPhoneFragment() {
    }

    /**
     * @param isRegister true 注册，false 忘记密码
     * @return Fragment实例
     * @author wangjie
     * @date 2017/1/18 9:30
     * @desc 新建一个Fragment实例
     */
    public static UserInputPhoneFragment newInstance(boolean isRegister) {
        UserInputPhoneFragment fragment = new UserInputPhoneFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_REGISTER, isRegister);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_input_phone, container, false);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_code = (EditText) view.findViewById(R.id.et_code);
        Button btn_next = (Button) view.findViewById(R.id.btn_next);
        tv_verify = (TextView) view.findViewById(R.id.tv_verify);

        btn_next.setOnClickListener(this);
        tv_verify.setOnClickListener(this);

        if (null != getArguments())
            isRegister = getArguments().getBoolean(IS_REGISTER, true);

        return showTopTitle(view, isRegister ? R.string.register : R.string.forget_pwd_title,
                0, 0, 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        String phone = et_phone.getText().toString();
        String code = et_code.getText().toString();
        switch (view.getId()) {
            case R.id.btn_next:
                // 下一步
                if (!Tools.validatePhone(phone)) {
                    ToastUtil.showToast(R.string.toast_input_phone);
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(R.string.toast_input_verify);
                    return;
                }
                startFragment(UserInputPwdFragment.newInstance(isRegister, phone, code));
                break;

            case R.id.tv_verify:
                // 获取验证码
                if (Tools.validatePhone(phone)) {
                    getVerifyCode(phone);
                } else {
                    ToastUtil.showToast(R.string.toast_input_phone);
                }
                break;

            default:
                break;
        }
    }

    /**
     * @desc 获取验证码, type 0：注册 1：忘记密码
     * @author wangjie
     * @date 2017/2/16 17:55
     */
    private void getVerifyCode(String phone) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("phone", phone);
        params.put("type", isRegister ? "0" : "1");
        mContext.httpRequest(Constants.Url.GET_VERIFY_CODE, params, httpCallBack, 0, this);
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
                            timer = new TimeCount(60000, 1000);
                            // 开始计时
                            timer.start();
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
     * 自定义计时器
     */
    private TimeCount timer;

    /**
     * @author wangjie
     * @desc 倒数计时器
     * @date 2017/2/16 17:22
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            /*
             * 参数依次为总时长,和计时的时间间隔
			 */
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            /*
             * 计时完毕时触发
			 */
            tv_verify.setText(R.string.get_verifiy_code);
            tv_verify.setClickable(true);
            tv_verify.setTextColor(mContext.getResources().getColor(R.color.text_orange));
        }

        @Override
        public void onTick(long millisUntilFinished) {
            /*
             * 计时过程显示
			 */
            tv_verify.setClickable(false);
            tv_verify.setText(millisUntilFinished / 1000 + mContext.getResources()
                    .getString(R.string.seconds));
            tv_verify.setTextColor(mContext.getResources().getColor(R.color.text_dark_gray));
        }
    }

}
