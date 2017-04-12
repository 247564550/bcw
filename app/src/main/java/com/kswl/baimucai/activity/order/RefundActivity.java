package com.kswl.baimucai.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

/**
 * @author wangjie
 * @desc 退款申请
 * @date 2017/2/15 12:53
 */
public class RefundActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        showTopTitle(R.string.order_btn_refund);
        showTopLine();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_apply:
                startActivity(new Intent(this, RefundApplyResultActivity.class));
                finish();
                break;

            default:
                break;
        }
    }
}
