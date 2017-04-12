package com.kswl.baimucai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kswl.baimucai.R;

/**
 * @author wangjie
 * @package com.kswl.ggt.shipper.view
 * @desc
 * @date 2016-2016/11/29-13:48
 */

public class ProportionRelativeLayout extends RelativeLayout {

    private final static int default_w_h = 0;

    /**
     * 宽高比：宽
     */
    private int p_w;

    /**
     * 宽高比：高
     */
    private int p_h;

    public ProportionRelativeLayout(Context context) {
        this(context, null);
    }

    public ProportionRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Proportion, defStyleAttr, 0);
        p_w = a.getInt(R.styleable.Proportion_proportion_width, default_w_h);
        p_h = a.getInt(R.styleable.Proportion_proportion_height, default_w_h);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (p_w != default_w_h && p_h != default_w_h) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                    getDefaultSize(0, heightMeasureSpec));

            // Children are just made to fill our space.
            int childWidthSize = getMeasuredWidth();
            // int childHeightSize = getMeasuredHeight();
            // 高度和宽度一样
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                    MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize
                    * p_h / p_w, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
