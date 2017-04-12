package com.kswl.baimucai.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
import com.kswl.baimucai.activity.base.ImagePagerAdapter;
import com.kswl.baimucai.activity.event.EventAdapter;
import com.kswl.baimucai.activity.event.EventDetailActivity;
import com.kswl.baimucai.activity.goods.GoodsListActivity;
import com.kswl.baimucai.activity.message.MsgListActivity;
import com.kswl.baimucai.activity.search.SearchActivity;
import com.kswl.baimucai.activity.user.UserActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.BannerBean;
import com.kswl.baimucai.bean.ClassifyBean;
import com.kswl.baimucai.bean.EventBean;
import com.kswl.baimucai.interfaces.OnViewPagerItemClickListener;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.JsonUtil;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
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
 * @desc 首页
 * @date 2017/1/16 18:35
 */
public class MainHomeFragment extends BaseFragment implements View.OnClickListener {

    /**
     * vp_activities 活动banner
     * vp_advertising 广告banner
     */
    ViewPager vp_activities, vp_advertising;

    /**
     * ll_activities_indicator 活动banner滑动指示器
     * ll_advertising_indicator 广告banner滑动指示器
     */
    LinearLayout ll_activities_indicator, ll_advertising_indicator;

    RelativeLayout rl_classify_0, rl_classify_1, rl_classify_2;

    PullToRefreshView v_refresh;

    GridView gv_event;

    EventAdapter adapter;

    List<EventBean> data = new ArrayList<>();

    private int curPage = 1;

    OnMainPageChangeListener onMainPageChangeListener;

    public static MainHomeFragment newInstance() {
        MainHomeFragment fragment = new MainHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        v_refresh = (PullToRefreshView) view.findViewById(R.id.v_refresh);
        // 消息
        ImageView iv_msg = (ImageView) view.findViewById(R.id.iv_msg);
        // 搜索
        LinearLayout ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        // 分类更多按钮
        TextView tv_classify_more = (TextView) view.findViewById(R.id.tv_classify_more);
        // 活动banner
        RelativeLayout rl_activities = (RelativeLayout) view.findViewById(R.id.rl_activities);
        vp_activities = (ViewPager) rl_activities.findViewById(R.id.vp_banner);
        ll_activities_indicator =
                (LinearLayout) rl_activities.findViewById(R.id.ll_banner_indicator);
        // 广告banner
        RelativeLayout rl_advertising = (RelativeLayout) view.findViewById(R.id.rl_advertising);
        vp_advertising = (ViewPager) rl_advertising.findViewById(R.id.vp_banner);
        ll_advertising_indicator =
                (LinearLayout) rl_advertising.findViewById(R.id.ll_banner_indicator);
        ll_advertising_indicator.setGravity(Gravity.RIGHT);
        rl_classify_0 = (RelativeLayout) view.findViewById(R.id.rl_classify_0);
        rl_classify_1 = (RelativeLayout) view.findViewById(R.id.rl_classify_1);
        rl_classify_2 = (RelativeLayout) view.findViewById(R.id.rl_classify_2);
        gv_event = (GridView) view.findViewById(R.id.gv_event);

        ll_search.setOnClickListener(this);
        iv_msg.setOnClickListener(this);
        tv_classify_more.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new EventAdapter(mContext, data);
        gv_event.setAdapter(adapter);
        gv_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(Constants.Char.EVENT_DATA, data.get(i));
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getEvent();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getEvent();
            }
        });

        getEvent();
        getBanner();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                // 搜索
                startActivity(new Intent(mContext, SearchActivity.class));
                break;

            case R.id.iv_msg:
                // 消息
                if (null == App.app.getUser()) {
                    startActivity(new Intent(mContext, UserActivity.class));
                    break;
                }
                startActivity(new Intent(mContext, MsgListActivity.class));
                break;

            case R.id.tv_classify_more:
                // 分类更多
                if (onMainPageChangeListener != null) {
                    onMainPageChangeListener.onMainPageChange(1);
                }
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
    private void initActivitiesBanner(ArrayList<BannerBean> urls) {
        if (null != urls && urls.size() > 0) {
            ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(mContext, urls,
                    new OnViewPagerItemClickListener() {
                        @Override
                        public void onClick(View view, int posiition) {
                            ToastUtil.showToast("" + posiition);
                        }
                    });
            vp_activities.setAdapter(pagerAdapter);
            if (urls.size() > 1) {
                // 只有当轮播图多余一张时，才设置滑动滑动指示器
                int indicatorSize = (int) getResources().getDimension(
                        R.dimen.banner_indicator_size);
                for (int i = 0; i < urls.size(); i++) {
                    ImageView indicator = new ImageView(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            indicatorSize, indicatorSize);
                    param.leftMargin = 10;
                    indicator.setLayoutParams(param);
                    indicator.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    indicator.setImageResource(R.drawable.xml_viewpager_indicator_selector);
                    ll_activities_indicator.addView(indicator);
                }
                // 为了在OnPageChangeListener中引用ViewPager
                // 设置指示器中第一项选中
                ll_activities_indicator.getChildAt(0).setSelected(true);
                vp_activities.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int
                            positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // viewpager滑动时更新指示器
                        for (int i = 0; i < vp_activities.getAdapter().getCount(); i++) {
                            ll_activities_indicator.getChildAt(i).setSelected(i == position);
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
     * @author wangjie
     * @date 2017/1/18 17:07
     * @desc 初始化广告banner
     */
    private void initAdvertisingBanner(ArrayList<BannerBean> urls) {
        if (null != urls && urls.size() > 0) {
            ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(mContext, urls);
            vp_advertising.setAdapter(pagerAdapter);
            if (urls.size() > 1) {
                // 只有当轮播图多余一张时，才设置滑动滑动指示器
                int indicatorSize = (int) getResources().getDimension(
                        R.dimen.banner_indicator_size);
                for (int i = 0; i < urls.size(); i++) {
                    ImageView indicator = new ImageView(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            indicatorSize, indicatorSize);
                    param.leftMargin = 10;
                    indicator.setLayoutParams(param);
                    indicator.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    indicator.setImageResource(R.drawable.xml_viewpager_indicator_selector);
                    ll_advertising_indicator.addView(indicator);
                }
                // 为了在OnPageChangeListener中引用ViewPager
                // 设置指示器中第一项选中
                ll_advertising_indicator.getChildAt(0).setSelected(true);
                vp_advertising.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int
                            positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // viewpager滑动时更新指示器
                        for (int i = 0; i < vp_advertising.getAdapter().getCount(); i++) {
                            ll_advertising_indicator.getChildAt(i).setSelected(i == position);
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
     * @desc 初始化分类
     * @author wangjie
     * @date 2017/2/17 16:08
     */
    private void initClassifyView(ArrayList<ClassifyBean> classifyData) {
        if (null != classifyData) {
            switch (classifyData.size()) {
                case 3:
                    initClassifyData(rl_classify_2, classifyData.get(2));

                case 2:
                    initClassifyData(rl_classify_1, classifyData.get(1));

                case 1:
                    initClassifyData(rl_classify_0, classifyData.get(0));

                default:
                    break;
            }
        }
    }

    /**
     * @desc 加载分类数据
     * @author wangjie
     * @date 2017/2/17 17:06
     */
    private void initClassifyData(RelativeLayout rl_classify, ClassifyBean bean) {
        ((TextView) (rl_classify.getChildAt(0))).setText(bean.getName());
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into((ImageView) (rl_classify.getChildAt(1)));
        final String classifyId = bean.getId();
        final String classifyName = bean.getName();
        rl_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsListActivity.class);
                intent.putExtra(Constants.Char.CLASSIFY_ID, classifyId);
                intent.putExtra(Constants.Char.CLASSIFY_NAME, classifyName);
                startActivity(intent);
            }
        });
    }

    /**
     * @desc 获取banner数据
     * @author wangjie
     * @date 2017/2/17 16:06
     */
    private void getBanner() {
        mContext.httpRequest(Constants.Url.GET_HOME_BANNER, null, httpCallBack, 0, this);
        DialogUtils.getInstance().show(mContext);
    }

    /**
     * @desc 获取活动
     * @author wangjie
     * @date 2017/2/20 14:28
     */
    private void getEvent() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        params.put("type", "1");
        mContext.httpRequest(Constants.Url.GET_EVENT, params, httpCallBack, 1, this);
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
                            JSONArray bannerArr = obj.optJSONArray("turnRoundImageDtoList");
                            JSONArray advertArr = obj.optJSONArray("advertDtoList");
                            JSONArray sortArr = obj.optJSONArray("mdseTypeDtoList");
                            ArrayList<BannerBean> bannerList = JsonUtil.JsonToBean(BannerBean.class,
                                    bannerArr.toString());
                            ArrayList<BannerBean> advertList = JsonUtil.JsonToBean(BannerBean.class,
                                    advertArr.toString());
                            ArrayList<ClassifyBean> classifyList = ClassifyBean.jsonToBean(sortArr);
                            initActivitiesBanner(bannerList);
                            initAdvertisingBanner(advertList);
                            initClassifyView(classifyList);
                            break;

                        case 1:
                            JSONArray eventArr = jsonObj.optJSONArray("data");
                            ArrayList<EventBean> eventList = JsonUtil.JsonToBean(EventBean.class,
                                    eventArr.toString());
                            if (curPage == 1) {
                                data.clear();
                            }
                            if (null != eventList) {
                                data.addAll(eventList);
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
