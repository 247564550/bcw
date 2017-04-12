package com.kswl.baimucai.activity.order;

import android.os.Bundle;
import android.view.View;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

public class OrderAppraiseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_appraise);
        showTopTitle(R.string.order_appraise);
        showTopLine();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                finish();
                break;

            default:
                break;
        }
    }

}
