package com.kswl.baimucai.view;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.kswl.baimucai.utils.LogUtil;

/**
 * @author wangjie
 * @package com.kswl.baimucai.view
 * @desc 自定义侧滑删除
 * @date 2017-2017/1/20-15:11
 */

public class SwipeMenuLayout extends FrameLayout {

    /**
     * 菜单按钮关闭
     */
    private static final int STATE_CLOSE = 0;

    /**
     * 菜单按钮打开
     */
    private static final int STATE_SCROLLING = 1;

    /**
     * 菜单按钮打开
     */
    private static final int STATE_OPEN = 2;

    /**
     * 当前滑动状态
     */
    private int state = STATE_CLOSE;

    private View mContentView;

    private View mMenuView;

    /**
     * 滑动溢出距离，滑动距离小于mTouchSlop不拦截
     */
    private int mTouchSlop;

    /**
     * 是否可以滑动打开侧滑菜单
     */
    private boolean isSwipe = true;

    private int mDownX, mDownY;

    private int mBaseX;

    private ScrollerCompat mOpenScroller;

    private ScrollerCompat mCloseScroller;

    boolean isIntercept = false;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalStateException(
                    "The childCount of SwipeMenuLayout must be 2");
        }

        mMenuView = getChildAt(0);
        int menuWidth = mMenuView.getMeasuredWidth();

        mContentView = getChildAt(1);
        int contentWidth = mContentView.getMeasuredWidth();

        LogUtil.e("w-" + getMeasuredWidth() + "-" + menuWidth + "-" + contentWidth);
    }

    private void init() {
        // 滑动距离小于mTouchSlop不拦截
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mOpenScroller = ScrollerCompat.create(getContext());
        mCloseScroller = ScrollerCompat.create(getContext());
        setClickable(true);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        if (!isSwipe) {
//            return super.onInterceptTouchEvent(e);
//        }
//        int x = (int) e.getRawX();
//        int y = (int) e.getRawY();
//        LogUtil.e("onInterceptTouchEvent:" + x);
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // 首先拦截down事件,记录x坐标
//                mDownX = x;
//                mDownY = y;
//                LogUtil.e("onInterceptTouchEvent-ACTION_DOWN:" + mDownX);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                // deltaX > 0 是向右运动,< 0是向左运动
//                int deltaX = x - mDownX;
//                int deltaY = y - mDownY;
//                LogUtil.e("onInterceptTouchEvent-ACTION_MOVE:" + deltaX);
//                if (deltaX < 0 && Math.abs(deltaX) > mTouchSlop
//                        && Math.abs(deltaX) > Math.abs(deltaY)
//                        && mMenuView.getMeasuredWidth() > 0) {
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return false;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isSwipe) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        LogUtil.e("onTouchEvent:" + x);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                mDownX = x;
                mDownY = y;
                if (state == STATE_OPEN) {
                    isIntercept = true;
                    smoothCloseMenu();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("ACTION_MOVE:" + x);
                int disX = mDownX - x;
                int disY = mDownY - y;
                if ((state == STATE_CLOSE)
                        && (Math.abs(disX) > mTouchSlop || Math.abs(disY) > mTouchSlop)) {
                    if (disX > 0 && Math.abs(disX) > Math.abs(disY)
                            && mMenuView.getMeasuredWidth() > 0) {
                        isIntercept = true;
                        swipe(disX);
                    }
                    state = STATE_SCROLLING;
                } else if (state == STATE_SCROLLING && isIntercept) {
                    swipe(disX);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isIntercept = false;
                if (x - mDownX > mTouchSlop) {
                    // open
                    smoothOpenMenu();
                } else {
                    // close
                    smoothCloseMenu();
                }
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(isIntercept);
        return true;
}

    /**
     * 只有当前layout中的某个child导致scroll发生滚动，才会致使自己的computeScroll被调用
     */
    @Override
    public void computeScroll() {
        if (state == STATE_OPEN) {
            if (mOpenScroller.computeScrollOffset()) {
                swipe(mOpenScroller.getCurrX());
                postInvalidate();
            }
        } else {
            if (mCloseScroller.computeScrollOffset()) {
                swipe(mBaseX - mCloseScroller.getCurrX());
                postInvalidate();
            }
        }
    }

    /**
     * 滑动
     *
     * @param dis 滑动距离
     */
    private void swipe(int dis) {
        if (dis > mMenuView.getWidth()) {
            dis = mMenuView.getWidth();
        }
        if (dis < 0) {
            dis = 0;
        }
        mContentView.layout(-dis, mContentView.getTop(),
                mContentView.getWidth() - dis, getMeasuredHeight());

    }

    public void smoothCloseMenu() {
        state = STATE_CLOSE;
        mBaseX = -mContentView.getLeft();
        mCloseScroller.startScroll(0, 0, -mContentView.getLeft(), 0, 350);
        postInvalidate();
    }

    public void smoothOpenMenu() {
        state = STATE_OPEN;
        mOpenScroller.startScroll(-mContentView.getLeft(), 0,
                mMenuView.getWidth(), 0, 350);
        postInvalidate();
    }

    public void closeMenu() {
        if (mCloseScroller.computeScrollOffset()) {
            mCloseScroller.abortAnimation();
        }
        if (state == STATE_OPEN) {
            swipe(0);
            state = STATE_CLOSE;
        }
    }

    public void openMenu() {
        if (state == STATE_CLOSE) {
            swipe(mMenuView.getWidth());
            state = STATE_OPEN;
        }
    }

    public boolean isSwipe() {
        return isSwipe;
    }

    public void setSwipe(boolean swipe) {
        isSwipe = swipe;
    }
}
