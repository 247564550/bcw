package com.kswl.baimucai.activity.goods;

import android.os.Bundle;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

/**
 * @author wangjie
 * @desc 商品评价
 * @date 2017/2/13 16:27
 */
public class AppraiseListActivity extends BaseActivity {

    ListView lv_appraise;

    AppraiseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise_list);
        showTopTitle(R.string.goods_appraise_title);
        lv_appraise = (ListView) findViewById(R.id.lv_appraise);

        adapter = new AppraiseAdapter(this, null);
        lv_appraise.setAdapter(adapter);
    }
}
