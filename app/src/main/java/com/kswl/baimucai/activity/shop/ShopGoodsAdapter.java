package com.kswl.baimucai.activity.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.GoodsBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.Tools;

import java.util.List;

/**
 * @author wangjie
 * @desc 活动-商品
 * @date 2017/2/10 12:03
 */
public class ShopGoodsAdapter extends IBaseAdapter<GoodsBean> {

    public ShopGoodsAdapter(Context context, List<GoodsBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_goods,
                    null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);

        GoodsBean bean = getItem(position);
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(bean.getName());
        tv_price.setText("¥ " + Tools.formatMoney(bean.getUnitPrice()));

        return convertView;
    }

}
