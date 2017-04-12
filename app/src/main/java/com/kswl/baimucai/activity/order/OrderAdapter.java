package com.kswl.baimucai.activity.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.OrderBean;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.account
 * @desc
 * @date 2017-2017/2/9-14:29
 */

public class OrderAdapter extends IBaseAdapter<OrderBean> {

    public OrderAdapter(Context context, List<OrderBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order,
                    null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_shop = ViewHolder.get(convertView, R.id.tv_shop);
        TextView tv_state = ViewHolder.get(convertView, R.id.tv_state);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_model = ViewHolder.get(convertView, R.id.tv_model);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
        TextView tv_freight = ViewHolder.get(convertView, R.id.tv_freight);
        RelativeLayout rl_btn = ViewHolder.get(convertView, R.id.rl_btn);
        TextView tv_1 = ViewHolder.get(convertView, R.id.tv_1);
        TextView tv_2 = ViewHolder.get(convertView, R.id.tv_2);
        TextView tv_3 = ViewHolder.get(convertView, R.id.tv_3);

        OrderBean bean = getItem(position);
        switch (bean.getType()) {
            case 0:
                // 待付款
                tv_state.setText(mContext.getString(R.string.order_payment));
                tv_3.setText(mContext.getString(R.string.order_btn_payment));
                tv_2.setText(mContext.getString(R.string.order_btn_cancel));
                tv_1.setVisibility(View.GONE);
                break;

            case 1:
                // 待发货
                tv_state.setText(mContext.getString(R.string.order_send));
                tv_3.setText(mContext.getString(R.string.order_btn_remind));
                tv_2.setText(mContext.getString(R.string.order_btn_refund));
                tv_1.setVisibility(View.GONE);
                break;

            case 2:
                // 待收货
                tv_state.setText(mContext.getString(R.string.order_receive));
                tv_3.setText(mContext.getString(R.string.order_btn_confirm));
                tv_2.setText(mContext.getString(R.string.order_btn_logistics));
                tv_1.setText(mContext.getString(R.string.order_btn_refund));
                break;

            case 3:
                // 退货订单
                tv_state.setText(mContext.getString(R.string.order_refund));
                rl_btn.setVisibility(View.GONE);
                break;

            case 4:
                // 完成订单
                tv_state.setText(mContext.getString(R.string.order_complete));
                tv_3.setText(mContext.getString(R.string.order_btn_appraise));
                tv_2.setText(mContext.getString(R.string.order_btn_delete));
                tv_1.setVisibility(View.GONE);
                break;

            default:
                break;
        }

        return convertView;
    }

}
