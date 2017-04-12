package com.kswl.baimucai.activity.tender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.FilterBean;
import com.kswl.baimucai.utils.Tools;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class TenderFilterAdapter extends IBaseAdapter<FilterBean> {

    public TenderFilterAdapter(Context context, List<FilterBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tender_filter, null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_value = ViewHolder.get(convertView, R.id.tv_value);
        FilterBean bean = getItem(position);
        if (!Tools.isNull(bean.getName())) {
            tv_name.setText(bean.getName());
            tv_value.setVisibility(View.VISIBLE);
            tv_value.setText(bean.getValue());
        } else {
            tv_name.setText(bean.getValue());
            tv_value.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
}
