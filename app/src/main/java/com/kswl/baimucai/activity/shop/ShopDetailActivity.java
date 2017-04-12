package com.kswl.baimucai.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.event.EventListActivity;
import com.kswl.baimucai.activity.goods.GoodsDetailActivity;
import com.kswl.baimucai.activity.search.SortingFragment;
import com.kswl.baimucai.activity.user.UserActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.CouponBean;
import com.kswl.baimucai.bean.GoodsBean;
import com.kswl.baimucai.bean.ShopBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.kswl.baimucai.view.ObservableScrollView;
import com.kswl.baimucai.view.PullToRefreshView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

public class ShopDetailActivity extends BaseActivity {

    GridView gv_goods;

    RelativeLayout rl_title;

    PullToRefreshView v_refresh;

    ObservableScrollView scrollView;

    ImageView iv_bg, iv_icon, iv_back;

    TextView tv_follow, tv_name, tv_vip, tv_follow_count;

    LinearLayout ll_coupon;

    ShopGoodsAdapter adapter;

    List<GoodsBean> data = new ArrayList<>();

    private int curPage = 1;

    private String shopId, sortId = "";

    private int selectCoupon = -1;

    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail, true);
        shopId = getIntent().getStringExtra(Constants.Char.SHOP_ID);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        gv_goods = (GridView) findViewById(R.id.gv_goods);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_follow = (TextView) findViewById(R.id.tv_follow);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_vip = (TextView) findViewById(R.id.tv_vip);
        tv_follow_count = (TextView) findViewById(R.id.tv_follow_count);
        ll_coupon = (LinearLayout) findViewById(R.id.ll_coupon);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        v_refresh = (PullToRefreshView) findViewById(R.id.v_refresh);

        adapter = new ShopGoodsAdapter(this, data);
        gv_goods.setAdapter(adapter);
        gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShopDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constants.Char.GOODS_ID, data.get(i).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getShopGoodsInfo();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getShopGoodsInfo();
            }
        });

        rl_title.getBackground().setAlpha(0);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx,
                                        int oldy) {
                // 这里的x、y是在X、Y轴上已滑动的距离
                if (0 == height) {
                    height = iv_bg.getHeight();
                }
                float half = height / 2;
                LogUtil.e(" y:" + y + "   oldy:" + oldy);
                int bgAlpha = 0;
                float vAlpha = 1f;
                if (y >= 0 && y <= half) {
                    bgAlpha = (int) (y * 255f / height);
                    vAlpha = (half - y) / half;
                    tv_follow.setTextColor(getResources().getColor(R.color.text_white));
                    iv_back.setImageResource(R.drawable.shop_back_white_icon);
                } else if (y > half && y < height) {
                    bgAlpha = (int) (y * 255f / height);
                    vAlpha = (y - half) / half;
                    tv_follow.setTextColor(getResources().getColor(R.color.text_black));
                    iv_back.setImageResource(R.drawable.shop_back_black_icon);
                } else {
                    bgAlpha = 255;
                    vAlpha = 1f;
                    tv_follow.setTextColor(getResources().getColor(R.color.text_black));
                    iv_back.setImageResource(R.drawable.shop_back_black_icon);
                }
                // Background 透明度是0-255
                rl_title.getBackground().setAlpha(bgAlpha);
                // view透明度0-1
                tv_follow.setAlpha(vAlpha);
                iv_back.setAlpha(vAlpha);
            }
        });

        getShopDetailInfo();
        getShopGoodsInfo();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_follow:
                // 关注
                if (null == App.app.getUser()) {
                    startActivityForResult(new Intent(ShopDetailActivity.this,
                            UserActivity.class), Constants.Code.REQUEST_SHOP_LOGIN);
                    return;
                }
                if (getResources().getString(R.string.follow).equals(tv_follow.getText().toString
                        ())) {
                    // 关注
                    shopFollow();
                } else {
                    // 取消关注
                    shopFollowCancel();
                }
                break;

            case R.id.ll_sort:
                // 分类查找
                startActivityForResult(new Intent(this, ShopFilterActivity.class),
                        Constants.Code.REQUEST_SHOP_FILTER);
                break;

            case R.id.ll_event:
                // 店铺活动
                startActivity(new Intent(this, EventListActivity.class)
                        .putExtra(Constants.Char.SHOP_ID, shopId));
                break;

            case R.id.ll_contact:
                // 联系卖家
                ToastUtil.showToast("联系卖家");
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case Constants.Code.REQUEST_SHOP_LOGIN:
                if (null != App.app.getUser()) {
                    getShopDetailInfo();
                }
                break;

            case Constants.Code.REQUEST_SHOP_FILTER:
                if (resultCode == RESULT_OK) {
                    sortId = intent.getStringExtra(Constants.Char.CLASSIFY_ID);
                    curPage = 1;
                    getShopGoodsInfo();
                }
                break;

            default:
                break;
        }
    }

    /**
     * @desc 加载店铺数据
     * @author wangjie
     * @date 2017/2/21 11:01
     */
    private void initShopView(ShopBean shopBean) {
        Glide.with(this).load(shopBean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        Glide.with(this).load(shopBean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        tv_name.setText(shopBean.getName());
        tv_vip.setText(Tools.isNull(shopBean.getShopLevel()) ? "0" : shopBean.getShopLevel());
        tv_follow_count.setText(getResources().getString(R.string.shop_follow_count) +
                (Tools.isNull(shopBean.getCareNum()) ? "0" : shopBean.getCareNum()));
    }

    /**
     * @desc 初始化优惠券数据
     * @author wangjie
     * @date 2017/2/21 11:01
     */
    private void initCouponView(List<CouponBean> couponList) {
        ll_coupon.removeAllViews();
        if (null != couponList) {
            int w = App.app.getScreenWidth() - 2 * Tools.dp2px(this, 20) - 2 * Tools.dp2px(this,
                    15);
            for (int i = 0; i < couponList.size(); i++) {
                View itemView = getLayoutInflater().inflate(R.layout.item_shop_coupon, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w / 3,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                if (i > 0) {
                    params.leftMargin = Tools.dp2px(this, 15);
                }
                itemView.setLayoutParams(params);
                TextView tv_amounts = (TextView) itemView.findViewById(R.id.tv_amounts);
                TextView tv_quota = (TextView) itemView.findViewById(R.id.tv_quota);

                CouponBean bean = couponList.get(i);
                tv_amounts.setText(Tools.formatDouble(bean.getDenomination()));
                tv_quota.setText(String.format(getResources().getString(R.string
                        .shop_coupon_quota), Tools.formatDouble(bean.getQuota())));
                if (Integer.parseInt(Tools.isNull(bean.getReceiveNum()) ? "0" : bean
                        .getReceiveNum()) > 0) {
                    // 已领取过
                    itemView.setBackgroundResource(R.drawable.xml_coupon_disabled_bg);
                } else {
                    itemView.setBackgroundResource(R.drawable.xml_coupon_usabled_bg);
                    final int finalI = i;
                    final String couponId = bean.getId();
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null == App.app.getUser()) {
                                startActivityForResult(new Intent(ShopDetailActivity.this,
                                        UserActivity.class), Constants.Code.REQUEST_SHOP_LOGIN);
                                return;
                            }
                            selectCoupon = finalI;
                            receiveShopCoupon(couponId);
                        }
                    });
                }
                ll_coupon.addView(itemView);
            }
        }
    }

    /**
     * @desc 店铺详情
     * @author wangjie
     * @date 2017/2/21 10:22
     */
    private void getShopDetailInfo() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", shopId);
        params.put("userId", null == App.app.getUser() ? "" : App.app.getUser().getId());
        httpRequest(Constants.Url.SHOP_GET_DETAIL, params, httpCallBack, 0, this);
    }

    /**
     * @desc 获取店铺商品
     * @author wangjie
     * @date 2017/2/21 10:20
     */
    private void getShopGoodsInfo() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        // 搜索关键字
        params.put("searchName", "");
        // 分类id
        params.put("type", sortId);
        // 店铺ID ，搜索店铺活动时可用
        params.put("id", shopId);
        params.put("city", "");
        // 排序方式
        params.put("sortAccording", SortingFragment.SORTING_DEFAULT);
        httpRequest(Constants.Url.GOODS_LIST_BY_SEARCH, params, httpCallBack, 1, this);
    }

    /**
     * @desc 关注店铺(type 0:收藏 1:点赞 2:关注, status 01900001:商品 01900002:话题 01900003:店铺)
     * @author wangjie
     * @date 2017/2/21 13:17
     */
    private void shopFollow() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", shopId);
        params.put("type", "2");
        params.put("status", "01900003");
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.COLLECT_INSERT, params, httpCallBack, 2, this);
    }

    /**
     * @desc 取消关注
     * @author wangjie
     * @date 2017/2/21 13:30
     */
    private void shopFollowCancel() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", shopId);
        params.put("type", "2");
        params.put("status", "01900003");
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.COLLECT_CANCEL, params, httpCallBack, 3, this);
    }

    /**
     * @desc 领取店铺优惠券
     * @author wangjie
     * @date 2017/2/21 13:17
     */
    private void receiveShopCoupon(String couponId) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", shopId);
        params.put("couponId", couponId);
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.COUPON_INSERT, params, httpCallBack, 4, this);
    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
            DialogUtils.getInstance().dismiss();
            ToastUtil.showToast(R.string.toast_network_fail);
            v_refresh.onHeaderRefreshComplete();
            v_refresh.onFooterRefreshComplete();
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            DialogUtils.getInstance().dismiss();
            v_refresh.onHeaderRefreshComplete();
            v_refresh.onFooterRefreshComplete();
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            JSONObject obj = jsonObj.optJSONObject("data");
                            // 关注状态（1：已关注，0 ：未关注）
                            String collectStatus = obj.optString("collectStatus", "1");
                            JSONObject shopObj = obj.optJSONObject("shopDto");
                            JSONArray couponArr = obj.optJSONArray("couponDtoList");
                            if ("1".equals(collectStatus) && null != App.app.getUser()) {
                                tv_follow.setText(getResources().getString(R.string.follow_cancel));
                            } else {
                                tv_follow.setText(getResources().getString(R.string.follow));
                            }
                            ShopBean shopBean = JsonUtil.JsonToBean(ShopBean.class, shopObj
                                    .toString());
                            ArrayList<CouponBean> couponList = JsonUtil.JsonToBean(CouponBean
                                    .class, couponArr.toString());
                            initShopView(shopBean);
                            initCouponView(couponList);
                            break;

                        case 1:
                            JSONArray arr = jsonObj.optJSONArray("data");
                            ArrayList<GoodsBean> mList = JsonUtil.JsonToBean(GoodsBean.class,
                                    arr.toString());
                            if (curPage == 1) {
                                data.clear();
                            }
                            if (null != mList) {
                                data.addAll(mList);
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        case 2:
                            // 关注
                            ToastUtil.showToast(R.string.success);
                            tv_follow.setText(R.string.follow_cancel);
                            break;

                        case 3:
                            // 取消关注
                            ToastUtil.showToast(R.string.success);
                            tv_follow.setText(R.string.follow);
                            break;

                        case 4:
                            // 领取优惠券
                            ToastUtil.showToast(R.string.success);
                            View vi = ll_coupon.getChildAt(selectCoupon);
                            vi.setBackgroundResource(R.drawable.xml_coupon_disabled_bg);
                            vi.setOnClickListener(null);
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
