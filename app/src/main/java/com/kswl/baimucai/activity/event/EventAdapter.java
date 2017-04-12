package com.kswl.baimucai.activity.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.EventBean;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class EventAdapter extends IBaseAdapter<EventBean> {

    public EventAdapter(Context context, List<EventBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_event, null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        EventBean bean = getItem(position);
        Glide.with(mContext).load(bean.getImageApp())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        return convertView;
    }
}
