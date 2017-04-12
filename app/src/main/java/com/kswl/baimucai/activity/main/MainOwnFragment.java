package com.kswl.baimucai.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.account.AccountShowActivity;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.activity.collect.ICollectActivity;
import com.kswl.baimucai.activity.coupon.CouponListOwnActivity;
import com.kswl.baimucai.activity.history.BrowsingHistoryActivity;
import com.kswl.baimucai.activity.message.MsgListActivity;
import com.kswl.baimucai.activity.order.OrderListActivity;
import com.kswl.baimucai.activity.setting.AboutActivity;
import com.kswl.baimucai.activity.setting.FeedbackActivity;
import com.kswl.baimucai.activity.setting.VipActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.ToastUtil;

/**
 * @author wangjie
 * @desc 我的
 * @date 2017/1/16 18:35
 */
public class MainOwnFragment extends BaseFragment implements View.OnClickListener {

    ImageView iv_bg, iv_portrait;

    /**
     * 进度条
     */
    ProgressBar pb_integral;

    /**
     * tv_progress 进度显示<br>
     * tv_nickname 昵称
     */
    TextView tv_progress, tv_nickname;

    OnMainPageChangeListener onMainPageChangeListener;

    public static MainOwnFragment newInstance() {
        MainOwnFragment fragment = new MainOwnFragment();
        // Bundle args = new Bundle();
        // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnMainPageChangeListener) {
            onMainPageChangeListener = (OnMainPageChangeListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_own, container, false);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        iv_portrait = (ImageView) view.findViewById(R.id.iv_portrait);
        pb_integral = (ProgressBar) view.findViewById(R.id.pb_integral);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);

        view.findViewById(R.id.rl_order_payment).setOnClickListener(this);
        view.findViewById(R.id.rl_order_send).setOnClickListener(this);
        view.findViewById(R.id.rl_order_receive).setOnClickListener(this);
        view.findViewById(R.id.rl_order_refund).setOnClickListener(this);
        view.findViewById(R.id.rl_order_all).setOnClickListener(this);
        view.findViewById(R.id.ll_msg).setOnClickListener(this);
        view.findViewById(R.id.ll_collect).setOnClickListener(this);
        view.findViewById(R.id.ll_vip).setOnClickListener(this);
        view.findViewById(R.id.ll_about).setOnClickListener(this);
        view.findViewById(R.id.ll_feedback).setOnClickListener(this);
        view.findViewById(R.id.ll_browsing).setOnClickListener(this);
        view.findViewById(R.id.ll_coupon).setOnClickListener(this);
        view.findViewById(R.id.ll_share).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        view.findViewById(R.id.tv_account).setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_order_payment:
                // 待付款订单
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra(Constants.Char.ORDER_TYPE, Constants.Code.REQUEST_ORDER_PAYMENT);
                startActivity(intent);
                break;

            case R.id.rl_order_send:
                // 待发货订单
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra(Constants.Char.ORDER_TYPE, Constants.Code.REQUEST_ORDER_SEND);
                startActivity(intent);
                break;

            case R.id.rl_order_receive:
                // 待收货订单
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra(Constants.Char.ORDER_TYPE, Constants.Code.REQUEST_ORDER_RECEIVE);
                startActivity(intent);
                break;

            case R.id.rl_order_refund:
                // 退货订单
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra(Constants.Char.ORDER_TYPE, Constants.Code.REQUEST_ORDER_REFUND);
                startActivity(intent);
                break;

            case R.id.rl_order_all:
                // 全部订单
                intent = new Intent(mContext, OrderListActivity.class);
                intent.putExtra(Constants.Char.ORDER_TYPE, Constants.Code.REQUEST_ORDER_ALL);
                startActivity(intent);
                break;

            case R.id.ll_msg:
                // 消息中心
                startActivity(new Intent(mContext, MsgListActivity.class));
                break;

            case R.id.ll_collect:
                // 我的收藏
                startActivity(new Intent(mContext, ICollectActivity.class));
                break;

            case R.id.ll_vip:
                // 会员服务
                startActivity(new Intent(mContext, VipActivity.class));
                break;

            case R.id.ll_about:
                // 关于我们
                startActivity(new Intent(mContext, AboutActivity.class));
                break;

            case R.id.ll_feedback:
                // 意见反馈
                startActivity(new Intent(mContext, FeedbackActivity.class));
                break;

            case R.id.ll_browsing:
                // 浏览记录
                startActivity(new Intent(mContext, BrowsingHistoryActivity.class));
                break;

            case R.id.ll_coupon:
                // 我的优惠券
                startActivity(new Intent(mContext, CouponListOwnActivity.class));
                break;

            case R.id.ll_share:
                // 分享
                ToastUtil.showToast("分享");
                break;

            case R.id.btn_logout:
                // 退出登录
                if (null != onMainPageChangeListener) {
                    App.app.setUser(null);
                    if (EMClient.getInstance().isLoggedInBefore()) {
                        EMClient.getInstance().logout(true);
                    }
                    onMainPageChangeListener.onMainPageChange(0);
                }
                break;

            case R.id.tv_account:
                // 账户信息
                startActivity(new Intent(mContext, AccountShowActivity.class));
                break;

            default:
                break;
        }
    }
}
