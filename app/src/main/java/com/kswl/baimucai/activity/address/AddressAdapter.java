package com.kswl.baimucai.activity.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.AddressBean;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.account
 * @desc
 * @date 2017-2017/2/9-14:29
 */

public class AddressAdapter extends IBaseAdapter<AddressBean> {

    OnItemIndicateListener mListener;

    public AddressAdapter(Context context, List<AddressBean> data, OnItemIndicateListener
            mListener) {
        super(context, data);
        this.mListener = mListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address,
                    null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_phone = ViewHolder.get(convertView, R.id.tv_phone);
        TextView tv_address = ViewHolder.get(convertView, R.id.tv_address);
        TextView tv_default = ViewHolder.get(convertView, R.id.tv_default);
        ImageView iv_delete = ViewHolder.get(convertView, R.id.iv_delete);
        ImageView iv_edit = ViewHolder.get(convertView, R.id.iv_edit);

        AddressBean bean = getItem(position);
        tv_name.setText(bean.getName());
        tv_phone.setText(bean.getPhone());
        tv_address.setText(bean.getProvince() + bean.getCity() + bean.getArea() + bean.getAddress
                ());
        if ("1".equals(bean.getIsDefault())) {
            tv_default.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.oval_checked_icon, 0,
                    0, 0);
        } else {
            tv_default.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.oval_unchecked_icon, 0,
                    0, 0);
        }

        final int selectPosition = position;
        tv_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDefaultClick(selectPosition);
                }
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDeleteClick(selectPosition);
                }
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onEditClick(selectPosition);
                }
            }
        });
        return convertView;
    }

    public interface OnItemIndicateListener {

        /**
         * 删除按钮点击事件
         */
        void onDeleteClick(int position);

        /**
         * 编辑按钮点击事件
         */
        void onEditClick(int position);

        /**
         * 默认地址按钮点击事件
         */
        void onDefaultClick(int position);
    }
}
