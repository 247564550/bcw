package com.kswl.baimucai.utils;

import android.util.Log;

/**
 * 自定义日志类 [一句话功能简述] [功能详细描述]
 *
 * @version [版本号, 2013-5-13]
 * @作者 mWX160443
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class LogUtil {

    /**
     * 是否打开LOG
     */
    public static boolean isLogEnabled = true;

    /**
     * LOG默认TAG
     */
    private static String defaultTag = "Log";

    /**
     *
     */
    private static final String TAG_CONTENT_PRINT = "%s:%s.%s:%d";


    private static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];

    }


    public static void trace() {
        if (isLogEnabled) {
            Log.d(defaultTag,
                    getContent(getCurrentStackTraceElement()));
        }
    }


    private static String getContent(StackTraceElement trace) {
        return String.format(TAG_CONTENT_PRINT, defaultTag,
                trace.getClassName(), trace.getMethodName(),
                trace.getLineNumber());
    }


    public static void traceStack() {
        if (isLogEnabled) {
            traceStack(defaultTag, Log.ERROR);
        }
    }


    public static void traceStack(String tag, int priority) {

        if (isLogEnabled) {
            StackTraceElement[] stackTrace = Thread.currentThread()
                    .getStackTrace();
            Log.println(priority, tag, stackTrace[4].toString());
            StringBuilder str = new StringBuilder();
            String prevClass = null;
            for (int i = 5; i < stackTrace.length; i++) {
                String className = stackTrace[i].getFileName();
                int idx = className.indexOf(".java");
                if (idx >= 0) {
                    className = className.substring(0, idx);
                }
                if (prevClass == null || !prevClass.equals(className)) {

                    str.append(className.substring(0, idx));

                }
                prevClass = className;
                str.append(".").append(stackTrace[i].getMethodName())
                        .append(":").append(stackTrace[i].getLineNumber())
                        .append("->");
            }
            Log.println(priority, tag, str.toString());
        }
    }


    public static void d(String tag, String msg) {
        if (isLogEnabled) {
            Log.d(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }


    public static void d(String msg) {
        if (isLogEnabled) {
            Log.d(defaultTag, getContent(getCurrentStackTraceElement()) + ">"
                    + msg);
        }
    }


    public static void i(String tag, String msg) {
        if (isLogEnabled) {
            Log.i(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }


    public static void w(String tag, String msg) {
        if (isLogEnabled) {
            Log.w(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)警告级日志
     *
     * @param tag 日志标签
     * @param msg 日志消息
     * @return void
     * @author wangjie 2015-4-17 下午2:14:24
     */
    public static void e(String tag, String msg) {
        if (isLogEnabled) {
            Log.e(tag, getContent(getCurrentStackTraceElement()) + ">" + msg);
        }
    }

    /**
     * TODO(这里用一句话描述这个方法的作用) 警告级日志（使用默认标签）
     *
     * @param msg 日志消息
     * @return void
     * @author wangjie 2015-4-17 下午2:13:58
     */
    public static void e(String msg) {
        if (isLogEnabled) {
            Log.e(defaultTag, getContent(getCurrentStackTraceElement()) + ">"
                    + msg);
        }
    }
}
