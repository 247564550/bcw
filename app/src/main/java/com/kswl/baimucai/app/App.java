package com.kswl.baimucai.app;

import android.app.Application;
import android.os.Environment;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.kswl.baimucai.bean.UserBean;
import com.kswl.baimucai.exception.CrashHandler;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.Tools;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by wangjie on 2016/11/10.
 */
public class App extends Application {

    /**
     * 应用实例对象
     */
    public static App app;

    /**
     * 屏幕宽度
     */
    private int screenWidth = 0, screenHeight = 0;

    private UserBean user;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        // 注册crashHandler
        CrashHandler.getInstance().init(this);

        initOkHttp();
        initHX();
    }

    /**
     * @desc 初始化OkHttp
     * @author wangjie
     * @date 2017/3/1 19:08
     */
    private void initOkHttp() {
        //初始化配置网络请求框架
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                .writeTimeout(15000L, TimeUnit.MILLISECONDS)
//                .cache(new Cache(new File(Environment.getExternalStorageDirectory(), "HttpCache")
//                        , 10 * 1024 * 1024))
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * @desc 初始化环信
     * @author wangjie
     * @date 2017/3/1 19:07
     */
    private void initHX() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EaseUI.getInstance().init(app, options);
        // EMClient.getInstance().init(app, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);
        LogUtil.e("initHX");
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public UserBean getUser() {
        if (user == null) {
            user = Tools.getObject(app, "user.bean");
        }
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
        Tools.saveObject(app, "user.bean", user);
    }
}
