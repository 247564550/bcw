package com.kswl.baimucai.activity.shoppingcart;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.ViewHolder;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.CartGoodsBean;
import com.kswl.baimucai.bean.CartShopBean;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.Tools;

import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 一级分类适配器
 * @date 2017-2017/1/19-15:20
 */
public class ShoppingCartAdapter extends BaseExpandableListAdapter {

    /**
     * item点击
     */
    public static final int ITEM_CLICK = 1;

    /**
     * 点击加号
     */
    public static final int ITEM_PLUS = 2;

    /**
     * 点击减号
     */
    public static final int ITEM_MINUS = 3;

    /**
     * 选中
     */
    public static final int ITEM_CHECK = 4;

    /**
     * 取消选中
     */
    public static final int ITEM_UNCHECK = 5;

    /**
     * 购买数量点击
     */
    public static final int ITEM_COUNT = 6;

    /**
     * 删除
     */
    public static final int ITEM_DELETE = 7;

    private Context mContext;

    List<CartShopBean> data;

    Handler mHandler;

    public ShoppingCartAdapter(Context mContext, List<CartShopBean> data, Handler mHandler) {
        this.mContext = mContext;
        this.data = data;
        this.mHandler = mHandler;
    }

    @Override
    public int getGroupCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        LogUtil.e("dd" + groupPosition);
        return groupPosition >= data.size() || null == data.get(groupPosition).getCartMdseDto()
                ? 0 : data.get(groupPosition).getCartMdseDto().size();
    }

    @Override
    public CartShopBean getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public CartGoodsBean getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getCartMdseDto().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shoppingcart_shop,
                    null);
        }
        TextView tv_shop = ViewHolder.get(convertView, R.id.tv_shop);
        tv_shop.setText(getGroup(groupPosition).getShopName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean
            isLastChild, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shoppingcart_goods,
                    null);
        }
        RelativeLayout rl_content = ViewHolder.get(convertView, R.id.rl_content);
        final SwipeLayout swipeLayout = ViewHolder.get(convertView, R.id.swipeLayout);
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_model = ViewHolder.get(convertView, R.id.tv_model);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
        TextView tv_delete = ViewHolder.get(convertView, R.id.tv_delete);
        // 减
        ImageButton btn_minus = ViewHolder.get(convertView, R.id.btn_minus);
        // 加
        ImageButton btn_plus = ViewHolder.get(convertView, R.id.btn_plus);
        CheckBox checkBox = ViewHolder.get(convertView, R.id.checkBox);

        CartGoodsBean bean = getChild(groupPosition, childPosition);
        Glide.with(App.app).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(bean.getMdseName());
        if ("0".equals(bean.getStock())) {
            // 无货
            tv_model.setVisibility(View.VISIBLE);
        } else {
            tv_model.setVisibility(View.INVISIBLE);
        }
        tv_price.setText("¥ " + Tools.formatMoney(bean.getUnitPrice()));
        tv_count.setText(bean.getAmount());
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(bean.isCheck());
        if (null != mHandler) {
            rl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = ITEM_CLICK;
                    msg.arg1 = groupPosition;
                    msg.arg2 = childPosition;
                    mHandler.sendMessage(msg);
                }
            });
            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = ITEM_MINUS;
                    msg.arg1 = groupPosition;
                    msg.arg2 = childPosition;
                    mHandler.sendMessage(msg);
                }
            });
            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = ITEM_PLUS;
                    msg.arg1 = groupPosition;
                    msg.arg2 = childPosition;
                    mHandler.sendMessage(msg);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Message msg = new Message();
                    msg.arg1 = groupPosition;
                    msg.arg2 = childPosition;
                    if (isChecked)
                        msg.what = ITEM_CHECK;
                    else
                        msg.what = ITEM_UNCHECK;
                    mHandler.sendMessage(msg);
                }
            });
            tv_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = ITEM_COUNT;
                    msg.arg1 = groupPosition;
                    msg.arg2 = childPosition;
                    mHandler.sendMessage(msg);
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = ITEM_DELETE;
                    msg.arg1 = groupPosition;
                    msg.arg2 = childPosition;
                    mHandler.sendMessage(msg);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
