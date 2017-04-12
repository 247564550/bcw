package com.kswl.baimucai.activity.search;

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
import com.kswl.baimucai.bean.ShopBean;
import com.kswl.baimucai.utils.Tools;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 搜索店铺适配器
 * @date 2017-2017/1/19-15:20
 */
public class SearchShopAdapter extends IBaseAdapter<ShopBean> {

    public SearchShopAdapter(Context context, List<ShopBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_shop, null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_vip = ViewHolder.get(convertView, R.id.tv_vip);
        TextView tv_collect = ViewHolder.get(convertView, R.id.tv_collect);

        ShopBean bean = getItem(position);
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(bean.getName());
        tv_vip.setText(Tools.isNull(bean.getShopLevel()) ? "0" : bean.getShopLevel());
        tv_collect.setText(Tools.isNull(bean.getCareNum()) ? "0" : bean.getCareNum());

        return convertView;
    }
}
