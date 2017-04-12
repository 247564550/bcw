package com.kswl.baimucai.activity.tender;

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
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.TenderBean;
import com.kswl.baimucai.utils.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class TenderAdapter extends IBaseAdapter<TenderBean> {

    Map<String, String> statusMap;

    public TenderAdapter(Context context, List<TenderBean> data) {
        super(context, data);
        String[] statusKey = context.getResources().getStringArray(R.array.tender_status_key);
        String[] statusValue = context.getResources().getStringArray(R.array.tender_status_value);
        statusMap = new HashMap<>();
        for (int i = 0; i < statusKey.length; i++) {
            statusMap.put(statusKey[i], statusValue[i]);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tender, null);
        }
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_area = ViewHolder.get(convertView, R.id.tv_area);
        TextView tv_state = ViewHolder.get(convertView, R.id.tv_state);
        TextView tv_date = ViewHolder.get(convertView, R.id.tv_date);

        TenderBean bean = getItem(position);
        Glide.with(App.app).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(bean.getName());
        tv_area.setText(bean.getAreaName());
        tv_date.setText(Tools.formatDate(bean.getBeginDate(), "yyyy-MM"));
        tv_state.setText(statusMap.get(bean.getTenderStatus()));

        return convertView;
    }
}
