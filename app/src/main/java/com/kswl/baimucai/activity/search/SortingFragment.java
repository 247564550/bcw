package com.kswl.baimucai.activity.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;

/**
 * @author wangjie
 * @desc 筛选
 * @date 2017/2/13 9:06
 */
public class SortingFragment extends Fragment implements View.OnClickListener {

    /**
     * 综合排序
     */
    public static final String SORTING_DEFAULT = "buyNum DESC,starLevel DESC,unitPrice ASC";

    /**
     * 销量排序
     */
    public static final String SORTING_SALES = "buyNum DESC";

    /**
     * 价格升序排序
     */
    public static final String SORTING_PRICE_ASC = "unitPrice ASC";

    /**
     * 价格降序排序
     */
    public static final String SORTING_PRICE_DESC = "unitPrice DESC";

    /**
     * 评分排序
     */
    public static final String SORTING_SCORE = "starLevel DESC";

    private OnFragmentFilterListener mListener;

    private Context mContext;

    /**
     * 排序方式
     * tv_sorting_comprehensive 综合<br>
     * tv_sorting_sales 销量<br>
     * tv_sorting_price 价格<br>
     * tv_sorting_score  评分<br>
     */
    TextView tv_sorting_comprehensive, tv_sorting_sales, tv_sorting_price, tv_sorting_score;

    /**
     * 0: 综合排序<br>
     * 1：销量<br>
     * 2：价格升序<br>
     * 3：价格降序<br>
     * 4：评分<br>
     */
    private String sortType = SORTING_DEFAULT;

    public SortingFragment() {
    }

    public static SortingFragment newInstance(String param1, String param2) {
        SortingFragment fragment = new SortingFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentFilterListener) {
            mListener = (OnFragmentFilterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentFilterListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting, container, false);
        tv_sorting_comprehensive = (TextView) view.findViewById(R.id.tv_sorting_comprehensive);
        tv_sorting_sales = (TextView) view.findViewById(R.id.tv_sorting_sales);
        tv_sorting_price = (TextView) view.findViewById(R.id.tv_sorting_price);
        tv_sorting_score = (TextView) view.findViewById(R.id.tv_sorting_score);
        TextView tv_filter = (TextView) view.findViewById(R.id.tv_filter);
        LinearLayout ll_sorting_price = (LinearLayout) view.findViewById(R.id.ll_sorting_price);

        tv_sorting_comprehensive.setOnClickListener(this);
        tv_sorting_sales.setOnClickListener(this);
        ll_sorting_price.setOnClickListener(this);
        tv_sorting_score.setOnClickListener(this);
        tv_filter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sorting_comprehensive:
                // 综合排序
                if (sortType != SORTING_DEFAULT) {
                    tv_sorting_comprehensive.setTextColor(mContext.getResources().getColor(
                            R.color.text_orange));
                    tv_sorting_sales.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_price.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_score.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_price.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.price_normal_icon, 0);
                    sortType = SORTING_DEFAULT;
                    mListener.onSorting(sortType);
                }
                break;

            case R.id.tv_sorting_sales:
                // 销量
                if (sortType != SORTING_SALES) {
                    tv_sorting_comprehensive.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_sales.setTextColor(mContext.getResources().getColor(
                            R.color.text_orange));
                    tv_sorting_price.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_score.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_price.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.price_normal_icon, 0);
                    sortType = SORTING_SALES;
                    mListener.onSorting(sortType);
                }
                break;

            case R.id.ll_sorting_price:
                // 价格排序
                if (sortType == SORTING_PRICE_ASC) {
                    tv_sorting_price.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.price_dsc_icon, 0);
                    sortType = SORTING_PRICE_DESC;
                } else if (sortType == SORTING_PRICE_DESC) {
                    tv_sorting_price.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.price_asc_icon, 0);
                    sortType = SORTING_PRICE_ASC;
                } else {
                    tv_sorting_comprehensive.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_sales.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_price.setTextColor(mContext.getResources().getColor(
                            R.color.text_orange));
                    tv_sorting_score.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_price.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.price_asc_icon, 0);
                    sortType = SORTING_PRICE_ASC;
                }
                mListener.onSorting(sortType);
                break;

            case R.id.tv_sorting_score:
                // 评分排序
                if (sortType != SORTING_SCORE) {
                    tv_sorting_comprehensive.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_sales.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_price.setTextColor(mContext.getResources().getColor(
                            R.color.text_dark_gray));
                    tv_sorting_score.setTextColor(mContext.getResources().getColor(
                            R.color.text_orange));
                    tv_sorting_price.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.price_normal_icon, 0);
                    sortType = SORTING_SCORE;
                    mListener.onSorting(sortType);
                }
                break;

            case R.id.tv_filter:
                // 筛选
                mListener.onFilter();
                break;

            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentFilterListener {
        /**
         * @desc 排序
         * @author wangjie
         * @date 2017/2/13 13:16
         */
        void onSorting(String sortord);

        /**
         * @desc 筛选
         * @author wangjie
         * @date 2017/2/13 13:17
         */
        void onFilter();
    }
}
