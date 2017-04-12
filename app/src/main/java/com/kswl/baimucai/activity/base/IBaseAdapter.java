package com.kswl.baimucai.activity.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/*
 * Author: Administrator Email:wangj@bluemobi.sh.cn
 * Created Date:2015-2-3
 * Copyright @ 2015 BU
 * Description: 类描述
 *
 * History:
 */
public class IBaseAdapter<T> extends BaseAdapter {

	public Context mContext;

	protected List<T> data;

	public IBaseAdapter(Context context, List<T> data) {
		this.mContext = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == data ? 0 : data.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return convertView;
	}

}
