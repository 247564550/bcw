package com.kswl.baimucai.activity.coupon;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.CouponBean;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

public class CouponListOwnActivity extends BaseActivity {

    ListView lv_coupon;

    CouponOwnAdapter adapter;

    List<CouponBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        showTopTitle(R.string.coupon_own_title);
        showRightText(R.string.delete);
        showTopLine();
        lv_coupon = (ListView) findViewById(R.id.lv_coupon);

        adapter = new CouponOwnAdapter(this, data);
        lv_coupon.setAdapter(adapter);
        lv_coupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CouponBean couponBean = data.get(i);
                if (!Tools.isNull(couponBean.getId())) {
                    if (adapter.getSelectSet().contains(couponBean)) {
                        adapter.getSelectSet().remove(couponBean);
                    } else {
                        adapter.getSelectSet().add(couponBean);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        getOwnCoupon();
    }

    @Override
    public void onRightTextBtnClick() {
        if (adapter.getSelectSet().size() > 0) {
            Iterator<CouponBean> it = adapter.getSelectSet().iterator();
            String couponIds = "";
            while (it.hasNext()) {
                CouponBean bean = it.next();
                couponIds += "," + bean.getId();
            }
            couponIds = couponIds.substring(1);
            removeOwnCoupon(couponIds);
        }
    }

    /**
     * @desc 获取我的优惠券
     * @author wangjie
     * @date 2017/2/21 16:53
     */
    private void getOwnCoupon() {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        httpRequest(Constants.Url.COUPON_LIST_OWN, params, httpCallBack, 0, this);
    }

    /**
     * @param couponIds 优惠券id，多个用逗号分隔
     * @desc 删除优惠券
     * @author wangjie
     * @date 2017/2/22 9:54
     */
    private void removeOwnCoupon(String couponIds) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userId", App.app.getUser().getId());
        params.put("couponId", couponIds);
        httpRequest(Constants.Url.COUPON_DELETE, params, httpCallBack, 1, this);
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
                            // 有效
                            JSONArray validArr = obj.optJSONArray("effectCouponList");
                            // 失效
                            JSONArray invalidArr = obj.optJSONArray("invalidCouponList");
                            ArrayList<CouponBean> validList = JsonUtil.JsonToBean(CouponBean.class,
                                    validArr.toString());
                            ArrayList<CouponBean> invalidList = JsonUtil.JsonToBean(CouponBean
                                    .class, invalidArr.toString());
                            if (null != validList && validList.size() > 0) {
                                data.addAll(validList);
                            }
                            if (null != invalidList && invalidList.size() > 0) {
                                data.add(new CouponBean());
                                data.addAll(invalidList);
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        case 1:
                            data.removeAll(adapter.getSelectSet());
                            adapter.getSelectSet().clear();
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
