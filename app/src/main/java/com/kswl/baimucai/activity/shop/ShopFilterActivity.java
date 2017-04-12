package com.kswl.baimucai.activity.shop;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.ClassifyBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

public class ShopFilterActivity extends BaseActivity {

    ListView lv_content;

    ShopFilterAdapter adapter;

    ImageView iv_last;

    TextView tv_cancel, tv_title, tv_confirm;

    /**
     * 1: 第一级分类，2：第二级分类，3：第三级分类
     */
    private int type = 1;

    List<ClassifyBean> data = new ArrayList<>();

    /**
     * 记录一级分类和二级分类选中position
     */
    private int selectFirst = -1, selectSecond = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_filter, true);
        lv_content = (ListView) findViewById(R.id.lv_content);
        iv_last = (ImageView) findViewById(R.id.iv_last);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);

        adapter = new ShopFilterAdapter(this, null);
        lv_content.setAdapter(adapter);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (type) {
                    case 1:
                        // 当前是一级分类点击
                        selectFirst = position;
                        ClassifyBean bean1 = adapter.getItem(selectFirst);
                        tv_title.setText(bean1.getName());
                        List<ClassifyBean> child1 = bean1.getMdseTypeDtoList();
                        if (child1 == null || child1.size() == 0) {
                            getOtherClassifyData(bean1.getId());
                        }
                        type = 2;
                        adapter.setData(type, child1);

                        tv_cancel.setVisibility(View.GONE);
                        iv_last.setVisibility(View.VISIBLE);
                        ObjectAnimator anim = ObjectAnimator.ofFloat(lv_content, "translationX",
                                App.app.getScreenWidth(), 0f);
                        anim.setDuration(200);
                        anim.start();
                        break;

                    case 2:
                        // 当前是二级分类点击
                        selectSecond = position;
                        ClassifyBean bean2 = adapter.getItem(selectSecond);
                        tv_title.setText(bean2.getName());
                        adapter.setSelectPosition(-1);
                        List<ClassifyBean> child2 = bean2.getMdseTypeDtoList();
                        type = 3;
                        adapter.setData(type, child2);

                        tv_confirm.setVisibility(View.VISIBLE);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(lv_content, "translationX",
                                App.app.getScreenWidth(), 0f);
                        anim2.setDuration(200);
                        anim2.start();
                        break;

                    case 3:
                        // 当前是三级分类点击
                        adapter.setSelectPosition(position);
                        adapter.notifyDataSetChanged();
                        break;

                    default:
                        break;
                }
            }
        });

        getFirstClassifyData();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_transparent:
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.iv_last:
                if (selectSecond >= 0) {
                    // 当前是三级分类返回二级分类
                    selectSecond = -1;
                    type = 2;
                    adapter.setData(type, data.get(selectFirst).getMdseTypeDtoList());
                    tv_title.setText(data.get(selectFirst).getName());
                    tv_confirm.setVisibility(View.GONE);
                } else if (selectFirst >= 0) {
                    // 当前是二级分类返回一级分类
                    selectFirst = -1;
                    type = 1;
                    adapter.setData(type, data);
                    tv_title.setText(R.string.shop_goods_sort);
                    iv_last.setVisibility(View.GONE);
                    tv_cancel.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_confirm:
                if (adapter.getSelectPosition() >= 0) {
                    ClassifyBean bean = adapter.getItem(adapter.getSelectPosition());
                    setResult(RESULT_OK, new Intent().putExtra(Constants.Char.CLASSIFY_ID, bean
                            .getId()));
                    finish();
                }
                break;

            default:
                break;
        }
    }

    /**
     * @desc 获取一级分类
     * @author wangjie
     * @date 2017/2/22 10:19
     */
    private void getFirstClassifyData() {
        httpRequest(Constants.Url.GET_FIRST_CLASSIFY, null, httpCallBack, 0, this);
        DialogUtils.getInstance().show(this);
    }

    /**
     * @desc 获取二级及三级分类
     * @author wangjie
     * @date 2017/2/22 10:19
     */
    private void getOtherClassifyData(String parentId) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("type", parentId);
        httpRequest(Constants.Url.GET_OTHER_CLASSIFY, params, httpCallBack, 1, this);
        DialogUtils.getInstance().show(this);
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
                            JSONArray firstArr = jsonObj.optJSONArray("data");
                            ArrayList<ClassifyBean> parentList = ClassifyBean.jsonToBean(firstArr);
                            if (null != parentList && parentList.size() > 0) {
                                data.addAll(parentList);
                                adapter.setData(type, data);
                            }
                            break;

                        case 1:
                            JSONArray childArr = jsonObj.optJSONArray("data");
                            ArrayList<ClassifyBean> childList = ClassifyBean.jsonToBean(childArr);
                            if (null != childList && childList.size() > 0) {
                                adapter.setData(type, childList);
                                // 更新分类数据
                                data.get(selectFirst).setMdseTypeDtoList(childList);
                            }
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
