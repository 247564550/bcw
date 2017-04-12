package com.kswl.baimucai.interfaces;

import android.support.annotation.IdRes;

import com.kswl.baimucai.activity.base.BaseFragment;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc
 * @date 2017-2017/1/16-19:01
 */
public interface OnFragmentInteractionListener {

    /**
     * @author wangjie
     * @date 2017/1/17 14:58
     * @desc 启动新的Fragment
     */
    void onFragmentStart(BaseFragment fragment);

    /**
     * @author wangjie
     * @date 2017/1/17 14:58
     * @desc 启动新的Fragment
     */
    void onFragmentStartForResult(BaseFragment fragment, int requestCode);

    /**
     * @author wangjie
     * @date 2017/1/17 14:58
     * @desc 结束当前Fragment
     */
    void onFragmentFinish();

}
