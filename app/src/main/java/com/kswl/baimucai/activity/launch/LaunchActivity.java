package com.kswl.baimucai.activity.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.main.MainActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.Tools;

public class LaunchActivity extends BaseActivity {

    ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch, true);
        LogUtil.e("LaunchActivity-onCreate");
        iv_splash = (ImageView) findViewById(R.id.iv_splash);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        App.app.setScreenWidth(width);
        App.app.setScreenHeight(height);

        LogUtil.e(Tools.getCachePath(App.app));

        load(iv_splash);
    }

    /**
     * @desc 渐变展示启动屏
     * @author wangjie
     * @date 2017/1/16 16:17
     */
    private void load(View view) {
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(3000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    /**
     * @desc 跳转意图，根据本地保存版本号判断是否更新版本了，如果有更新版本则跳转引导页，如果未更新版本则跳转主页
     * @author wangjie
     * @date 2017/1/16 16:18
     */
    private void redirectTo() {
        int versionCode = Tools.getVerCode(this);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean isFirstRun = versionCode > prefs.getInt("versionCode", 0);
        if (isFirstRun == false) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("versionCode", versionCode);
            editor.commit();
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        }
        finish();
    }
}
