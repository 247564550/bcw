package com.kswl.baimucai.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;

public class SexActivity extends BaseActivity {

    ImageView iv_male, iv_female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex);
        showTopTitle(R.string.account_sex);
        showRightText(R.string.confirm);
        showTopLine();
        iv_male = (ImageView) findViewById(R.id.iv_male);
        iv_female = (ImageView) findViewById(R.id.iv_female);

    }

    @Override
    public void onRightTextBtnClick() {
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_male:
                iv_male.setVisibility(View.VISIBLE);
                iv_female.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_female:
                iv_male.setVisibility(View.INVISIBLE);
                iv_female.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }


}
