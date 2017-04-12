package com.kswl.baimucai.activity.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;

public class FeedbackActivity extends BaseActivity {

    EditText et_title, et_desc;

    TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        showTopTitle(R.string.feedback_title);

        et_title = (EditText) findViewById(R.id.et_title);
        et_desc = (EditText) findViewById(R.id.et_desc);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                ToastUtil.showToast("提交");
                break;

            case R.id.tv_phone:
                String phone = tv_phone.getText().toString();
                Tools.callPhone(this, phone);
                break;

            default:
                break;
        }
    }

}
