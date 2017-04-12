package com.kswl.baimucai.activity.order;

import android.os.Bundle;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

/**
 * @author wangjie
 * @desc 物流信息
 * @date 2017/2/15 13:25
 */
public class LogisticsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        showTopTitle(R.string.logistics_info);
    }
}
