package com.kswl.baimucai.activity.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.CityBean;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class AddressFilterAdapter extends IBaseAdapter<CityBean> {

    private int selectPosition = -1;

    public AddressFilterAdapter(Context context, List<CityBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_filter, null);
        }
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        ImageView iv_select = ViewHolder.get(convertView, R.id.iv_select);
        CityBean bean = getItem(position);
        tv_title.setText(bean.getName());
        if (null == bean.getChild()) {
            if (selectPosition == position) {
                iv_select.setVisibility(View.VISIBLE);
                iv_select.setImageResource(R.drawable.blue_tick_icon);
            } else {
                iv_select.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
