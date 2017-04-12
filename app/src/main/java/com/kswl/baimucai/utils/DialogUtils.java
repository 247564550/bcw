package com.kswl.baimucai.utils;

import android.app.ProgressDialog;
import android.content.Context;

/*
 * Author: Administrator Email:wangj@bluemobi.sh.cn
 * Created Date:2015-3-11
 * Copyright @ 2015 BU
 * Description: 类描述
 *	自定义样式进度条
 * History:
 */
public class DialogUtils {

	public static final String EMPTY_MSG = null;

	public static final String DEFAULT_MSG = "加载中...";

	private static DialogUtils instance;

	/**
	 * 加载提示框
	 */
	ProgressDialog progressDialog;

	private DialogUtils() {
	};

	public static DialogUtils getInstance() {
		if (null == instance) {
			instance = new DialogUtils();
		}
		return instance;
	}

	/**
	 * 
	 * TODO(这里用一句话描述这个方法的作用)展示自定义样式ProgressDialog，不带标题
	 * 
	 * @author wangj@bluemobi.sh.cn 2015-3-11 下午3:02:07
	 * @param context
	 *            上下文对象
	 * @param msg
	 *            消息内容
	 * @param isCancel
	 *            是否可以按退回按键取消
	 * @return void
	 */
	public void show(Context context, String msg, boolean isCancel) {
		if (null == progressDialog) {
			progressDialog = ProgressDialog.show(context, null, msg, false,
					isCancel);
		} else {
			progressDialog.setMessage(msg);
			progressDialog.setCancelable(isCancel);
		}
		progressDialog.show();
	}

	/**
	 * 
	 * TODO(这里用一句话描述这个方法的作用)展示自定义样式ProgressDialog，不带标题
	 * 
	 * @author wangj@bluemobi.sh.cn 2015-3-11 下午3:02:07
	 * @param context
	 *            上下文对象
	 * @param msg
	 *            消息内容
	 * @return void
	 */
	public void show(Context context, String msg) {
		if (null == progressDialog) {
			progressDialog = ProgressDialog.show(context, null, msg);
		} else {
			progressDialog.setMessage(msg);
		}
		progressDialog.show();
	}

	/**
	 * 
	 * TODO(这里用一句话描述这个方法的作用)展示自定义样式ProgressDialog，不带标题
	 * 
	 * @author wangj@bluemobi.sh.cn 2015-3-11 下午3:02:07
	 * @param context
	 *            上下文对象
	 * @return void
	 */
	public void show(Context context) {
		if (null == progressDialog) {
			progressDialog = ProgressDialog.show(context, null, DEFAULT_MSG);
		}
		progressDialog.show();
	}

	/**
	 * 
	 * TODO(这里用一句话描述这个方法的作用)取消进度条展示
	 * 
	 * @author wangj@bluemobi.sh.cn 2015-3-11 下午3:05:06
	 * @return void
	 */
	public void dismiss() {
		if (null != progressDialog) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
