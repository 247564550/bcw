package com.kswl.baimucai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/*
 * Author: wangjie Email:wangj@bluemobi.sh.cn
 * Created Date:2014-9-12
 * Copyright @ 2014 BU
 * Description: 类描述
 *	自定义GridView，高度随内容变化,禁止滑动
 * History:
 */
public class CustomGridView extends GridView {

    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
