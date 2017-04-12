package com.kswl.baimucai.activity.goods;

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
import com.kswl.baimucai.bean.GoodsRecommendBean;

import java.util.List;

/**
 * @author wangjie
 * @desc 活动-商品
 * @date 2017/2/10 12:03
 */
public class RecommendGoodsAdapter extends IBaseAdapter<GoodsRecommendBean> {

    String shopName;

    public RecommendGoodsAdapter(Context context, List<GoodsRecommendBean> data, String shopName) {
        super(context, data);
        this.shopName = shopName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_goods,
                    null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_shop = ViewHolder.get(convertView, R.id.tv_shop);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);

        GoodsRecommendBean bean = getItem(position);
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(bean.getName());
        tv_shop.setText(shopName);
        tv_price.setText("¥ " + bean.getPrice());
        tv_count.setText(String.format(mContext.getResources().getString(R.string
                .goods_comment_count), bean.getAssessCount()));
        return convertView;
    }
}
