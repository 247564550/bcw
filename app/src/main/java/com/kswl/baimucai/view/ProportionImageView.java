package com.kswl.baimucai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.kswl.baimucai.R;

/**
 * @author wangjie
 * @package com.kswl.ggt.shipper.view
 * @desc
 * @date 2016-2016/12/15-17:12
 */

public class ProportionImageView extends ImageView {

    private final static int default_w_h = 0;

    /**
     * 宽高比：宽
     */
    private int p_w;

    /**
     * 宽高比：高
     */
    private int p_h;

    public ProportionImageView(Context context) {
        this(context, null);
    }

    public ProportionImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
            int childHeightSize = getMeasuredHeight();
//            Log.e("onMeasure", "onMeasure: " + childWidthSize + "-" + childHeightSize);
            // 高度和宽度一样
            if (childWidthSize == 0) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize * p_w / p_h,
                        MeasureSpec.EXACTLY);
            } else {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                        MeasureSpec.EXACTLY);
            }
            if (childHeightSize == 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * p_h / p_w,
                        MeasureSpec.EXACTLY);
            } else {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
                        MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
