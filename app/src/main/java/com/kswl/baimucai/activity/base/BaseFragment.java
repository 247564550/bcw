package com.kswl.baimucai.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.interfaces.OnFragmentInteractionListener;

/**
 * @author wangjie
 * @desc Fragment基类
 * @date 2017/1/16 19:08
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mContext;

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mContext = (BaseActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        mContext.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_alpha_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        mContext.overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_alpha_out);
    }

    /**
     * @param view
     * @param resTitleId     标题id
     * @param resLeftImgId   左边返回键图片，可以不设置默认返回
     * @param resRightImgId  右边图片按钮
     * @param resRightTextId 右边文件按钮
     * @return
     * @author wangjie
     * @date 2017/1/17 16:44
     * @desc 显示Fragment公共标题
     */
    protected View showTopTitle(View view, int resTitleId, int resLeftImgId, int resRightImgId,
                                int resRightTextId) {
        return showTopTitle(view, resTitleId > 0 ? getString(resTitleId) : "", resLeftImgId,
                resRightImgId, resRightTextId > 0 ? getString(resRightTextId) : "");
    }

    /**
     * @param view
     * @param title         标题
     * @param resLeftImgId  左边返回键图片，可以不设置默认返回
     * @param resRightImgId 右边图片按钮
     * @param rightText     右边文件按钮
     * @return
     * @author wangjie
     * @date 2017/1/17 16:44
     * @desc 显示Fragment公共标题
     */
    protected View showTopTitle(View view, String title, int resLeftImgId, int resRightImgId,
                                String rightText) {
        View parentView = mContext.getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout fl_contain = (FrameLayout) parentView.findViewById(R.id.fl_contain);
        ImageView iv_left_btn = (ImageView) parentView.findViewById(R.id.iv_left_btn);
        ImageView iv_right_btn = (ImageView) parentView.findViewById(R.id.iv_right_btn);
        TextView tv_top_title = (TextView) parentView.findViewById(R.id.tv_top_title);
        TextView tv_right_btn = (TextView) parentView.findViewById(R.id.tv_right_btn);
        RelativeLayout rl_top = (RelativeLayout) parentView.findViewById(R.id.rl_top);

        if (!TextUtils.isEmpty(title)) {
            tv_top_title.setVisibility(View.VISIBLE);
            tv_top_title.setText(title);
        }
        if (!TextUtils.isEmpty(rightText)) {
            tv_right_btn.setVisibility(View.VISIBLE);
            tv_right_btn.setText(rightText);
            tv_right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRightTextBtnClick();
                }
            });
        }
        if (resLeftImgId > 0) {
            iv_left_btn.setVisibility(View.VISIBLE);
            iv_left_btn.setImageResource(resLeftImgId);
        }
        if (resRightImgId > 0) {
            iv_right_btn.setVisibility(View.VISIBLE);
            iv_right_btn.setImageResource(resRightImgId);
            iv_right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRightImgBtnClick();
                }
            });
        }

        iv_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeftBtnClick();
            }
        });

        fl_contain.addView(view);
        return parentView;
    }


    /**
     * 标题栏左边按钮点击事件
     */
    public void onLeftBtnClick() {
        finishFragment();
    }

    /**
     * 标题栏右边图片按钮点击事件
     */
    public void onRightImgBtnClick() {

    }

    /**
     * 标题栏右边文字按钮点击事件
     */
    public void onRightTextBtnClick() {

    }

    /**
     * @return 是否拦截点击事件
     * @author wangjie
     * @date 2017/1/17 14:17
     * @desc 手机返回键点击事件
     */
    public boolean onFragmentBackPressed() {
        return false;
    }

    public void startFragment(BaseFragment fragment) {
        mListener.onFragmentStart(fragment);
    }

    public void startFragmentForResult(BaseFragment fragment, int resultCode) {
        mListener.onFragmentStartForResult(fragment, resultCode);
    }

    public void finishFragment() {
        mListener.onFragmentFinish();
    }

    public void setFragmentResult(int resultCode) {
        if (null != getTargetFragment()) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, new Intent());
        }
    }

    public void setFragmentResult(int resultCode, Intent data) {
        if (null != getTargetFragment()) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, data);
        }
    }
}
