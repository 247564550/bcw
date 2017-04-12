package com.kswl.baimucai.utils;

import android.content.Context;
import android.widget.Toast;

import com.kswl.baimucai.app.App;


/**
 * Copyright © 2014 蓝色互动. All rights reserved.
 *
 * @author wangjie
 * @Description Toast工具
 * @date 2014-11-10 下午3:39:50
 */
public class ToastUtil {

    public static Toast mToast;

    /**
     * @return void
     * @author 高骅
     * @Description 显示Toast
     * @date 2014-11-10 下午3:52:55
     */
    public static void showToast(Context context, String toastText, int duration) {
        if (mToast != null) {
            mToast.setText(toastText);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(context, toastText, duration);
            mToast.show();
        }
    }

    /**
     * @param context  上下文对象
     * @param resId    提示文字
     * @param duration 提示时间
     * @return void
     * @Description: TODO(这里用一句话描述这个方法的作用)显示Toast
     * @author wangjie
     * @date 2015-7-8 下午4:12:16
     */
    public static void showToast(Context context, int resId, int duration) {
        if (mToast != null) {
            mToast.setText(resId);
            mToast.setDuration(duration);
            mToast.show();
        } else {
            mToast = Toast.makeText(context, resId, duration);
            mToast.show();
        }
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)显示Toast，默认显示时间短
     *
     * @param context
     * @param toastText
     * @return void
     * @author wangj@bluemobi.sh.cn 2015-2-3 上午11:46:35
     */
    public static void showToast(Context context, String toastText) {
        if (mToast != null) {
            mToast.setText(toastText);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * @param context 上下文对象
     * @param resId   提示文字
     * @return void
     * @Description: TODO(这里用一句话描述这个方法的作用) 显示Toast，默认显示时间短
     * @author wangjie
     * @date 2015-7-8 下午4:13:41
     */
    public static void showToast(Context context, int resId) {
        if (mToast != null) {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)显示Toast
     *
     * @param toastText
     * @return void
     * @author wangj@bluemobi.sh.cn 2015-2-3 上午11:46:39
     */
    public static void showToast(String toastText) {
        if (mToast != null) {
            mToast.setText(toastText);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = Toast.makeText(App.app, toastText, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * @param resId 提示文字
     * @return void
     * @Description: TODO(这里用一句话描述这个方法的作用) 显示Toast
     * @author wangjie
     * @date 2015-7-8 下午4:14:05
     */
    public static void showToast(int resId) {
        if (mToast != null) {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast = Toast.makeText(App.app, resId, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
