package com.kswl.baimucai.activity.coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.bean.CouponBean;
import com.kswl.baimucai.utils.Tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wangjie
 * @desc 优惠券
 * @date 2017/2/10 12:03
 */
public class CouponOwnAdapter extends IBaseAdapter<CouponBean> {

    private int[] layoutArr = {R.layout.item_coupon,
            R.layout.item_coupon_type};

    private Set<CouponBean> selectSet = new HashSet<>();

    public CouponOwnAdapter(Context context, List<CouponBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(layoutArr[getItemViewType(position)], null);
        }
        if (getItemViewType(position) == 0) {
            ImageView iv_state = ViewHolder.get(convertView, R.id.iv_state);
            ImageView iv_check = ViewHolder.get(convertView, R.id.iv_check);
            TextView tv_amount = ViewHolder.get(convertView, R.id.tv_amount);
            TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
            TextView tv_quota = ViewHolder.get(convertView, R.id.tv_quota);
            TextView tv_date = ViewHolder.get(convertView, R.id.tv_date);
            LinearLayout ll_content = ViewHolder.get(convertView, R.id.ll_content);

            CouponBean bean = getItem(position);
            tv_name.setText(bean.getName());
            tv_amount.setText(Tools.formatDouble(bean.getDenomination()));
            tv_quota.setText(String.format(mContext.getResources().getString(R.string
                    .shop_coupon_quota), Tools.formatDouble(bean.getQuota())));
            tv_date.setText(String.format(mContext.getResources().getString(R.string.coupon_date),
                    Tools.formatDate(bean.getUseStart()), Tools.formatDate(bean.getUseEnd())));
            if ("0".equals(bean.getUseStatus()) && "0".equals(bean.getIsEffect())) {
                // 未使用，未失效
                ll_content.setBackgroundResource(R.drawable.xml_coupon_usabled_bg);
                iv_state.setImageResource(0);
            } else if ("1".equals(bean.getUseStatus())) {
                // 已使用
                ll_content.setBackgroundResource(R.drawable.xml_coupon_disabled_bg);
                iv_state.setImageResource(R.drawable.coupon_used_icon);
            } else if ("1".equals(bean.getIsEffect())) {
                // 已失效
                ll_content.setBackgroundResource(R.drawable.xml_coupon_disabled_bg);
                iv_state.setImageResource(R.drawable.coupon_expired_icon);
            }
            if (selectSet.contains(bean)) {
                iv_check.setImageResource(R.drawable.coupn_checked_icon);
            } else {
                iv_check.setImageResource(R.drawable.coupn_unchecked_icon);
            }
        }
        return convertView;
    }

    public Set<CouponBean> getSelectSet() {
        return selectSet;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        CouponBean bean = getItem(position);
        String id = bean.getId();
        return Tools.isNull(id) ? 1 : 0;
    }
}
