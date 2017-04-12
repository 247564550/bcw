package com.kswl.baimucai.activity.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.shop.ShopDetailActivity;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.view.CustomAlertDialog;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 平台电话
     */
    public static final String PHONE = "400-123-4567";

    /**
     * ll_reason_refund 退款原因<br>
     * ll_reason_refuse 拒绝退货原因<br>
     * ll_phone 平台电话<br>
     */
    LinearLayout ll_reason_refund, ll_reason_refuse, ll_phone;

    /**
     * tv_order_info 订单编号等信息<br>
     * tv_timing 自动确认倒计时<br>
     * tv_state 订单状态<br>
     * tv_platform_phone 平台电话<br>
     */
    TextView tv_order_info, tv_timing, tv_state, tv_platform_phone;

    /**
     * 订单处理按钮<br>
     * tv_order_cancel 取消订单 <br>
     * tv_order_detele 删除订单<br>
     * tv_order_refund 退款<br>
     * tv_order_payment 付款<br>
     * tv_order_remind 提醒发货<br>
     * tv_order_logistics 查看物流<br>
     * tv_order_confirm 确认收货<br>
     * tv_order_appraise 评价<br>
     */
    TextView tv_order_cancel, tv_order_detele, tv_order_refund, tv_order_payment,
            tv_order_remind, tv_order_logistics, tv_order_confirm, tv_order_appraise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        showTopTitle(R.string.order_detail);
        showTopLine();
        ll_reason_refund = (LinearLayout) findViewById(R.id.ll_reason_refund);
        ll_reason_refuse = (LinearLayout) findViewById(R.id.ll_reason_refuse);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        tv_order_info = (TextView) findViewById(R.id.tv_order_info);
        findViewById(R.id.ll_shop).setOnClickListener(this);
        tv_order_cancel = (TextView) findViewById(R.id.tv_order_cancel);
        tv_order_detele = (TextView) findViewById(R.id.tv_order_detele);
        tv_order_refund = (TextView) findViewById(R.id.tv_order_refund);
        tv_order_payment = (TextView) findViewById(R.id.tv_order_payment);
        tv_order_remind = (TextView) findViewById(R.id.tv_order_remind);
        tv_order_logistics = (TextView) findViewById(R.id.tv_order_logistics);
        tv_order_confirm = (TextView) findViewById(R.id.tv_order_confirm);
        tv_order_appraise = (TextView) findViewById(R.id.tv_order_appraise);
        tv_timing = (TextView) findViewById(R.id.tv_timing);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_platform_phone = (TextView) findViewById(R.id.tv_platform_phone);

        int type = getIntent().getIntExtra(Constants.Char.ORDER_TYPE, Constants.Code
                .REQUEST_ORDER_COMPLETE);
        switch (type) {
            case Constants.Code.REQUEST_ORDER_PAYMENT:
                // 带支付订单
                tv_order_cancel.setVisibility(View.VISIBLE);
                tv_order_payment.setVisibility(View.VISIBLE);
                tv_state.setText(R.string.order_payment);
                tv_order_info.setText("订单编号：1111111111\n付款时间：2016-01-01\n发货时间：2016-01-01");
                break;

            case Constants.Code.REQUEST_ORDER_SEND:
                // 待发货订单
                tv_order_refund.setVisibility(View.VISIBLE);
                tv_order_remind.setVisibility(View.VISIBLE);
                tv_timing.setVisibility(View.GONE);
                tv_state.setText(R.string.order_send);
                tv_order_info.setText("订单编号：1111111111\n付款时间：2016-01-01\n发货时间：2016-01-01");

                // 暂时测试
                findViewById(R.id.tv_goods_refund).setVisibility(View.VISIBLE);
                break;

            case Constants.Code.REQUEST_ORDER_RECEIVE:
                // 待收货订单
                tv_order_refund.setVisibility(View.VISIBLE);
                tv_order_logistics.setVisibility(View.VISIBLE);
                tv_order_confirm.setVisibility(View.VISIBLE);
                tv_state.setText(R.string.order_receive);
                tv_order_info.setText("订单编号：111111111\n付款时间：2016-01-01\n发货时间：2016-01-01");

                // 暂时测试
                findViewById(R.id.tv_goods_refund).setVisibility(View.VISIBLE);
                break;

            case Constants.Code.REQUEST_ORDER_REFUND:
                // 退货订单
                ll_phone.setVisibility(View.VISIBLE);
                tv_state.setText(R.string.order_state_seller);
                tv_order_info.setText("退款编号：1111111111\n申请时间：2016-01-01\n确认时间：2016-01-01");
                tv_platform_phone.setText(
                        String.format(getResources().getString(R.string.order_phone_hint), PHONE));
                ll_reason_refund.setVisibility(View.VISIBLE);
                ll_reason_refuse.setVisibility(View.VISIBLE);
                break;

            case Constants.Code.REQUEST_ORDER_COMPLETE:
                // 已完成订单
                tv_order_refund.setVisibility(View.VISIBLE);
                tv_order_detele.setVisibility(View.VISIBLE);
                tv_order_appraise.setVisibility(View.VISIBLE);
                tv_state.setText(R.string.order_state_complete);
                tv_order_info.setText("订单编号：1111111111\n付款时间：2016-01-01\n发货时间：2016-01-01");
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_shop:
                startActivity(new Intent(this, ShopDetailActivity.class)
                        .putExtra(Constants.Char.SHOP_ID, ""));
                break;

            case R.id.ll_phone:
                // 平台电话
                ToastUtil.showToast("联系平台");
                break;

            case R.id.tv_order_cancel:
                // 取消订单
                ToastUtil.showToast("取消订单");
                break;

            case R.id.tv_order_detele:
                // 删除订单
                ToastUtil.showToast("删除订单");
                break;

            case R.id.tv_order_refund:
                // 退款
                new CustomAlertDialog.Builder(this).setMessage(R.string.refund_confirm_hint)
                        .setPositiveButton(R.string.yes, new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                startActivity(new Intent(OrderDetailActivity.this,
                                        RefundActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.no, null).create().show();
                break;

            case R.id.tv_order_payment:
                // 付款
                ToastUtil.showToast("付款");
                break;

            case R.id.tv_order_remind:
                // 提醒发货
                ToastUtil.showToast("提醒发货");
                break;

            case R.id.tv_order_logistics:
                // 查看物流
                startActivity(new Intent(this, LogisticsActivity.class));
                break;

            case R.id.tv_order_confirm:
                // 确认收货
                ToastUtil.showToast("确认收货");
                break;

            case R.id.tv_order_appraise:
                // 评价
                startActivity(new Intent(this, OrderAppraiseActivity.class));
                break;

            default:
                break;
        }
    }
}
