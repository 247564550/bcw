package com.kswl.baimucai.activity.base;

import android.util.SparseArray;
import android.view.View;

/*
 * Author: Administrator Email:wangj@bluemobi.sh.cn
 * Created Date:2015-2-3
 * Copyright @ 2015 BU
 * Description: 类描述
 *	 通用ViewHolder
 * History:
 */
public class ViewHolder {

	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
