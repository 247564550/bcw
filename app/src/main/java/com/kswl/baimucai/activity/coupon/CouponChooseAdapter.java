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

import java.util.List;

/**
 * @author wangjie
 * @desc 优惠券
 * @date 2017/2/10 12:03
 */
public class CouponChooseAdapter extends IBaseAdapter<CouponBean> {

    private int selectPosition = -1;

    public CouponChooseAdapter(Context context, List<CouponBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_coupon, null);
        }
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
        iv_state.setVisibility(View.GONE);
        if ("0".equals(bean.getUseStatus()) && "0".equals(bean.getIsEffect())) {
            // 未使用，未失效
            ll_content.setBackgroundResource(R.drawable.xml_coupon_usabled_bg);
            iv_check.setVisibility(View.VISIBLE);
            if (selectPosition == position) {
                iv_check.setImageResource(R.drawable.coupn_checked_icon);
            } else {
                iv_check.setImageResource(R.drawable.coupn_unchecked_icon);
            }
        } else {
            // 已失效、已使用
            ll_content.setBackgroundResource(R.drawable.xml_coupon_disabled_bg);
            iv_check.setVisibility(View.GONE);
        }
        return convertView;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
