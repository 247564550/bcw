package com.kswl.baimucai.activity.user;

import android.os.Bundle;
import android.text.TextUtils;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragmentActivity;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.LogUtil;

public class UserActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        String curTag = getIntent().getStringExtra(Constants.Char.FRAGMENT_INTENT_TAG);
        curTag = TextUtils.isEmpty(curTag) ? Constants.Tag.USER_LOGIN : curTag;
        switch (curTag) {
            case Constants.Tag.USER_LOGIN:
                onFragmentStart(UserLoginFragment.newInstance());
                break;

            case Constants.Tag.USER_REGISTER:
                onFragmentStart(UserInputPhoneFragment.newInstance(true));
                break;

            default:
                onFragmentStart(UserLoginFragment.newInstance());
                break;
        }
    }


}
