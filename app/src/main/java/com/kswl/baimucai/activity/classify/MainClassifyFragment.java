package com.kswl.baimucai.activity.classify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseFragment;
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

/**
 * @author wangjie
 * @desc 分类
 * @date 2017/1/16 18:35
 */
public class MainClassifyFragment extends BaseFragment {

    ListView lv_parent;

    ListView lv_child;

    ClassifyParentAdapter adapterParent;

    ClassifyChildAdapter adapterChild;

    List<ClassifyBean> data = new ArrayList<>();

    List<ClassifyBean> dataChild = new ArrayList<>();

    ImageView iv_child;

    public static MainClassifyFragment newInstance() {
        MainClassifyFragment fragment = new MainClassifyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_classify, container, false);
        lv_parent = (ListView) view.findViewById(R.id.lv_parent);
        lv_child = (ListView) view.findViewById(R.id.lv_child);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFirstClassifyData();
    }

    /**
     * @desc 获取一级分类
     * @author wangjie
     * @date 2017/2/22 10:19
     */
    private void getFirstClassifyData() {
        mContext.httpRequest(Constants.Url.GET_FIRST_CLASSIFY, null, httpCallBack, 0, this);
        DialogUtils.getInstance().show(mContext);
    }

    /**
     * @desc 获取二级及三级分类
     * @author wangjie
     * @date 2017/2/22 10:19
     */
    private void getOtherClassifyData(String parentId) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("type", parentId);
        mContext.httpRequest(Constants.Url.GET_OTHER_CLASSIFY, params, httpCallBack, 1, this);
        DialogUtils.getInstance().show(mContext);
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
                            data = ClassifyBean.jsonToBean(firstArr);
                            if (null != data && data.size() > 0) {
                                adapterParent = new ClassifyParentAdapter(mContext, data);
                                lv_parent.setAdapter(adapterParent);
                                lv_parent.setOnItemClickListener(new AdapterView
                                        .OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View
                                            view, int position, long l) {
                                        adapterParent.setSelectPosition(position);
                                        Glide.with(mContext).load(data.get(position).getImage())
                                                .placeholder(R.drawable.default_loading)
                                                .error(R.drawable.default_loading)
                                                .dontAnimate().into(iv_child);
                                        if (null != data.get(position).getMdseTypeDtoList()) {
                                            dataChild.clear();
                                            dataChild.addAll(data.get(position)
                                                    .getMdseTypeDtoList());
                                            adapterChild.notifyDataSetChanged();
                                        } else {
                                            dataChild.clear();
                                            adapterChild.notifyDataSetChanged();
                                            getOtherClassifyData(data.get(position).getId());
                                        }
                                    }
                                });

                                View headView = LayoutInflater.from(mContext).inflate(R.layout
                                        .item_classify_parent_icon, null);
                                iv_child = (ImageView) headView.findViewById(R.id
                                        .iv_child);
                                Glide.with(mContext).load(data.get(0).getImage())
                                        .placeholder(R.drawable.default_loading)
                                        .error(R.drawable.default_loading)
                                        .dontAnimate().into(iv_child);
                                lv_child.addHeaderView(headView);
                                adapterChild = new ClassifyChildAdapter(mContext, dataChild);
                                lv_child.setAdapter(adapterChild);
                                if (data.get(0).getMdseTypeDtoList() != null
                                        && data.get(0).getMdseTypeDtoList().size() > 0) {
                                    dataChild.clear();
                                    dataChild.addAll(data.get(0).getMdseTypeDtoList());
                                    adapterChild.notifyDataSetChanged();
                                } else {
                                    getOtherClassifyData(data.get(0).getId());
                                }
                            }
                            break;

                        case 1:
                            JSONArray childArr = jsonObj.optJSONArray("data");
                            ArrayList<ClassifyBean> mList = ClassifyBean.jsonToBean(childArr);
                            dataChild.clear();
                            dataChild.addAll(mList);
                            adapterChild.notifyDataSetChanged();
                            // 更新一级分类数据
                            data.get(adapterParent.getSelectPosition()).setMdseTypeDtoList(mList);
                            adapterParent.notifyDataSetChanged();
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
