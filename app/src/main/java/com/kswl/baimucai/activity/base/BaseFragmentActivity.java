package com.kswl.baimucai.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.kswl.baimucai.R;

public class BaseFragmentActivity extends BaseActivity {

    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment, true);
    }

    @Override
    public void onBackPressed() {
        // 先判断fragment中是否拦截了返回事件
        if (!currentFragment.onFragmentBackPressed()) {
            //判断当前fragment堆栈中是否还可以返回
            onFragmentFinish();
        }
    }

    @Override
    public void onFragmentStart(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.activity_fragment, fragment).addToBackStack(null);
        if (null != currentFragment) {
            fragmentTransaction.hide(currentFragment);
        }
        fragmentTransaction.show(fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void onFragmentStartForResult(BaseFragment fragment, int
            requestCode) {
        fragment.setTargetFragment(currentFragment, requestCode);
        onFragmentStart(fragment);
    }

    @Override
    public void onFragmentFinish() {
        super.onBackPressed();
        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (stackCount > 0) {
            currentFragment = (BaseFragment) getSupportFragmentManager().getFragments()
                    .get(stackCount - 1);
            getSupportFragmentManager().beginTransaction().show(currentFragment).commit();
        }
        if (stackCount == 0) {
            finish();
        }
    }

}
