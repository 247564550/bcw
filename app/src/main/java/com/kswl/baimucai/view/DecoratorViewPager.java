package com.kswl.baimucai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author wangjie
 * @date 2017/1/18 16:13
 * @desc
 */
public class DecoratorViewPager extends ViewPager {

    public DecoratorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DecoratorViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    // private float start_x, start_y;
    //
    // @Override
    // public boolean onTouchEvent(MotionEvent ev) {
    //
    // switch (ev.getAction()) {
    // case MotionEvent.ACTION_DOWN:
    // start_x = ev.getX();
    // start_y = ev.getY();
    // getParent().requestDisallowInterceptTouchEvent(true);
    // break;
    //
    // case MotionEvent.ACTION_MOVE:
    // float cur_x = ev.getX();
    // float cur_y = ev.getY();
    // if (Math.abs(cur_x - start_x) > Math.abs(cur_y - start_y)) {
    // getParent().requestDisallowInterceptTouchEvent(true);
    // } else {
    // getParent().requestDisallowInterceptTouchEvent(false);
    // }
    // break;
    //
    // case MotionEvent.ACTION_UP:
    // getParent().requestDisallowInterceptTouchEvent(false);
    // break;
    //
    // default:
    // break;
    // }
    // return super.onTouchEvent(ev);
    // }

}
