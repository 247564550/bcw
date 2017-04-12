package com.kswl.baimucai.activity.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.interfaces.OnViewPagerItemClickListener;

import java.util.ArrayList;

/**
 * @author wangjie
 * @date 2017/1/18 16:42
 * @desc banner图片适配器
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList urls;

    private ImageView[] mViews;

    OnViewPagerItemClickListener itemClickListener;

    public ImagePagerAdapter(Context mContext, ArrayList urls) {
        this(mContext, urls, null);
    }

    public ImagePagerAdapter(Context mContext, ArrayList urls,
                             OnViewPagerItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.urls = urls;
        this.mViews = new ImageView[null == urls ? 0 : urls.size()];
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return null == urls ? 0 : urls.size();
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        ImageView imageView = mViews[position];
        if (imageView == null) {
            imageView = new ImageView(mContext);
            mViews[position] = imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Glide.with(mContext)
                .load(urls.get(position).toString())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(imageView);
        if (null != itemClickListener) {
            final int curPosition = position;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onClick(mViews[curPosition], curPosition);
                }
            });
        }
        ((ViewPager) view).addView(imageView, 0);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
