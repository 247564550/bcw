package com.kswl.baimucai.activity.goods;

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
public class AppraiseAdapter extends IBaseAdapter<String> {

    @Override
    public int getCount() {
        return 10;
    }

    public AppraiseAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_appraise, null);
        }
        return convertView;
    }

}
