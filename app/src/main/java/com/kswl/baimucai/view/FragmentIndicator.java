package com.kswl.baimucai.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;


/**
 * @author wangjie
 * @package com.kswl.port.view
 * @desc
 * @date 2016-2016/11/18-13:53
 */

public class FragmentIndicator extends LinearLayout implements View.OnClickListener {

    /**
     * 底部按钮文字集合
     */
    private String[] textArr;

    /**
     * 未选中颜色、选中颜色
     */
    private int colorUnselect, colorSelect;

    /**
     * 选中图片
     */
    private static int[] imgSelectArr = {R.drawable.main_home_select_icon,
            R.drawable.main_classify_select_icon, R.drawable.main_tender_select_icon,
            R.drawable.main_shopping_select_icon, R.drawable.main_own_select_icon};

    /**
     * 未选中图片
     */
    private static int[] imgUnSelectArr = {R.drawable.main_home_unselect_icon,
            R.drawable.main_classify_unselect_icon, R.drawable.main_tender_unselect_icon,
            R.drawable.main_shopping_unselect_icon, R.drawable.main_own_unselect_icon};

    /**
     * 默认的选择项
     */
    private int mDefaultIndicator = -1;

    /**
     * 当前游标位置
     */
    private static int mCurIndicator;

    /**
     * 选项卡集合
     */
    public static LinearLayout[] mIndicators;

    /**
     * 选项卡变动监听
     */
    private static OnIndicateListener mOnIndicateListener;

    /**
     * 选项卡选中回调接口
     */
    public interface OnIndicateListener {
        /**
         * @param which 选中项
         * @return 是否拦截本次点击事件
         */
        public boolean onIndicate(int which);
    }

    /**
     * 设置选项卡点击回调时间
     *
     * @param listener 回到函数
     */
    public void setOnIndicateListener(OnIndicateListener listener) {
        mOnIndicateListener = listener;
    }

    public FragmentIndicator(Context context) {
        super(context);
        init(context);
    }

    public FragmentIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FragmentIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        textArr = context.getResources().getStringArray(R.array.main_sort);
        colorSelect = context.getResources().getColor(R.color.text_orange);
        colorUnselect = context.getResources().getColor(R.color.text_dark_gray);
        mCurIndicator = mDefaultIndicator;
        setOrientation(LinearLayout.HORIZONTAL);

        mIndicators = new LinearLayout[textArr.length];
        for (int i = 0; i < textArr.length; i++) {
            mIndicators[i] =
                    createIndicator(i == mCurIndicator ? imgSelectArr[i] : imgUnSelectArr[i],
                            textArr[i],
                            i == mCurIndicator ? colorSelect : colorUnselect);
            mIndicators[i].setTag(Integer.valueOf(i));
            mIndicators[i].setOnClickListener(this);
            addView(mIndicators[i]);
        }
    }

    /**
     * 创建选项卡
     *
     * @param resIcon  选项卡图片
     * @param textStr  选项卡文字
     * @param strColor 选项卡文字颜色
     * @return
     */
    private LinearLayout createIndicator(int resIcon, String textStr, int strColor) {
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT, 1));
        view.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView iconView = new ImageView(getContext());
        iconView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT, 1));
        iconView.setPadding(0, 8, 0, 0);
        iconView.setImageResource(resIcon);

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setPadding(0, 0, 0, 5);
        textView.setTextColor(strColor);
        textView.setText(textStr);

        view.addView(iconView);
        view.addView(textView);
        return view;
    }

    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        setCheckTag(tag);
    }

    /**
     * 设置选中项
     *
     * @param which 选中项
     */
    private void setIndicator(int which) {

        if (mCurIndicator >= 0 && mCurIndicator < mIndicators.length) {
            ImageView iconCurView = (ImageView) mIndicators[mCurIndicator].getChildAt(0);
            TextView textCurView = (TextView) mIndicators[mCurIndicator].getChildAt(1);
            iconCurView.setImageResource(imgUnSelectArr[mCurIndicator]);
            textCurView.setTextColor(colorUnselect);
        }

        if (which >= 0 && which < mIndicators.length) {
            ImageView iconWhichView = (ImageView) mIndicators[which].getChildAt(0);
            TextView textWhichView = (TextView) mIndicators[which].getChildAt(1);
            iconWhichView.setImageResource(imgSelectArr[which]);
            textWhichView.setTextColor(colorSelect);
        }

        mCurIndicator = which;
    }

    public void setCheckTag(int checkTag) {
        if (mOnIndicateListener != null && mOnIndicateListener.onIndicate(checkTag)) {
            return;
        }
        if (mCurIndicator != checkTag) {
            setIndicator(checkTag);
        }
    }
}
