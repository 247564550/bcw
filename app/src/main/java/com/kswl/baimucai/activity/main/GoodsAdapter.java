package com.kswl.baimucai.activity.main;

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
import com.kswl.baimucai.utils.Tools;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class GoodsAdapter extends IBaseAdapter<GoodsBean> {

    public GoodsAdapter(Context context, List<GoodsBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods, null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_shop = ViewHolder.get(convertView, R.id.tv_shop);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);

        GoodsBean bean = getItem(position);
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(bean.getName());
        tv_shop.setText(bean.getShopName());
        tv_price.setText("¥ " + Tools.formatMoney(bean.getUnitPrice()));
        tv_count.setText(String.format(mContext.getResources().getString(R.string
                .goods_comment_count), bean.getAssessNum()));

        return convertView;
    }
}
