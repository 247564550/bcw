package com.kswl.baimucai.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kswl.baimucai.app.App;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangjie
 * @desc 工具类
 * @date 2017/1/16 15:47
 */
public class Tools {

    /**
     * 压缩图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }

    public static Bitmap getBitmapByScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 格式化价格
     *
     * @param argStr
     * @return
     */
    public static String getFloatDotStr(String argStr) {
        float arg = Float.valueOf(argStr);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(arg);
    }

    public static void Log(String s) {
        if (s == null) {
            s = "传进来的是null";
        }

        Log.i("info", s);
    }

    public static void Toast(Context context, String s) {
        // if (context == null)
        // context = ShiQiangApplication.getInstance().getApplicationContext();
        if (s != null) {
            android.widget.Toast.makeText(context, s,
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    public static void Toast(Context context, int res) {
        if (context != null) {
            Toast(context, context.getString(res));
        }
    }

    public static boolean IsHaveInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }

    // 得到versionName
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;

    }

    public static String millisToString(long millis) {
        boolean negative = millis < 0;
        millis = Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat
                .getInstance(Locale.US);
        format.applyPattern("00");
        if (millis > 0) {
            time = (negative ? "-" : "")
                    + (hours == 0 ? 00 : hours < 10 ? "0" + hours : hours)
                    + ":" + (min == 0 ? 00 : min < 10 ? "0" + min : min) + ":"
                    + (sec == 0 ? 00 : sec < 10 ? "0" + sec : sec);
        } else {
            time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

    // 得到versionName
    public static int getVerCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;

    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("")
                    || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
            return true;
        }

        return false;
    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(TextView... vv) {
        for (int i = 0; i < vv.length; i++) {
            if (null == vv[i] || Tools.isNull(Tools.getText(vv[i]))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param v
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(TextView v) {
        if (null == v || Tools.isNull(Tools.getText(v))) {
            return true;
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param v
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(EditText v) {
        if (null == v || Tools.isNull(Tools.getText(v))) {
            return true;
        }

        return false;
    }

    /**
     * 判断两个字段是否一样
     *
     * @author Michael.Zhang 2013-08-02 13:32:51
     */
    public static boolean judgeStringEquals(String s0, String s1) {
        return s0 != null && s0.equals(s1);
    }

    /**
     * 将dp类型的尺寸转换成px类型的尺寸
     *
     * @param size
     * @param context
     * @return
     */
    public static int DPtoPX(int size, Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return (int) ((float) size * metrics.density + 0.5);
    }

    /**
     * 屏幕宽高
     *
     * @param context
     * @return 0:width，1:height
     * @author TangWei 2013-11-5上午10:27:54
     */
    public static int[] ScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /**
     * double 整理
     *
     * @return
     */
    public static Double roundDouble(double val, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = ((0 == val) ? new BigDecimal("0.0") : new BigDecimal(
                Double.toString(val)));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(List... list) {
        for (int i = 0; i < list.length; i++) {
            if (isEmptyList(list[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(Object[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(Object[]... list) {
        for (int i = 0; i < list.length; i++) {
            if (isEmptyList(list[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断sd卡是否存在
     *
     * @return
     * @author Michael.Zhang 2013-07-04 11:30:54
     */
    public static boolean judgeSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断 http 链接
     *
     * @param url
     * @return
     * @author Michael.Zhang
     */
    public static boolean isUrl(String url) {
        return url != null && url.startsWith("http://");
    }

    /**
     * 获取保存到View的Tag中的字符串
     *
     * @param v
     * @return
     */
    public static String getTagString(View v) {
        try {
            return v.getTag().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取文本控件上显示的文字
     *
     * @param tv
     * @return
     * @author TangWei 2013-9-29下午2:40:52
     */
    public static String getText(TextView tv) {
        if (tv != null)
            return tv.getText().toString().trim();
        return "";
    }

    /**
     * 获取文本控件上显示的文字
     *
     * @param tv
     * @return
     * @author TangWei 2013-9-29下午2:40:52
     */
    public static String getText(EditText tv) {
        if (tv != null)
            return tv.getText().toString().trim();
        return "";
    }

    /**
     * 隐藏键盘
     *
     * @author TangWei 2013-9-13下午7:51:32
     */
    public static void hideKeyboard(Activity activity) {
        ((InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // public static void playSound(int raw, Context context) {
    // SoundPool sp;
    // sp = new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
    // int task = sp.load(context, raw, 1);
    // sp.play(task, 1, 1, 0, 0, 1);
    // }

    /**
     * 显示纯汉字的星期名称
     *
     * @param i 星期：1,2,3,4,5,6,7
     * @return
     * @author TangWei 2013-10-25上午11:31:51
     */
    public static String changeWeekToHanzi(int i) {
        switch (i) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    /**
     * 验证身份证号码
     *
     * @param idCard
     * @return
     * @author TangWei
     */
    public static boolean validateIdCard(String idCard) {
        if (isNull(idCard))
            return false;
        String pattern = "^[0-9]{17}[0-9|xX]{1}$";
        return idCard.matches(pattern);
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     * @author TangWei
     */
    public static boolean validatePhone(String phone) {
        if (isNull(phone))
            return false;
        String pattern = "^1[3,5,7,8]\\d{9}$";
        return phone.matches(pattern);
    }

    public static boolean validatePwd(String pwd) {
        if (isNull(pwd))
            return false;
        // 6-16位数字或字母,可以全部是数字或字母
        // String pattern = "^[a-zA-Z0-9]{6,16}+$";
        // 6-16位数字或字母，必须包含至少一个数字和一个字母
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return pwd.matches(pattern);
    }

    /**
     * 简单的验证一下银行卡号
     *
     * @param bankCard 信用卡是16位，其他的是13-19位
     * @return
     */
    public static boolean validateBankCard(String bankCard) {
        if (isNull(bankCard))
            return false;
        String pattern = "^\\d{13,19}$";
        return bankCard.matches(pattern);
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     * @author TangWei 2013-12-13下午2:33:16
     */
    public static boolean validateEmail(String email) {
        if (isNull(email))
            return false;
        String pattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        return email.matches(pattern);
    }

    public static String trimString(String str) {
        if (!Tools.isNull(str)) {
            return str.trim();
        }
        return "";
    }

    public static int StringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float StringToFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0.00f;
        }
    }

    public static String formatString(Object obj) {
        try {
            if (!Tools.isNull(obj.toString())) {
                return obj.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化money，当返回数据为空时，返回0.00
     *
     * @param obj
     * @return
     * @author TangWei 2013-11-23上午11:42:33
     */
    public static String formatMoney(Object obj) {
        String money = formatString(obj);
        try {
            if (money.length() == 0) {
                money = "0";
            }
            money = Constants.decimalFormat.format(Double.parseDouble(money) / 100.0D);
        } catch (Exception e) {
            return Constants.decimalFormat.format(0);
        }
        return money;
    }

    /**
     * @param d
     * @desc 格式化double，有小数则显示小数并且最大显示两位小数，没有小数则不显示小数位
     * @author wangjie
     * @date 2017/2/21 17:38
     */
    public static String formatDouble(String d) {
        String s = formatString(d);
        try {
            if (s.length() == 0) {
                s = "0";
            }
            s = new DecimalFormat("#0.##").format(Double.parseDouble(s) / 100.0D);
        } catch (Exception e) {
            return new DecimalFormat("#0.##").format(0D);
        }
        return s;
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数
     *
     * @param date   日期毫秒数
     * @param format 格式化样式 示例：yyyy-MM-dd HH:mm:ss
     * @return
     * @author TangWei 2013-11-29上午11:31:49
     */
    public static String formatDate(Object date, String format) {
        try {
            return new SimpleDateFormat(format).format(new Date(Long
                    .parseLong(formatString(date))));
        } catch (Exception e) {
            return "";
        }
    }

    public static String formatStringDate(String date, String fromFormat,
                                          String toFormat) {
        try {
            return new SimpleDateFormat(toFormat, Locale.CHINA)
                    .format(new SimpleDateFormat(fromFormat, Locale.CHINA)
                            .parse(date));
        } catch (Exception e) {
            return "";
        }
    }

    public static long dateStringToLong(String date, String fromFormat) {
        try {
            return new SimpleDateFormat(fromFormat, Locale.CHINA).parse(date)
                    .getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12 11:12:13
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    public static String formatTime(Object date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    public static String formatDate(Object date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 获取屏幕像素尺寸
     *
     * @return 数组：0-宽，1-高
     * @author TangWei 2013-10-31下午1:08:22
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    /**
     * 设置圆角的图片
     *
     * @param bitmap 图片
     * @param pixels 角度
     * @return
     * @author TangWei 2013-12-10下午4:43:33
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        try {
            if (bitmap != null) {
                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                        bitmap.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final int color = 0xff424242;
                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                        bitmap.getHeight());
                final RectF rectF = new RectF(rect);
                final float roundPx = pixels;

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);

                return output;
            }
        } catch (Exception e) {
        }

        return bitmap;
    }

    /**
     * 将图片转换为圆形的
     *
     * @param bitmap
     * @return
     * @author TangWei 2013-12-10下午4:45:47
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap = cutSquareBitmap(bitmap);
            return toRoundCorner(bitmap, bitmap.getWidth() / 2);
        }
        return bitmap;
    }

    /**
     * 把图片切成正方形的
     *
     * @param bitmap
     * @return
     * @author TangWei 2013-12-10下午5:16:18
     */
    public static Bitmap cutSquareBitmap(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                Bitmap result;
                int w = bitmap.getWidth();// 输入长方形宽
                int h = bitmap.getHeight();// 输入长方形高
                int nw;// 输出正方形宽
                if (w > h) {
                    // 宽大于高
                    nw = h;
                    result = Bitmap.createBitmap(bitmap, (w - nw) / 2, 0, nw,
                            nw);
                } else {
                    // 高大于宽
                    nw = w;
                    result = Bitmap.createBitmap(bitmap, 0, (h - nw) / 2, nw,
                            nw);
                }
                return result;
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 获取在GridView中一行中一张正方形图片的尺寸大小
     *
     * @param context 上下文，用于计算屏幕的宽度
     * @param offset  界面上左右两边的偏移量，dp值
     * @param spac    水平方向，图片之间的间距，dp值
     * @param count   一行中图片的个数
     * @return
     * @author TangWei 2013-12-12下午1:15:49
     */
    public static int getImageSize(Context context, int offset, int spac,
                                   int count) {
        int width = getScreenSize(context)[0] - Tools.DPtoPX(offset, context)
                - (Tools.DPtoPX(spac, context) * (count - 1));
        return width / count;
    }

    /**
     * 获取一个圆弧上等分点的坐标列表
     *
     * @param radius      半径
     * @param count       等分点个数
     * @param start_angle 开始角度
     * @param end_angle   结束角度
     * @return
     * @author TangWei 2013-12-16下午5:06:31
     */
    public static ArrayList<String[]> getDividePoints(double radius, int count,
                                                      double start_angle, double end_angle) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        double sub_angle = (start_angle - end_angle) / ((double) (count - 1));
        for (int i = 0; i < count; i++) {
            double angle = (start_angle - sub_angle * i) * Math.PI / 180;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            list.add(new String[]{x + "", y + ""});
        }
        return list;
    }

    /**
     * 判断字符串是邮箱还是手机号码
     *
     * @param str
     * @return 1-手机号码，2-邮箱，如果都不是则返回0
     * @author TangWei 2013-12-19下午1:59:16
     */
    public static int validatePhoneOrEmail(String str) {
        if (validatePhone(str))
            return 1;
        if (validateEmail(str))
            return 2;
        return 0;
    }

    /**
     * 播放动画
     *
     * @param layout
     * @param img
     * @param drawableClick
     * @param isClicked     true（点击）、false（取消）
     */
    public static void startAnimation(final View layout, ImageView img,
                                      int drawableBefore, int drawableClick, boolean isClicked) {
        if (isClicked) {
            img.setBackgroundResource(drawableClick);
        } else {
            img.setBackgroundResource(drawableBefore);
        }

        // 播放动画
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation1.setStartOffset(0);
        scaleAnimation1.setDuration(50);
        scaleAnimation2.setStartOffset(50);
        scaleAnimation2.setDuration(50);
        animationSet.addAnimation(scaleAnimation1);
        animationSet.addAnimation(scaleAnimation2);
        animationSet.setFillAfter(true);
        img.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encode(bitmapBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * SD卡是否存在
     */
    public static boolean existSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 创建文件夹
     */
    public static void makeDir(String path) {
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdir();
        }
    }

    /**
     * 根据Uri返回文件路径
     *
     * @param mUri
     * @return String
     * @author gdpancheng@gmail.com 2013-3-18 上午10:17:55
     */
    public static String getFilePath(ContentResolver mContentResolver, Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(mContentResolver, mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 将100以内的阿拉伯数字转换成中文汉字（15变成十五）
     *
     * @param round 最大值50
     * @return >99的，返回“”
     */
    public static String getHanZi1(int round) {
        if (round > 99 || round == 0) {
            return "";
        }
        int ge = round % 10;
        int shi = (round - ge) / 10;
        String value = "";
        if (shi != 0) {
            if (shi == 1) {
                value = "十";
            } else {
                value = getHanZi2(shi) + "十";
            }

        }
        value = value + getHanZi2(ge);
        return value;
    }

    /**
     * 将0-9 转换为 汉字（ _一二三四五六七八九）
     *
     * @param round
     * @return
     */
    public static String getHanZi2(int round) {
        String[] value = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        return value[round];
    }

    /**
     * 将content: 开通的系统uri转换成绝对路径
     *
     * @param mContentResolver
     * @param mUri
     * @return
     * @throws FileNotFoundException
     */
    public static String getFilePathByUri(ContentResolver mContentResolver,
                                          Uri mUri) throws FileNotFoundException {

        String imgPath;
        Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
        cursor.moveToFirst();
        imgPath = cursor.getString(1); // 图片文件路径
        return imgPath;
    }

    /**
     * 去除字符串中的 ":"
     *
     * @param str
     * @return
     */
    public static String deleteColon(String str) {
        if (str == null) {
            return null;
        } else {
            return str.replace(":", "");
        }
    }

    /**
     * 将 1800 加个":",变成 18:00
     *
     * @param str
     * @return
     */
    public static String addColon(String str) {
        if (str == null || str.length() != 4) {
            return null;
        }
        return str.substring(0, 2) + ":" + str.substring(2, 4);
    }

    /**
     * TODO(验证邮箱)
     *
     * @param email_str
     * @return boolean
     * @author ligt 2013-6-9 上午11:53:19
     */
    public boolean is_Email(String email_str) {

        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(" +
                        "([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = pattern.matcher(email_str);
        return m.matches();
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^1[3,5,8]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证车牌号
     *
     * @param carNumber
     * @return
     */
    public static boolean isCarNO(String carNumber) {
        Pattern p = Pattern
                .compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$");
        Matcher m = p.matcher(carNumber);
        return m.matches();
    }

    public static Bitmap convertViewToBitmapLayout(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static String combineBitmaps(Bitmap bitmap1, Bitmap bitmap2) {
        String file = "/sdcard" + File.separator + "/combine/"
                + System.currentTimeMillis() + ".png";
        File combine = new File(file);
        combine.getParentFile().mkdirs();
        try {
            Bitmap bitmap3 = Bitmap.createBitmap(
                    bitmap1.getWidth() + bitmap2.getWidth(),
                    bitmap1.getHeight(), bitmap1.getConfig());
            Canvas canvas = new Canvas(bitmap3);
            canvas.drawBitmap(bitmap1, 0, 0, null);
            canvas.drawBitmap(bitmap2, bitmap1.getWidth(), 0, null);
            // 将合并后的bitmap3保存为png图片到本地
            FileOutputStream out = new FileOutputStream(combine);
            bitmap3.compress(Bitmap.CompressFormat.PNG, 60, out);
            out.flush();
            out.close();
            bitmap1 = null;
            bitmap2 = null;
            return combine.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static String checkNull(String str) {
        return isNull(str) ? "" : str;
    }

    public static String getCachePath(Application appContext) {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment
                .MEDIA_MOUNTED))
            cacheDir = appContext.getExternalCacheDir();
        else
            cacheDir = appContext.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * 跳转拨号界面
     *
     * @param context
     * @param phone
     */
    public static void callPhone(Context context, String phone) {
        context.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + phone)));
    }

    /**
     * @param fileName 文件名称
     * @return 文件内容
     * @desc 从Assets目录下读取数据
     * @author wangjie
     * @date 2017/2/13 12:01
     */
    public static String getStringFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(App.app
                    .getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
//            while ((line = bufReader.readLine()) != null)
//                result += line;
            char[] tempchars = new char[1000];
            int length = 0;
            while ((length = bufReader.read(tempchars)) != -1) {
                result += new String(tempchars, 0, length);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param appContext 应用上下文
     * @param fileName   文件名
     * @param object     数据源，需要实现序列化
     * @desc 保存数据到私有文件
     * @author wangjie
     * @date 2017/1/16 16:04
     */
    public static void saveObject(Application appContext, String fileName, Serializable object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = appContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);// 写入
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * @param appContext 应用上下文
     * @param fileName   文件名
     * @desc 从私有文件获取数据
     * @author wangjie
     * @date 2017/1/16 16:04
     */
    public static <T> T getObject(Application appContext, String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            //获得输入流
            fis = appContext.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 将工程需要的资源文件拷贝到SD卡中使用
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source  assets 文件名
     * @param dest    sdk文件路径
     */
    public static void copyFromAssetsToSdcard(Context context, boolean isCover, String source,
                                              String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = context.getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
