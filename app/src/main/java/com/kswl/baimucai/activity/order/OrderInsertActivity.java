package com.kswl.baimucai.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.address.AddressActivity;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.goods.GoodsDetailActivity;
import com.kswl.baimucai.activity.shop.ShopDetailActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.AddressBean;
import com.kswl.baimucai.bean.CartGoodsBean;
import com.kswl.baimucai.bean.CartShopBean;
import com.kswl.baimucai.bean.CouponBean;
import com.kswl.baimucai.bean.GoodsBean;
import com.kswl.baimucai.bean.ShopBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.Call;

public class OrderInsertActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout ll_content;

    RelativeLayout rl_address;

    /**
     * 订单商品总件数、总价
     */
    TextView tv_total_count, tv_total_price;

    AddressBean addressBean;

    ArrayList<CartShopBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_insert);
        showTopTitle(R.string.order_insert_title);
        showTopLine();
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        tv_total_count = (TextView) findViewById(R.id.tv_total_count);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);

        findViewById(R.id.tv_address_choose).setOnClickListener(this);
        rl_address.setOnClickListener(this);

        data = (ArrayList<CartShopBean>) getIntent().getSerializableExtra(Constants.Char
                .ORDER_GOODS_INFO);

        initGoodsView();
        getDefaultAddress();
    }

    /**
     * @desc 初始化商品信息
     * @author wangjie
     * @date 2017/3/22 19:15
     */
    private void initGoodsView() {
        // 商品总件数
        int totalCount = 0;
        // 商品总价，包含运费和商品定价
        int totalPrice = 0;
        for (int i = 0; i < data.size(); i++) {
            // 分割线
            View dividerView = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, Tools.dp2px(this, 5));
            dividerView.setBackgroundResource(R.color.gray_bg);
            dividerView.setLayoutParams(params);
            ll_content.addView(dividerView);

            // 店铺信息
            View shopView = getLayoutInflater().inflate(R.layout.item_order_shop, null);
            TextView tv_shop = (TextView) shopView.findViewById(R.id.tv_shop);
            tv_shop.setText(data.get(i).getShopName());
            final String shopId = data.get(i).getShopId();
            shopView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderInsertActivity.this, ShopDetailActivity.class)
                            .putExtra(Constants.Char.SHOP_ID, shopId));
                }
            });
            ll_content.addView(shopView);

            // 当前店铺下商品数量
            int shopCount = 0;
            // 当前店铺合计,包含运费和商品价格
            int shopPrice = 0;
            // 当前店铺运费合计
            int shopFreight = 0;
            // 店铺下商品信息
            for (int j = 0; j < data.get(i).getCartMdseDto().size(); j++) {
                View goodsView = getLayoutInflater().inflate(R.layout.item_order_goods, null);
                ImageView iv_icon = (ImageView) goodsView.findViewById(R.id.iv_icon);
                TextView tv_name = (TextView) goodsView.findViewById(R.id.tv_name);
                TextView tv_number = (TextView) goodsView.findViewById(R.id.tv_number);
                TextView tv_type = (TextView) goodsView.findViewById(R.id.tv_type);
                TextView tv_price = (TextView) goodsView.findViewById(R.id.tv_price);
                TextView tv_count = (TextView) goodsView.findViewById(R.id.tv_count);

                CartGoodsBean goodsBean = data.get(i).getCartMdseDto().get(j);
                String type = "";
                for (int k = 0; k < goodsBean.getTypeDtos().size(); k++) {
                    type += goodsBean.getTypeDtos().get(k).getType() + "：" + goodsBean
                            .getTypeDtos().get(k).getValue() + "，";
                }
                int count = Tools.StringToInt(goodsBean.getAmount());
                int price = Tools.StringToInt(goodsBean.getUnitPrice());
                int freight = Tools.StringToInt(goodsBean.getYunfei());
                shopCount += count;
                shopPrice += price * count;
                shopFreight += freight * count;
                Glide.with(App.app).load(goodsBean.getImage())
                        .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                        .dontAnimate().into(iv_icon);
                tv_name.setText(goodsBean.getMdseName());
                tv_number.setText(String.format(getString(R.string.goods_number), goodsBean
                        .getMdsePropertyId()));
                tv_type.setText(type);
                tv_count.setText("X" + count);
                tv_price.setText("¥ " + Tools.formatMoney(price));
                final String goodsId = goodsBean.getMdsePropertyId();
                goodsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderInsertActivity.this, GoodsDetailActivity
                                .class);
                        intent.putExtra(Constants.Char.GOODS_ID, goodsId);
                        startActivity(intent);
                    }
                });
                ll_content.addView(goodsView);
            }
            shopPrice += shopFreight;
            totalCount += shopCount;
            totalPrice += shopPrice;

            // 运费，店铺合计信息
            View freightView = getLayoutInflater().inflate(R.layout.item_order_insert, null);
            TextView tv_shop_freight = (TextView) freightView.findViewById(R.id.tv_shop_freight);
            TextView tv_shop_count = (TextView) freightView.findViewById(R.id.tv_shop_count);
            TextView tv_shop_price = (TextView) freightView.findViewById(R.id.tv_shop_price);

            tv_shop_freight.setText("¥ " + Tools.formatMoney(shopFreight));
            tv_shop_count.setText(String.format(getString(R.string.order_number), shopCount));
            tv_shop_price.setText("¥ " + Tools.formatMoney(shopPrice));
            ll_content.addView(freightView);
        }
        tv_total_count.setText(String.format(getString(R.string.order_insert_count), totalCount));
        tv_total_price.setText("¥ " + Tools.formatMoney(totalPrice));
    }

    /**
     * @desc 修改地址数据
     * @author wangjie
     * @date 2017/3/22 19:14
     */
    private void initAddressView() {
        if (addressBean == null) {
            rl_address.setVisibility(View.GONE);
        } else {
            rl_address.setVisibility(View.VISIBLE);
            TextView tv_name = (TextView) rl_address.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) rl_address.findViewById(R.id.tv_phone);
            TextView tv_address = (TextView) rl_address.findViewById(R.id.tv_address);
            tv_name.setText(addressBean.getName());
            tv_phone.setText(addressBean.getPhone());
            tv_address.setText(addressBean.getProvince() + addressBean.getCity()
                    + addressBean.getArea() + addressBean.getAddress());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case Constants.Code.REQUEST_ADDRESS_CHOOSE:
                    addressBean = (AddressBean) intent.getSerializableExtra(Constants.Char
                            .ADDRESS_DATA);
                    initAddressView();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                startActivity(new Intent(this, PayConfirmActivity.class));
                finish();
                break;

            case R.id.rl_address:
            case R.id.tv_address_choose:
                startActivityForResult(new Intent(OrderInsertActivity.this, AddressActivity.class)
                                .putExtra(Constants.Char.ADDRESS_TYPE, true),
                        Constants.Code.REQUEST_ADDRESS_CHOOSE);
                break;

            default:
                break;
        }
    }

    /**
     * 获取默认地址
     */
    private void getDefaultAddress() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.ADDRESS_GET_DEFAULT, params, httpCallBack, 0, this);
    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
            DialogUtils.getInstance().dismiss();
            ToastUtil.showToast(R.string.toast_network_fail);
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            DialogUtils.getInstance().dismiss();
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            JSONObject obj = jsonObj.optJSONObject("data");
                            addressBean = JsonUtil.JsonToBean(AddressBean.class, obj.toString());
                            initAddressView();
                            break;

                        default:
                            break;
                    }
                } else {
                    // ToastUtil.showToast(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
