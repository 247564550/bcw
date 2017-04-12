package com.kswl.baimucai.activity.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.ClassifyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class ShopFilterAdapter extends IBaseAdapter<ClassifyBean> {

    private int selectPosition = -1;

    private int type = 1;

    public ShopFilterAdapter(Context context, List<ClassifyBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_filter, null);
        }
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        ImageView iv_select = ViewHolder.get(convertView, R.id.iv_select);
        ClassifyBean bean = getItem(position);
        tv_title.setText(bean.getName());
        if (type == 3) {
            if (selectPosition == position) {
                iv_select.setVisibility(View.VISIBLE);
                iv_select.setImageResource(R.drawable.blue_tick_icon);
            } else {
                iv_select.setVisibility(View.GONE);
            }
        } else {
            iv_select.setVisibility(View.VISIBLE);
            iv_select.setImageResource(R.drawable.arrow_gray_right_icon);
        }
        return convertView;
    }

    public int getType() {
        return type;
    }

    public void setData(int type, List<ClassifyBean> mList) {
        this.type = type;
        if (null == data) {
            data = new ArrayList<>();
        }
        data.clear();
        if (null != mList) {
            data.addAll(mList);
        }
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
