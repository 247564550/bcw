package com.kswl.baimucai.activity.classify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.activity.goods.GoodsListActivity;
import com.kswl.baimucai.bean.ClassifyBean;
import com.kswl.baimucai.utils.Constants;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 二级分类适配器
 * @date 2017-2017/1/19-15:20
 */
public class ClassifyChildAdapter extends IBaseAdapter<ClassifyBean> {

    public ClassifyChildAdapter(Context context, List<ClassifyBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classify_child,
                    null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        GridView gv_third = ViewHolder.get(convertView, R.id.gv_third);

        ClassifyBean bean = getItem(position);
        tv_name.setText(bean.getName());
        gv_third.setAdapter(new ClassifyThirdAdapter(mContext, bean.getMdseTypeDtoList()));
        return convertView;
    }

}
