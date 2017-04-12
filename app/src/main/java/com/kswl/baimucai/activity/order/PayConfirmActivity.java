package com.kswl.baimucai.activity.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.coupon.CouponListChooseActivity;
import com.kswl.baimucai.activity.coupon.CouponListOwnActivity;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.view.CustomAlertDialog;

public class PayConfirmActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirm);
        showTopTitle(R.string.pay_confirm_title);
        showTopLine();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                new CustomAlertDialog.Builder(this).setMessage(R.string.pay_update_amount_hint)
                        .setPositiveButton(R.string.pay_continue, new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                startActivity(new Intent(PayConfirmActivity.this,
                                        PayResultActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null).create().show();
                break;

            case R.id.ll_coupon:
                startActivity(new Intent(this, CouponListChooseActivity.class));
                break;

            default:
                break;
        }
    }
}
