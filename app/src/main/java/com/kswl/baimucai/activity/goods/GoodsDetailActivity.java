package com.kswl.baimucai.activity.goods;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.base.ImagePagerAdapter;
import com.kswl.baimucai.activity.order.OrderInsertActivity;
import com.kswl.baimucai.activity.shop.ShopDetailActivity;
import com.kswl.baimucai.activity.shoppingcart.ShoppingCartActivity;
import com.kswl.baimucai.activity.user.UserActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.AppraiseBean;
import com.kswl.baimucai.bean.GoodsBannerBean;
import com.kswl.baimucai.bean.GoodsModelBean;
import com.kswl.baimucai.bean.GoodsModelChildBean;
import com.kswl.baimucai.bean.GoodsModelGroupBean;
import com.kswl.baimucai.bean.GoodsRecommendBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.kswl.baimucai.view.FlowViewGroup;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 选中的名称集合，多个以,隔开
     */
    public static final String CHOOSE_NAMES = "chooseNames";

    /**
     * 选中的id集合，多个以,隔开
     */
    public static final String CHOOSE_IDS = "chooseIds";

    RelativeLayout rl_page, rl_title;

    ViewPager vp_banner;

    /**
     * ll_banner_indicator banner滑动指示器 <br>
     * ll_appraise_desc 商品评价-父布局 <br>
     * ll_appraise_list 商品评价 <br>
     * ll_dialog_model 弹窗型号显示 <br>
     */
    LinearLayout ll_banner_indicator, ll_appraise_desc, ll_appraise_list, ll_dialog_model;

    /**
     * tv_all_appraise 全部评价按钮 <br>
     * tv_goods_id 商品id <br>
     * tv_goods_name 商品名称 <br>
     * tv_goods_price 商品价格 <br>
     * tv_freight 运费 <br>
     * tv_cart_count 购物车数量 <br>
     * tv_stock 库存 <br>
     * tv_choose 已选择 <br>
     * tv_dialog_stock 弹窗显示库存 <br>
     * tv_dialog_choose 弹窗已选择 <br>
     */
    TextView tv_all_appraise, tv_goods_id, tv_goods_name, tv_goods_price, tv_freight,
            tv_cart_count, tv_stock, tv_choose, tv_dialog_stock, tv_dialog_choose;

    /**
     * 商品详情 <br>
     */
    WebView wb_detail;

    TextView tv_detail, tv_appraise;
    View v_detail, v_appraise;

    /**
     * 购买数量
     */
    EditText et_count;

    GridView gv_goods;

    /**
     * iv_back 返回 <br>
     * iv_share 分享 <br>
     * iv_collect 收藏 <br>
     */
    ImageView iv_back, iv_share, iv_collect;

    Dialog dialog;

    /**
     * goodsId 商品id <br>
     * shopId 店铺id <br>
     * groupId 商品可用组合id <br>
     */
    private String goodsId, shopId, groupId;

    /**
     * 所有类型数据，包含一级分类及其子分类
     */
    List<GoodsModelBean> modelData = new ArrayList<>();

    /**
     * 所有可用的分类组合
     */
    List<GoodsModelGroupBean> groupData = new ArrayList<>();

    /**
     * 所有选中子分类
     */
//    List<GoodsModelChildBean> chooseData = new ArrayList<>();
    Map<String, GoodsModelChildBean> chooseMap = new HashMap<>();

    /**
     * 当前库存
     */
    private int curStock = 0, buyCount = 1;

    // 是否收藏
    boolean isCollect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail, true);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_black);
        rl_page = (RelativeLayout) findViewById(R.id.rl_page);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        vp_banner = (ViewPager) findViewById(R.id.vp_banner);
        ll_banner_indicator = (LinearLayout) findViewById(R.id.ll_banner_indicator);
        ll_appraise_desc = (LinearLayout) findViewById(R.id.ll_appraise_desc);
        ll_appraise_list = (LinearLayout) findViewById(R.id.ll_appraise_list);
        tv_all_appraise = (TextView) findViewById(R.id.tv_all_appraise);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_appraise = (TextView) findViewById(R.id.tv_appraise);
        v_detail = findViewById(R.id.v_detail);
        v_appraise = findViewById(R.id.v_appraise);
        wb_detail = (WebView) findViewById(R.id.wb_detail);
        gv_goods = (GridView) findViewById(R.id.gv_goods);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        tv_goods_id = (TextView) findViewById(R.id.tv_goods_id);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_price = (TextView) findViewById(R.id.tv_goods_price);
        tv_freight = (TextView) findViewById(R.id.tv_freight);
        tv_cart_count = (TextView) findViewById(R.id.tv_cart_count);
        tv_stock = (TextView) findViewById(R.id.tv_stock);
        tv_choose = (TextView) findViewById(R.id.tv_choose);

        goodsId = getIntent().getStringExtra(Constants.Char.GOODS_ID);
        getGoodsDetailInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_share:
                // 分享
                ToastUtil.showToast("分享");
                break;

            case R.id.iv_collect:
                // 收藏
                if (null == App.app.getUser()) {
                    startActivity(new Intent(this, UserActivity.class));
                    break;
                }
                if (isCollect) {
                    goodsCollectCancel();
                } else {
                    goodsCollect();
                }
                break;

            case R.id.btn_shop:
                // 进店逛逛
                if (!Tools.isNull(shopId)) {
                    startActivity(new Intent(this, ShopDetailActivity.class)
                            .putExtra(Constants.Char.SHOP_ID, shopId));
                }
                break;

            case R.id.ll_detail:
                // 商品详情
                tv_detail.setFocusableInTouchMode(true);
                tv_detail.setFocusable(true);
                tv_detail.setTextColor(getResources().getColor(R.color.text_orange));
                v_detail.setBackgroundResource(R.color.orange);
                tv_appraise.setTextColor(getResources().getColor(R.color.text_dark_gray));
                v_appraise.setBackgroundResource(R.color.white);
                wb_detail.setVisibility(View.VISIBLE);
                ll_appraise_desc.setVisibility(View.GONE);
                break;

            case R.id.ll_appraise:
                // 商品评价
                tv_detail.setTextColor(getResources().getColor(R.color.text_dark_gray));
                v_detail.setBackgroundResource(R.color.white);
                tv_appraise.setTextColor(getResources().getColor(R.color.text_orange));
                v_appraise.setBackgroundResource(R.color.orange);
                wb_detail.setVisibility(View.GONE);
                ll_appraise_desc.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_all_appraise:
                // 查看全部评价
                startActivity(new Intent(this, AppraiseListActivity.class));
                break;

            case R.id.ll_contact:
                // 联系卖家
                ToastUtil.showToast("联系卖家");
                break;

            case R.id.rl_cart:
                // 打开购物车
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;

            case R.id.btn_cart:
            case R.id.btn_dialog_cart:
                // 加入购物车
                buyCount = Tools.StringToInt(null == et_count ? "1" : et_count.getText()
                        .toString());
                GoodsModelGroupBean chooseGroup = getChooseGroup(getChooseModel().get(CHOOSE_IDS));
                // 判断是否所有类型都选择完整，是否是可用的组合
                if (null == chooseGroup) {
                    ToastUtil.showToast(R.string.toast_goods_type);
                    if (null == dialog || !dialog.isShowing()) {
                        showChooseModelView();
                    }
                    break;
                } else {
                    groupId = chooseGroup.getId();
                }
                if (buyCount > curStock) {
                    ToastUtil.showToast(R.string.toast_goods_stock);
                    if (null == dialog || !dialog.isShowing()) {
                        showChooseModelView();
                    }
                    break;
                }
                addToShoppingCart();
                break;

            case R.id.btn_buy:
            case R.id.btn_dialog_buy:
                // 立即购买
                // startActivity(new Intent(this, OrderInsertActivity.class));
                break;

            case R.id.ll_choose:
                // 选择型号
//                // 仿淘宝缩小旋转动画
//                AnimatorSet animatorSet = new AnimatorSet();//组合动画
//                ObjectAnimator scaleX = ObjectAnimator.ofFloat(rl_page, "scaleX", 1f, 0.8f);
//                ObjectAnimator scaleY = ObjectAnimator.ofFloat(rl_page, "scaleY", 1f, 0.9f);
//                ObjectAnimator rotationX = ObjectAnimator.ofFloat(rl_page, "rotationX", 0, 10, 0);
//                animatorSet.setDuration(1000);
//                animatorSet.setInterpolator(new DecelerateInterpolator());
//                animatorSet.playTogether(scaleX, scaleY, rotationX);//多个动画同时开始
//                animatorSet.start();
                showChooseModelView();
                break;

            case R.id.btn_plus:
                // 加
                String old1 = et_count.getText().toString();
                if (Tools.StringToInt(old1) >= curStock) {
                    ToastUtil.showToast(R.string.toast_goods_stock);
                    break;
                }
                et_count.setText(Integer.parseInt(TextUtils.isEmpty(old1) ? "0" : old1) + 1 + "");
                break;

            case R.id.btn_minus:
                // 减
                String old2 = et_count.getText().toString();
                int oldCount = Integer.parseInt(TextUtils.isEmpty(old2) ? "0" : old2);
                et_count.setText((oldCount > 1 ? (oldCount - 1) : oldCount) + "");
                break;

            default:
                break;
        }
    }

    /**
     * @author wangjie
     * @date 2017/1/18 17:07
     * @desc 初始化活动banner
     */
    private void initActivitiesBanner(ArrayList<GoodsBannerBean> urls) {
        if (null != urls && urls.size() > 0) {
            ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, urls, null);
            vp_banner.setAdapter(pagerAdapter);
            if (urls.size() > 1) {
                // 只有当轮播图多余一张时，才设置滑动滑动指示器
                int indicatorSize = (int) getResources().getDimension(
                        R.dimen.banner_indicator_size);
                for (int i = 0; i < urls.size(); i++) {
                    ImageView indicator = new ImageView(this);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            indicatorSize, indicatorSize);
                    param.leftMargin = 10;
                    indicator.setLayoutParams(param);
                    indicator.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    indicator.setImageResource(R.drawable.xml_viewpager_indicator_selector);
                    ll_banner_indicator.addView(indicator);
                }
                // 为了在OnPageChangeListener中引用ViewPager
                // 设置指示器中第一项选中
                ll_banner_indicator.getChildAt(0).setSelected(true);
                vp_banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int
                            positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // viewpager滑动时更新指示器
                        for (int i = 0; i < vp_banner.getAdapter().getCount(); i++) {
                            ll_banner_indicator.getChildAt(i).setSelected(i == position);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
        }
    }

    /**
     * @desc 初始化webview相关配置
     * @author wangjie
     * @date 2017/2/14 13:23
     */
    private void initWebview(String data) {
        wb_detail.clearCache(true);
        wb_detail.getSettings().setJavaScriptEnabled(true);
        wb_detail.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 设置可以支持缩放
        wb_detail.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        // wv.getSettings().setBuiltInZoomControls(true);
        // 扩大比例的缩放(WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小，如下设置)
        wb_detail.getSettings().setUseWideViewPort(true);
        // 缓存模式
        wb_detail.getSettings().setLoadWithOverviewMode(true);
        // 自适应屏幕
        wb_detail.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置不使用缓存，只从网络获取数据.
        wb_detail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // 设置

//        String css = "<style type=\"text/css\"> img {" +
//                "width:100%;" +//限定图片宽度填充屏幕
//                "height:auto;" +//限定图片高度自动
//                "}body {" +
//                "margin-right:15px;" +//限定网页中的文字右边距为15px(可根据实际需要进行行管屏幕适配操作)
//                "margin-left:15px;" +//限定网页中的文字左边距为15px(可根据实际需要进行行管屏幕适配操作)
//                "margin-top:15px;" +//限定网页中的文字上边距为15px(可根据实际需要进行行管屏幕适配操作)
//                "font-size:40px;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
//                "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
//                "}</style>";
//        String html = "<html><header>" + css + "</header>" + data + "</html>";

        String html = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\"" +
                " content=\"width=device-width, initial-scale=1.0\">"
                + "<style type=\"text/css\">body { font-family: Arial,\"microsoft yahei\"," +
                "Verdana; word-wrap:break-word; padding:0;	margin:0; font-size:14px; " +
                "color:#000; background: #fff; " +
                "overflow-x:hidden; }img {padding:0; margin:0; vertical-align:top; border: " +
                "none}li,ul {list-style: none outside none; padding: 0; margin: " +
                "0}input[type=text],select {-webkit-appearance:none; -moz-appearance: none; " +
                "margin:0; padding:0; background:none; border:none; font-size:14px; " +
                "text-indent:3px; font-family: Arial,\"microsoft yahei\",Verdana;}.wrapper { " +
                "width:95%; padding-right:10px;padding-top:10px;padding-bottom:10px;" +
                "padding-left:10px; box-sizing:border-box;}p { color:#666; line-height:1.0em;}" +
                ".wrapper img { display:block; max-width:100%; height:auto !important; " +
                "margin-bottom:10px;} p {margin:0; margin-bottom:1px;}</style></head><body><div " +
                "class=\"wrapper\">" + data + "</div></body></html>";

        wb_detail.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    /**
     * @desc 初始化商品评价数据
     * @author wangjie
     * @date 2017/3/8 15:53
     */
    private void initAppraiseView(List<AppraiseBean> mList) {
        // 评价总数据条数
        if (null != mList) {
            for (int i = 0; i < mList.size(); i++) {
                View appraiseView = getLayoutInflater().inflate(R.layout.item_appraise,
                        null);
                RatingBar ratingBar = (RatingBar) appraiseView.findViewById(R.id.ratingBar);
                TextView tv_name = (TextView) appraiseView.findViewById(R.id.tv_name);
                TextView tv_date = (TextView) appraiseView.findViewById(R.id.tv_date);
                TextView tv_desc = (TextView) appraiseView.findViewById(R.id.tv_desc);
                tv_name.setText(mList.get(i).getNickName());
                tv_date.setText(mList.get(i).getCreateTime());
                tv_desc.setText(mList.get(i).getIntroduce());
                String level = mList.get(i).getStarLevel();
                ratingBar.setRating(Float.parseFloat(Tools.isNull(level) ? "0" : level));
                ll_appraise_list.addView(appraiseView);
            }
        }
    }

    /**
     * @desc 初始化店家推荐数据
     * @author wangjie
     * @date 2017/3/8 16:18
     */
    private void initRecommendView(List<GoodsRecommendBean> mList, String shopName) {
        final RecommendGoodsAdapter adapter = new RecommendGoodsAdapter(this, mList, shopName);
        gv_goods.setAdapter(adapter);
        gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GoodsDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constants.Char.GOODS_ID, adapter.getItem(i).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * @desc 选择型号
     * @author wangjie
     * @date 2017/3/7 17:17
     */
    private void showChooseModelView() {
        if (null != dialog) {
            dialog.show();
            return;
        }
        dialog = new Dialog(this, R.style.DialogFullscreen);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_goods_choose_model, null);
        ImageButton btn_minus = (ImageButton) view.findViewById(R.id.btn_minus);
        ImageButton btn_plus = (ImageButton) view.findViewById(R.id.btn_plus);
        Button btn_dialog_cart = (Button) view.findViewById(R.id.btn_dialog_cart);
        Button btn_dialog_buy = (Button) view.findViewById(R.id.btn_dialog_buy);
        // 购买数量
        et_count = (EditText) view.findViewById(R.id.et_count);
        // 库存
        tv_dialog_stock = (TextView) view.findViewById(R.id.tv_stock);
        // 已选择
        tv_dialog_choose = (TextView) view.findViewById(R.id.tv_choose);
        // 型号
        ll_dialog_model = (LinearLayout) view.findViewById(R.id.ll_model);

        updateModelView(ll_dialog_model);
        et_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (str.startsWith("0")) {
                    et_count.setText(str.substring(1));
                }
            }
        });

        tv_dialog_stock.setText(String.format(getString(R.string.goods_stock_dialog), "" +
                curStock));
        tv_dialog_choose.setText(getChooseModel().get(CHOOSE_NAMES));
        btn_minus.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_dialog_cart.setOnClickListener(this);
        btn_dialog_buy.setOnClickListener(this);

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                AnimatorSet animatorSet = new AnimatorSet();//组合动画
//                ObjectAnimator scaleX = ObjectAnimator.ofFloat(rl_page, "scaleX", 0.8f, 1f);
//                ObjectAnimator scaleY = ObjectAnimator.ofFloat(rl_page, "scaleY", 0.9f, 1f);
//                ObjectAnimator rotationX = ObjectAnimator.ofFloat(rl_page, "rotationX", 0, 10, 0);
//                animatorSet.setDuration(500);
//                animatorSet.setInterpolator(new DecelerateInterpolator());
//                animatorSet.playTogether(scaleX, scaleY, rotationX);//多个动画同时开始
//                animatorSet.start();
                // 更新已选择和库存
                Map<String, String> map = getChooseModel();
                GoodsModelGroupBean chooseGroup = getChooseGroup(map.get(CHOOSE_IDS));
                if (chooseGroup != null) {
                    groupId = chooseGroup.getId();
                    tv_choose.setText(map.get(CHOOSE_NAMES));
                    curStock = Tools.StringToInt(chooseGroup.getAmount());
                    tv_stock.setText(String.format(getString(R.string.goods_stock_dialog), "" +
                            curStock));
                    tv_goods_price.setText("¥ " + Tools.formatMoney(chooseGroup.getUnitPrice()));
                } else {
                    tv_choose.setText("");
                    groupId = "";
                }
            }
        });

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 此处可以设置dialog显示的位置
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.dialog_animation);
        dialog.show();
    }

    /**
     * @desc 添加一级分类
     * @author wangjie
     * @date 2017/3/9 16:56
     */
    private void updateModelView(LinearLayout ll_model) {
        ll_model.removeAllViews();
        for (int i = 0; i < modelData.size(); i++) {
            View itemView = getLayoutInflater().inflate(R.layout.item_goods_model, null);
            TextView tv_model = (TextView) itemView.findViewById(R.id.tv_model);
            FlowViewGroup fvg_model = (FlowViewGroup) itemView.findViewById(R.id.fvg_model);

            String name = modelData.get(i).getName();
            tv_model.setText(name);
            updateModelChildView(fvg_model, name, modelData.get(i).getMdseTypePropertyDetailDtos());
            ll_model.addView(itemView);
        }
        // 更新已选择和库存
        Map<String, String> map = getChooseModel();
        tv_dialog_choose.setText(map.get(CHOOSE_NAMES));
        GoodsModelGroupBean chooseGroup = getChooseGroup(map.get(CHOOSE_IDS));
        if (chooseGroup != null) {
            curStock = Tools.StringToInt(chooseGroup.getAmount());
            tv_dialog_stock.setText(String.format(getString(R.string.goods_stock_dialog),
                    "" + curStock));
            if (Tools.StringToInt(et_count.getText().toString()) > curStock) {
                et_count.setText("" + curStock);
            }
        }
    }

    /**
     * @param fvg_model
     * @param parentName 父分类名称
     * @param child      子分类数据集
     * @desc 添加子分类
     * @author wangjie
     * @date 2017/3/9 16:56
     */
    private void updateModelChildView(ViewGroup fvg_model, final String parentName,
                                      List<GoodsModelChildBean> child) {
        fvg_model.removeAllViews();
        if (null != child) {
            for (int i = 0; i < child.size(); i++) {
                final GoodsModelChildBean bean = child.get(i);
                String id = bean.getId();
                TextView childView = (TextView) getLayoutInflater().inflate(R.layout
                        .item_goods_model_child, fvg_model, false);
                childView.setText(bean.getName());
                if (checkIsCanClick(bean, parentName)) {
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (chooseMap.containsValue(bean)) {
                                chooseMap.put(parentName, null);
                            } else {
                                chooseMap.put(parentName, bean);
                            }
                            updateModelView(ll_dialog_model);
                        }
                    });
                    childView.setTextColor(getResources().getColor(R.color.text_black));
                    if (chooseMap.containsValue(bean)) {
                        // 选中状态
                        childView.setBackgroundResource(R.drawable.xml_goods_model_item_check_bg);
                    } else {
                        //  可点击未选中状态
                        childView.setBackgroundResource(R.drawable.xml_search_item_bg);
                    }
                } else {
                    // 不可点击状态
                    childView.setTextColor(getResources().getColor(R.color.text_dark_gray));
                    childView.setBackgroundResource(R.drawable.xml_search_item_bg);
                }
                fvg_model.addView(childView);
            }
        }
    }

    /**
     * @desc 判断当前子分类是可点击
     * @author wangjie
     * @date 2017/3/9 16:24
     */
    private boolean checkIsCanClick(GoodsModelChildBean bean, String parentName) {
        if (chooseMap.containsValue(bean)) {
            // 如果当前默认选中包含该子分类
            return true;
        }
        for (int i = 0; i < groupData.size(); i++) {
            String group = groupData.get(i).getPropertyValue();
            if (checkContains(group, bean.getId(), parentName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param group
     * @param childId
     * @param parentName
     * @desc 判断group组合中是否包含当前分类id，及其他父分类下已选择子分类id
     * @author wangjie
     * @date 2017/3/9 17:41
     */
    private boolean checkContains(String group, String childId, String parentName) {
        if (!group.contains(childId)) {
            return false;
        }
        A:
        for (Map.Entry<String, GoodsModelChildBean> entry : chooseMap.entrySet()) {
            String key = entry.getKey();
            GoodsModelChildBean value = entry.getValue();
            if (parentName.equals(key)) {
                // 如果是当前父分类下同级有选中子分类，跳过
                continue A;
            }
            if (null != value && !group.contains(value.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Map<String, String> key: CHOOSE_IDS 选中的id集合，key: CHOOSE_NAMES 选中的名称集合，多个以,隔开
     * @desc 获取已选择id和name的集合
     * @author wangjie
     * @date 2017/3/9 13:47
     */
    private Map<String, String> getChooseModel() {
        Map<String, String> map = new HashMap<>();
        String chooseIds = "";
        String chooseNames = "";
        for (int i = 0; i < modelData.size(); i++) {
            GoodsModelChildBean bean = chooseMap.get(modelData.get(i).getName());
            if (null != bean) {
                chooseIds += bean.getId() + ",";
                chooseNames += bean.getName() + ",";
            }
        }
        if (chooseIds.endsWith(",")) {
            chooseIds = chooseIds.substring(0, chooseIds.length() - 1);
            chooseNames = chooseNames.substring(0, chooseNames.length() - 1);
        }
        map.put(CHOOSE_IDS, chooseIds);
        map.put(CHOOSE_NAMES, chooseNames);
        return map;
    }

    /**
     * @desc 获取已选中的可用组合
     * @author wangjie
     * @date 2017/3/10 10:38
     */
    private GoodsModelGroupBean getChooseGroup(String chooseIds) {
        for (int i = 0; i < groupData.size(); i++) {
            String group = groupData.get(i).getPropertyValue();
            if (group.equals(chooseIds)) {
                return groupData.get(i);
            }
        }
        return null;
    }

    /**
     * @desc 获取商品详情
     * @author wangjie
     * @date 2017/3/7 17:51
     */
    private void getGoodsDetailInfo() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", goodsId);
        params.put("userId", null == App.app.getUser() ? "" : App.app.getUser().getId());
        httpRequest(Constants.Url.GOODS_DETAIL, params, httpCallBack, 0, this);
    }

    /**
     * @desc 收藏商品(type 0:收藏 1:点赞 2:关注, status 01900001:商品 01900002:话题 01900003:店铺)
     * @author wangjie
     * @date 2017/2/21 13:17
     */
    private void goodsCollect() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", goodsId);
        params.put("type", "0");
        params.put("status", "01900001");
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.COLLECT_INSERT, params, httpCallBack, 2, this);
    }

    /**
     * @desc 取消收藏商品
     * @author wangjie
     * @date 2017/2/21 13:30
     */
    private void goodsCollectCancel() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", goodsId);
        params.put("type", "0");
        params.put("status", "01900001");
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.COLLECT_CANCEL, params, httpCallBack, 3, this);
    }

    /**
     * buyCount 购买数量
     *
     * @desc 添加商品到购物车，mdsePropertyId 组合id，amount 数量
     * @author wangjie
     * @date 2017/3/20 17:39
     */
    private void addToShoppingCart() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("mdsePropertyId", groupId);
        params.put("amount", buyCount + "");
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.SHOPPINGCART_ADD, params, httpCallBack, 4, this);
    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
            DialogUtils.getInstance().dismiss();
            ToastUtil.showToast(R.string.toast_network_fail);
//            v_refresh.onHeaderRefreshComplete();
//            v_refresh.onFooterRefreshComplete();
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            DialogUtils.getInstance().dismiss();
//            v_refresh.onHeaderRefreshComplete();
//            v_refresh.onFooterRefreshComplete();
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            JSONObject obj = jsonObj.optJSONObject("data");
                            // 商品名称
                            String mdseName = obj.optString("mdseName");
                            // 编号id
                            groupId = obj.optString("mdseId");
                            // 店铺id
                            shopId = obj.optString("shopId");
                            // 店铺名称
                            String shopName = obj.optString("shopName");
                            // 店铺logo
                            String shopLogo = obj.optString("shopLogo");
                            // 库存
                            curStock = obj.optInt("repertory", 0);
                            // 价格
                            String price = obj.optString("price");
                            // 评价数量
                            int assessCount = obj.optInt("assessCount", 0);
                            isCollect = !"0".equals(obj.optString("collection"));
                            // 总销量
                            String buyNum = obj.optString("buyNum");
                            // 默认分类
                            String morenfenlei = obj.optString("morenfenlei");
                            // 运费
                            String yunfei = obj.optString("yunfei");
                            // 商品详情
                            String remark = obj.optString("remark");
                            // 评论集合
                            JSONArray assessDto = obj.optJSONArray("assessDto");
                            // 轮播图
                            JSONArray mdseImageDto = obj.optJSONArray("mdseImageDto");
                            // 属性集合
                            JSONArray mdseTypePropertyDtos = obj.optJSONArray
                                    ("mdseTypePropertyDtos");
                            // 推荐商品
                            JSONArray mdseTuijianDtos = obj.optJSONArray("mdseTuijianDtos");
                            // 商品排列组合
                            JSONArray mdsePropertyDtos = obj.optJSONArray("mdsePropertyDtos");

                            tv_goods_name.setText(mdseName);
                            tv_goods_id.setText(String.format(getString(R.string.goods_number),
                                    groupId));
                            tv_goods_price.setText("¥ " + Tools.formatMoney(price));
                            tv_freight.setText(String.format(getString(R.string.goods_freight),
                                    Tools.formatMoney(yunfei)));
                            tv_stock.setText(String.format(getString(R.string.goods_stock),
                                    curStock + ""));
                            Glide.with(GoodsDetailActivity.this).load(shopLogo)
                                    .placeholder(R.drawable.default_loading)
                                    .error(R.drawable.default_loading).dontAnimate()
                                    .into((ImageView) findViewById(R.id.iv_shop_icon));
                            ((TextView) findViewById(R.id.tv_shop_name)).setText(shopName);
                            if (isCollect) {
                                iv_collect.setImageResource(R.drawable.goods_collect_orange_icon);
                            }
                            if (assessCount > 3) {
                                tv_all_appraise.setVisibility(View.VISIBLE);
                            } else {
                                tv_all_appraise.setVisibility(View.GONE);
                            }
                            initWebview(remark);
                            List<AppraiseBean> appraiseData = JsonUtil.JsonToBean(AppraiseBean
                                    .class, assessDto.toString());
                            initAppraiseView(appraiseData);

                            List<GoodsRecommendBean> recommendList = JsonUtil.JsonToBean(
                                    GoodsRecommendBean.class, mdseTuijianDtos.toString());
                            initRecommendView(recommendList, shopName);

                            ArrayList<GoodsBannerBean> bannerList = JsonUtil.JsonToBean(
                                    GoodsBannerBean.class, mdseImageDto.toString());
                            initActivitiesBanner(bannerList);

                            ArrayList<GoodsModelGroupBean> groupList = JsonUtil.JsonToBean(
                                    GoodsModelGroupBean.class, mdsePropertyDtos.toString());
                            if (null != groupList) {
                                groupData.clear();
                                groupData.addAll(groupList);
                            }

                            List<GoodsModelBean> modelList = JsonUtil.JsonToBean(GoodsModelBean
                                    .class, mdseTypePropertyDtos.toString());
                            if (null != modelList) {
                                modelData.clear();
                                modelData.addAll(modelList);
                            }
                            if (!Tools.isNull(morenfenlei)) {
                                for (int i = 0; i < modelData.size(); i++) {
                                    String name = modelData.get(i).getName();
                                    List<GoodsModelChildBean> childList = modelData.get(i)
                                            .getMdseTypePropertyDetailDtos();
                                    A:
                                    for (int j = 0; j < childList.size(); j++) {
                                        if (morenfenlei.contains(childList.get(j).getId()
                                        )) {
                                            chooseMap.put(name, childList.get(j));
                                            break A;
                                        }
                                    }
                                }
                            }
                            Map<String, String> map = getChooseModel();
                            tv_choose.setText(getChooseModel().get(CHOOSE_NAMES));
                            break;

                        case 2:
                            // 收藏
                            isCollect = true;
                            iv_collect.setImageResource(R.drawable.goods_collect_orange_icon);
                            ToastUtil.showToast(R.string.success);
                            break;

                        case 3:
                            // 取消收藏
                            isCollect = false;
                            iv_collect.setImageResource(R.drawable.goods_collect_white_icon);
                            ToastUtil.showToast(R.string.success);
                            break;

                        case 4:
                            ToastUtil.showToast(R.string.success);
                            tv_cart_count.setVisibility(View.VISIBLE);
                            tv_cart_count.setText(Tools.StringToInt(tv_cart_count.getText()
                                    .toString()) + buyCount + "");
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
