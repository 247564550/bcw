package com.kswl.baimucai.activity.event;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.activity.goods.GoodsDetailActivity;
import com.kswl.baimucai.activity.shop.ShopGoodsAdapter;
import com.kswl.baimucai.bean.EventBean;
import com.kswl.baimucai.bean.GoodsBean;
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

public class EventDetailActivity extends BaseActivity {

    PullToRefreshView v_refresh;

    GridView gv_goods;

    TextView tv_day, tv_hour, tv_minute;

    ShopGoodsAdapter adapter;

    List<GoodsBean> data = new ArrayList<>();

    private int curPage = 1;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        showTopTitle(R.string.event_title);
        v_refresh = (PullToRefreshView) findViewById(R.id.v_refresh);
        gv_goods = (GridView) findViewById(R.id.gv_goods);
        ImageView iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_minute = (TextView) findViewById(R.id.tv_minute);

        EventBean bean = (EventBean) getIntent().getSerializableExtra(Constants.Char.EVENT_DATA);
        Glide.with(this).load(bean.getActivityImageApp())
                .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                .dontAnimate().into(iv_icon);
        id = bean.getId();

        adapter = new ShopGoodsAdapter(this, data);
        gv_goods.setAdapter(adapter);
        gv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra(Constants.Char.GOODS_ID, data.get(position).getId());
                startActivity(intent);
            }
        });

        v_refresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                curPage++;
                getEventGoods();
            }
        });
        v_refresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                curPage = 1;
                getEventGoods();
            }
        });

        getEventTime();
        getEventGoods();
    }

    /**
     * @desc 获取活动商品
     * @author wangjie
     * @date 2017/2/20 14:33
     */
    private void getEventGoods() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        params.put("curPage", "" + curPage);
        params.put("pageSize", "10");
        httpRequest(Constants.Url.GET_EVENT_DETAIL, params, httpCallBack, 0, this);
    }

    /**
     * @desc 获取活动倒计时
     * @author wangjie
     * @date 2017/2/20 15:13
     */
    private void getEventTime() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", id);
        httpRequest(Constants.Url.GET_EVENT_TIME, params, httpCallBack, 1, this);
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
                            JSONArray arr = jsonObj.optJSONArray("data");
                            ArrayList<GoodsBean> mList = JsonUtil.JsonToBean(GoodsBean.class, arr
                                    .toString());
                            if (curPage == 1) {
                                data.clear();
                            }
                            if (null != mList) {
                                data.addAll(mList);
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        case 1:
                            long time = jsonObj.optLong("data", 0L);
                            timer = new TimeCount(time, 60000);
                            // 开始计时
                            timer.start();
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


    /**
     * 自定义计时器
     */
    private TimeCount timer;

    /**
     * @author wangjie
     * @desc 倒数计时器
     * @date 2017/2/16 17:22
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            /*
             * 参数依次为总时长,和计时的时间间隔
			 */
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            /*
             * 计时完毕时触发
			 */
            tv_day.setText("00");
            tv_hour.setText("00");
            tv_minute.setText("00");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            /*
             * 计时过程显示
			 */
            long millis = millisUntilFinished / 1000;
            int seconds = (int) (millis % 60);
            millis /= 60;
            int minute = (int) (millis % 60);
            millis /= 60;
            int hour = (int) (millis % 24);
            millis /= 24;
            int day = (int) millis;
            tv_day.setText(format(day));
            tv_hour.setText(format(hour));
            tv_minute.setText(format(minute));
        }

        private String format(int i) {
            if (i < 10) {
                return "0" + i;
            }
            return "" + i;
        }
    }
}
