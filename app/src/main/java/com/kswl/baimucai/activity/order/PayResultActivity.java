package com.kswl.baimucai.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

import java.util.Random;

/**
 * @author wangjie
 * @desc 支付状态结果页
 * @date 2017/2/14 16:00
 */
public class PayResultActivity extends BaseActivity {

    ImageView iv_icon;

    TextView tv_state, tv_state_hint;

    Button btn_complete;

    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        showTopTitle(R.string.pay_result_title);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_state_hint = (TextView) findViewById(R.id.tv_state_hint);
        btn_complete = (Button) findViewById(R.id.btn_complete);


        type = new Random().nextInt(2);
        if (type == 0) {
            // 成功
            iv_icon.setImageResource(R.drawable.pay_success_icon);
            tv_state.setText(R.string.pay_success);
            tv_state_hint.setText(R.string.pay_success_hint);
            btn_complete.setText(R.string.complete);
        } else {
            // 失败
            showRightText(R.string.complete);
            iv_icon.setImageResource(R.drawable.pay_fail_icon);
            tv_state.setText(R.string.pay_fail);
            tv_state_hint.setText(R.string.pay_fail_hint);
            btn_complete.setText(R.string.pay_goto);
        }
    }

    @Override
    public void onRightTextBtnClick() {
        // 支付失败完成
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:
                if (type == 0) {
                    finish();
                } else {
                    startActivity(new Intent(this, PayConfirmActivity.class));
                    finish();
                }
                break;

            default:
                break;
        }
    }
}
