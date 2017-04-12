package com.kswl.baimucai.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.bean.OrderBean;
import com.kswl.baimucai.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity {

    ListView lv_order;

    OrderAdapter adapter;

    List<OrderBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        final int type = getIntent().getIntExtra(Constants.Char.ORDER_TYPE, Constants.Code
                .REQUEST_ORDER_ALL);

        OrderBean bean = new OrderBean();
        switch (type) {
            case Constants.Code.REQUEST_ORDER_PAYMENT:
                showTopTitle(R.string.order_payment);
                bean.setType(0);
                break;

            case Constants.Code.REQUEST_ORDER_SEND:
                showTopTitle(R.string.order_send);
                bean.setType(1);
                break;

            case Constants.Code.REQUEST_ORDER_RECEIVE:
                showTopTitle(R.string.order_receive);
                bean.setType(2);
                break;

            case Constants.Code.REQUEST_ORDER_REFUND:
                showTopTitle(R.string.order_refund);
                bean.setType(3);
                break;

            case Constants.Code.REQUEST_ORDER_ALL:
                showTopTitle(R.string.order_all);
                bean.setType(4);
                break;

            default:
                break;
        }
        data.add(bean);
        lv_order = (ListView) findViewById(R.id.lv_order);

        adapter = new OrderAdapter(this, data);
        lv_order.setAdapter(adapter);
        lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra(Constants.Char.ORDER_TYPE,
                        type == Constants.Code.REQUEST_ORDER_ALL ?
                                Constants.Code.REQUEST_ORDER_COMPLETE : type);
                startActivity(intent);
            }
        });
    }
}
