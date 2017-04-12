package com.kswl.baimucai.activity.classify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
 * @desc 三级分类适配器
 * @date 2017-2017/1/19-16:31
 */
public class ClassifyThirdAdapter extends IBaseAdapter<ClassifyBean> {

    public ClassifyThirdAdapter(Context context, List<ClassifyBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classify_third,
                    null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        ImageView iv_third = ViewHolder.get(convertView, R.id.iv_third);

        ClassifyBean bean = getItem(position);
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_third);
        tv_name.setText(bean.getName());
        final String classifyId = bean.getId();
        final String classifyName = bean.getName();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsListActivity.class);
                intent.putExtra(Constants.Char.CLASSIFY_ID, classifyId);
                intent.putExtra(Constants.Char.CLASSIFY_NAME, classifyName);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
