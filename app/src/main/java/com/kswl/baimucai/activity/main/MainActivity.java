package com.kswl.baimucai.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.classify.MainClassifyFragment;
import com.kswl.baimucai.activity.shoppingcart.ShoppingCartFragment;
import com.kswl.baimucai.activity.tender.MainTenderFragment;
import com.kswl.baimucai.activity.user.UserActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.app.AppManager;
import com.kswl.baimucai.view.FragmentIndicator;

public class MainActivity extends BaseActivity implements OnMainPageChangeListener {

    FragmentIndicator fi_indicator;

    private int curTag = 0;

    /**
     * 当前加载的Fragment
     */
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main, true);
        init();
    }

    @Override
    public void onMainPageChange(int tag) {
        fi_indicator.setCheckTag(tag);
    }

    private void initView() {
        fi_indicator = (FragmentIndicator) findViewById(R.id.fi_indicator);
    }

    private void init() {
        initView();

        fi_indicator.setOnIndicateListener(new FragmentIndicator.OnIndicateListener() {
            @Override
            public boolean onIndicate(int which) {
                if ((which == 4 || which == 3) && null == App.app.getUser()) {
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                    return true;
                }
                showFragment(which);
                return false;
            }
        });
        fi_indicator.setCheckTag(curTag);
    }

    private void showFragment(int tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                tag + "");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (tag) {
            case 0:
                if (fragment == null) {
                    fragment = MainHomeFragment.newInstance();
                    fragmentTransaction.add(R.id.fl_fragment, fragment, tag + "");
                }
                break;

            case 1:
                if (fragment == null) {
                    fragment = MainClassifyFragment.newInstance();
                    fragmentTransaction.add(R.id.fl_fragment, fragment, tag + "");
                }
                break;

            case 2:
                if (fragment == null) {
                    fragment = MainTenderFragment.newInstance();
                    fragmentTransaction.add(R.id.fl_fragment, fragment, tag + "");
                }
                break;

            case 3:
                if (fragment == null) {
                    fragment = ShoppingCartFragment.newInstance(false);
                    fragmentTransaction.add(R.id.fl_fragment, fragment, tag + "");
                }
                break;

            case 4:
                if (fragment == null) {
                    fragment = MainOwnFragment.newInstance();
                    fragmentTransaction.add(R.id.fl_fragment, fragment, tag + "");
                }
                break;

            default:
                break;
        }
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
            currentFragment.setUserVisibleHint(false);
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.commit();
        currentFragment = (Fragment) fragment;
        curTag = tag;
        currentFragment.setUserVisibleHint(true);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (null != currentFragment) {
            currentFragment.setUserVisibleHint(false);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null != currentFragment) {
            currentFragment.setUserVisibleHint(true);
        }
    }


    @Override
    public void onBackPressed() {
        if (curTag != 0) {
            fi_indicator.setCheckTag(0);
            return;
        }
        doublePressBackToast();
    }

    /**
     * 判断是否已经点击过一次回退键
     */
    private boolean isBackPressed = false;

    /**
     * @desc 双击回退键退出程序
     * @author wangjie
     * @date 2017/1/16 16:40
     */
    private void doublePressBackToast() {
        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, R.string.exit_hint, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 3000);
        } else {
            AppManager.getAppManager().appExit();
        }
    }
}
