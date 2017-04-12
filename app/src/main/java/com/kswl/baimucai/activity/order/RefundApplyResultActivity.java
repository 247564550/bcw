package com.kswl.baimucai.activity.order;

import android.os.Bundle;
import android.view.View;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

/**
 * @author wangjie
 * @desc 退款申请结果页
 * @date 2017/2/15 13:02
 */
public class RefundApplyResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_apply_result, true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;

            default:
                break;
        }
    }
}
