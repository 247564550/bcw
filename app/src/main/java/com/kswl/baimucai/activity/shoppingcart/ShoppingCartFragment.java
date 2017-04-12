package com.kswl.baimucai.activity.shoppingcart;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.activity.goods.GoodsDetailActivity;
import com.kswl.baimucai.activity.order.OrderInsertActivity;
import com.kswl.baimucai.activity.shop.ShopDetailActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.CartGoodsBean;
import com.kswl.baimucai.bean.CartShopBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.kswl.baimucai.view.CustomAlertDialog;
import com.kswl.baimucai.view.PullToRefreshView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

/**
 * @author wangjie
 * @desc 购物车
 * @date 2017/1/16 18:35
 */
public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 是否显示返回按钮
     */
    public static final String IS_SHOW_BACK = "isShowBack";

    PullToRefreshView v_refresh;

    ExpandableListView elv_goods;

    /**
     * 结算
     */
    Button btn_submit;

    ImageView iv_back;

    CheckBox cb_check_all;

    TextView tv_total;

    EditText et_count;

    Dialog dialog;

    ShoppingCartAdapter adapter;

    ArrayList<CartShopBean> data = new ArrayList<>();

    /**
     * count 准备修改的商品购买数量 <br>
     * checkCount 选中商品种类数量<br>
     */
    private int groupPosition, childPosition, count, checkCount = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            groupPosition = msg.arg1;
            childPosition = msg.arg2;
            switch (msg.what) {
                case ShoppingCartAdapter.ITEM_CLICK:
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    intent.putExtra(Constants.Char.GOODS_ID, adapter.getChild(groupPosition,
                            childPosition).getMdsePropertyId());
                    startActivity(intent);
                    break;

                case ShoppingCartAdapter.ITEM_PLUS:
                    if (Tools.StringToInt(adapter.getChild(groupPosition, childPosition)
                            .getAmount()) >=
                            Tools.StringToInt(adapter.getChild(groupPosition, childPosition)
                                    .getStock())) {
                        ToastUtil.showToast(R.string.toast_goods_stock);
                        break;
                    }
                    updateCartGoodsCount(adapter.getChild(groupPosition, childPosition).getId(),
                            Tools.StringToInt(adapter.getChild(groupPosition, childPosition)
                                    .getAmount()) + 1);
                    break;

                case ShoppingCartAdapter.ITEM_MINUS:
                    if (Tools.StringToInt(adapter.getChild(groupPosition, childPosition)
                            .getAmount()) <= 1) {
                        ToastUtil.showToast(R.string.toast_goods_stock);
                        break;
                    }
                    updateCartGoodsCount(adapter.getChild(groupPosition, childPosition).getId(),
                            Tools.StringToInt(adapter.getChild(groupPosition, childPosition)
                                    .getAmount()) - 1);
                    break;

                case ShoppingCartAdapter.ITEM_CHECK:
                    data.get(groupPosition).getCartMdseDto().get(childPosition).setCheck(true);
                    adapter.notifyDataSetChanged();
                    updateCheck();
                    updateTotalPrice();
                    break;

                case ShoppingCartAdapter.ITEM_UNCHECK:
                    data.get(groupPosition).getCartMdseDto().get(childPosition).setCheck(false);
                    adapter.notifyDataSetChanged();
                    updateCheck();
                    updateTotalPrice();
                    break;

                case ShoppingCartAdapter.ITEM_COUNT:
                    showUpdateCountDialog(data.get(groupPosition).getCartMdseDto().get
                            (childPosition).getAmount());
                    break;

                case ShoppingCartAdapter.ITEM_DELETE:
                    deleteCartGoods(data.get(groupPosition).getCartMdseDto().get(childPosition)
                            .getId());
                    break;

                default:
                    break;
            }
        }
    };

    public static ShoppingCartFragment newInstance(boolean isShowBack) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_SHOW_BACK, isShowBack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        v_refresh = (PullToRefreshView) view.findViewById(R.id.v_refresh);
        elv_goods = (ExpandableListView) view.findViewById(R.id.lv_goods);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        cb_check_all = (CheckBox) view.findViewById(R.id.cb_check_all);
        tv_total = (TextView) view.findViewById(R.id.tv_total);

        if (null != getArguments() && getArguments().getBoolean(IS_SHOW_BACK, false)) {
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setOnClickListener(this);
        }
        btn_submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        v_refresh.setEnablePullLoadMoreDataStatus(false);
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                getShoppingCartData();
            }
        });

        adapter = new ShoppingCartAdapter(mContext, data, mHandler);
        elv_goods.setAdapter(adapter);
        elv_goods.setOnGroupClickListener(new ExpandableListView
                .OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                startActivity(new Intent(mContext, ShopDetailActivity.class)
                        .putExtra(Constants.Char.SHOP_ID, adapter.getGroup(groupPosition)
                                .getShopId()));
                return true;
            }
        });
        cb_check_all.setOnCheckedChangeListener(mCheckedChangeListener);

        getShoppingCartData();
        DialogUtils.getInstance().show(mContext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (checkCount == 0) {
                    ToastUtil.showToast(R.string.toast_cart_no_choose);
                } else {
                    Intent intent = new Intent(mContext, OrderInsertActivity.class);
                    intent.putExtra(Constants.Char.ORDER_GOODS_INFO, data);
                    startActivity(intent);
                }
                break;

            case R.id.iv_back:
                mContext.finish();
                break;

            default:
                break;
        }
    }

    /**
     * @desc 更新全选的选中状态
     * @author wangjie
     * @date 2017/3/21 17:33
     */
    private void updateCheck() {
        boolean isCheckAll = true;
        A:
        for (int i = 0; i < data.size(); i++) {
            ArrayList<CartGoodsBean> goodsList = data.get(i).getCartMdseDto();
            if (goodsList != null)
                for (int j = 0; j < goodsList.size(); j++) {
                    if (!goodsList.get(j).isCheck()) {
                        isCheckAll = false;
                        break A;
                    }
                }
        }
        // 先清空全选状态变化监听，以屏蔽此处调用setChecked引起的变化
        cb_check_all.setOnCheckedChangeListener(null);
        cb_check_all.setChecked(isCheckAll);
        cb_check_all.setOnCheckedChangeListener(mCheckedChangeListener);
    }

    /**
     * 全选按钮选中状态变化监听
     */
    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton
            .OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            for (int i = 0; i < data.size(); i++) {
                ArrayList<CartGoodsBean> goodsList = data.get(i).getCartMdseDto();
                if (goodsList != null)
                    for (int j = 0; j < goodsList.size(); j++) {
                        goodsList.get(j).setCheck(isChecked);
                    }
            }
            adapter.notifyDataSetChanged();
            updateTotalPrice();
        }
    };

    /**
     * @desc 更新合计价格
     * @author wangjie
     * @date 2017/3/21 17:32
     */
    private void updateTotalPrice() {
        int price = 0;
        checkCount = 0;
        for (int i = 0; i < data.size(); i++) {
            ArrayList<CartGoodsBean> goodsList = data.get(i).getCartMdseDto();
            if (goodsList != null)
                for (int j = 0; j < goodsList.size(); j++) {
                    if (goodsList.get(j).isCheck()) {
                        price += Tools.StringToInt(goodsList.get(j).getUnitPrice()) * Tools
                                .StringToInt(goodsList.get(j).getAmount());
                        checkCount++;
                    }
                }
        }
        tv_total.setText("¥ " + Tools.formatMoney(price));
        btn_submit.setText(String.format(mContext.getString(R.string.shopping_settle), checkCount));
    }

    private void showUpdateCountDialog(String oldCount) {
        if (null == dialog) {
            View view = mContext.getLayoutInflater().inflate(R.layout.dialog_cart_update_count,
                    null);
            et_count = (EditText) view.findViewById(R.id.et_count);
            // 减
            ImageButton btn_minus = (ImageButton) view.findViewById(R.id.btn_minus);
            // 加
            ImageButton btn_plus = (ImageButton) view.findViewById(R.id.btn_plus);
            btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oc = Tools.StringToInt(et_count.getText().toString());
                    if (oc <= 1) {
                        ToastUtil.showToast(R.string.toast_goods_stock);
                    } else {
                        et_count.setText(oc - 1 + "");
                    }
                }
            });
            btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_count.setText(Tools.StringToInt(et_count.getText().toString()) + 1 + "");
                }
            });
            dialog = new CustomAlertDialog.Builder(mContext)
                    .setTitle(R.string.shopping_update_count)
                    .setContentView(view)
                    .setCanceledOnTouchOutside(false)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            int oc = Tools.StringToInt(et_count.getText().toString());
                            int stock = Tools.StringToInt(data.get(groupPosition).getCartMdseDto
                                    ().get(childPosition).getStock());
                            if (oc <= 0 || oc > stock) {
                                ToastUtil.showToast(R.string.toast_goods_stock);
                            } else {
                                updateCartGoodsCount(data.get(groupPosition).getCartMdseDto().get
                                        (childPosition).getId(), oc);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
        }
        et_count.setText(oldCount);
        dialog.show();
    }

    /**
     * @desc 获取购物车数据
     * @author wangjie
     * @date 2017/3/20 17:05
     */
    private void getShoppingCartData() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        mContext.httpRequest(Constants.Url.SHOPPINGCART_LIST, params, httpCallBack, 0, this);
    }

    /**
     * @param id
     * @param count 更改后数量
     * @desc 更改购物车商品数量
     * @author wangjie
     * @date 2017/3/22 15:33
     */
    private void updateCartGoodsCount(String id, int count) {
        this.count = count;
        DialogUtils.getInstance().show(mContext);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        params.put("amount", count + "");
        mContext.httpRequest(Constants.Url.SHOPPINGCART_CHANGE_COUNT, params, httpCallBack, 1,
                this);
    }

    /**
     * @desc 删除购物车商品
     * @author wangjie
     * @date 2017/3/22 16:52
     */
    private void deleteCartGoods(String id) {
        DialogUtils.getInstance().show(mContext);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        mContext.httpRequest(Constants.Url.SHOPPINGCART_DELETE, params, httpCallBack, 2,
                this);
    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
            DialogUtils.getInstance().dismiss();
            ToastUtil.showToast(R.string.toast_network_fail);
            v_refresh.onHeaderRefreshComplete();
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            DialogUtils.getInstance().dismiss();
            v_refresh.onHeaderRefreshComplete();
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            JSONObject obj = jsonObj.optJSONObject("data");
                            JSONArray cartDtos = obj.optJSONArray("cartDtos");
                            String sum = obj.optString("sum");
                            List<CartShopBean> mList = JsonUtil.JsonToBean(CartShopBean.class,
                                    cartDtos.toString());
                            if (null != mList) {
                                data.clear();
                                data.addAll(mList);
                                adapter.notifyDataSetChanged();
                                int groupCount = elv_goods.getCount();
                                for (int i = 0; i < groupCount; i++) {
                                    elv_goods.expandGroup(i);
                                }
                                updateTotalPrice();
                            }
                            break;

                        case 1:
                            data.get(groupPosition).getCartMdseDto().get(childPosition).setAmount
                                    (count + "");
                            adapter.notifyDataSetChanged();
                            updateTotalPrice();
                            break;

                        case 2:
                            data.get(groupPosition).getCartMdseDto().remove(childPosition);
                            if (data.get(groupPosition).getCartMdseDto().size() == 0) {
                                // 如果该店铺下的商品被删完了，同时删掉该店铺
                                data.remove(groupPosition);
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        default:
                            break;
                    }
                } else {
                    ToastUtil.showToast(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
