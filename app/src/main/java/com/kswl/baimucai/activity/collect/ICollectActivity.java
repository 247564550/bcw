package com.kswl.baimucai.activity.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

/**
 * @author wangjie
 * @desc 我的收藏
 * @date 2017/2/9 20:40
 */
public class ICollectActivity extends BaseActivity {

    TextView tv_goods, tv_shop;

    View v_goods, v_shop;

    private int curTag = 0;

    /**
     * 当前加载的Fragment
     */
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icollect);
        showTopTitle(R.string.own_collect);
        tv_goods = (TextView) findViewById(R.id.tv_goods);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        v_goods = findViewById(R.id.v_goods);
        v_shop = findViewById(R.id.v_shop);
        showFragment(0);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goods:
                if (0 != curTag) {
                    showFragment(0);
                    tv_goods.setTextColor(getResources().getColor(R.color.text_orange));
                    v_goods.setBackgroundResource(R.color.orange);
                    tv_shop.setTextColor(getResources().getColor(R.color.text_dark_gray));
                    v_shop.setBackgroundResource(R.color.white);
                }
                break;

            case R.id.ll_shop:
                if (1 != curTag) {
                    showFragment(1);
                    tv_goods.setTextColor(getResources().getColor(R.color.text_dark_gray));
                    v_goods.setBackgroundResource(R.color.white);
                    tv_shop.setTextColor(getResources().getColor(R.color.text_orange));
                    v_shop.setBackgroundResource(R.color.orange);
                }
                break;

            default:
                break;
        }

    }

    private void showFragment(int tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                tag + "");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (tag) {
            case 0:
                if (fragment == null) {
                    fragment = CollectGoodsFragment.newInstance();
                    fragmentTransaction.add(R.id.fl_fragment, fragment, tag + "");
                }
                break;

            case 1:
                if (fragment == null) {
                    fragment = CollectShopFragment.newInstance();
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
        curTag = tag;
        currentFragment = (Fragment) fragment;
    }

}
