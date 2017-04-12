package com.kswl.baimucai.activity.classify;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.ClassifyBean;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 一级分类适配器
 * @date 2017-2017/1/19-15:20
 */
public class ClassifyParentAdapter extends IBaseAdapter<ClassifyBean> {

    private int selectPosition = 0;

    public ClassifyParentAdapter(Context context, List<ClassifyBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classify_parent,
                    null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        ClassifyBean bean = getItem(position);
        tv_name.setText(bean.getName());
        if (selectPosition == position) {
            tv_name.setTextColor(mContext.getResources().getColor(R.color.text_orange));
            tv_name.setBackgroundResource(R.color.light_gray);
        } else {
            tv_name.setTextColor(mContext.getResources().getColor(R.color.text_black));
            tv_name.setBackgroundResource(R.color.white);
        }
        return convertView;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
