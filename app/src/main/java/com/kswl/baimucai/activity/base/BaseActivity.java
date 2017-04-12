package com.kswl.baimucai.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kswl.baimucai.R;
import com.kswl.baimucai.app.AppManager;
import com.kswl.baimucai.interfaces.OnFragmentInteractionListener;
import com.kswl.baimucai.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

public class BaseActivity extends FragmentActivity implements OnFragmentInteractionListener {

    View parentView;

    FrameLayout fl_contain;

    ImageView iv_left_btn, iv_right_btn;

    TextView tv_top_title, tv_right_btn;

    RelativeLayout rl_top;

    View v_title_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        // enterAnim 是下一界面进入效果的xml文件的id,exitAnim是当前界面退出效果的xml文件id。
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_alpha_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_alpha_out);
    }

    @Override
    public void finish() {
        super.finish();
        // enterAnim 是下一界面进入效果的xml文件的id,exitAnim是当前界面退出效果的xml文件id。
        overridePendingTransition(0, R.anim.slide_right_out);// 动画效果
    }

    private void initView() {
        parentView = getLayoutInflater().inflate(R.layout.activity_base, null);
        fl_contain = (FrameLayout) parentView.findViewById(R.id.fl_contain);
        iv_left_btn = (ImageView) parentView.findViewById(R.id.iv_left_btn);
        iv_right_btn = (ImageView) parentView.findViewById(R.id.iv_right_btn);
        tv_top_title = (TextView) parentView.findViewById(R.id.tv_top_title);
        tv_right_btn = (TextView) parentView.findViewById(R.id.tv_right_btn);
        rl_top = (RelativeLayout) parentView.findViewById(R.id.rl_top);
        v_title_line = parentView.findViewById(R.id.v_title_line);

        iv_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeftBtnClick();
            }
        });

        iv_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightImgBtnClick();
            }
        });

        tv_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightTextBtnClick();
            }
        });
    }

    public void hideTitle() {
        rl_top.setVisibility(View.GONE);
    }

    /**
     * 标题栏左边按钮点击事件
     */
    public void onLeftBtnClick() {
        finish();
    }

    /**
     * 标题栏右边图片按钮点击事件
     */
    public void onRightImgBtnClick() {

    }

    /**
     * 标题栏右边文字按钮点击事件
     */
    public void onRightTextBtnClick() {

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (null == parentView) {
            initView();
        }
        View childView = getLayoutInflater().inflate(layoutResID, null);
        fl_contain.removeAllViews();
        fl_contain.addView(childView);
        super.setContentView(parentView);
    }

    @Override
    public void setContentView(View view) {
        if (null == parentView) {
            initView();
        }
        fl_contain.removeAllViews();
        fl_contain.addView(view);
        super.setContentView(parentView);
    }

    /**
     * 设置当前界面视图
     *
     * @param layoutResID 视图id
     * @param isNoParent  是否需要公共标题
     */
    public void setContentView(@LayoutRes int layoutResID, boolean isNoParent) {
        if (isNoParent) {
            super.setContentView(layoutResID);
        } else {
            setContentView(layoutResID);
        }
    }

    /**
     * 设置当前界面视图
     *
     * @param view       视图view
     * @param isNoParent 是否需要公共标题
     */
    public void setContentView(View view, boolean isNoParent) {
        if (isNoParent) {
            super.setContentView(view);
        } else {
            setContentView(view);
        }
    }

    /**
     * TODO(这里用一句话描述这个方法的作用) 展示顶部菜单标题
     *
     * @param title 标题文字
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:27:23
     */
    protected void showTopTitle(String title) {
        tv_top_title.setVisibility(View.VISIBLE);
        tv_top_title.setText(title);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示顶部菜单标题
     *
     * @param resid 标题资源id
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:29:40
     */
    protected void showTopTitle(int resid) {
        tv_top_title.setVisibility(View.VISIBLE);
        tv_top_title.setText(resid);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)隐藏顶部菜单标题
     *
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:30:05
     */
    protected void hideTopTitle() {
        tv_top_title.setVisibility(View.GONE);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示右边图片按钮、使用默认图片
     *
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:25
     */
    protected void showRightImg() {
        iv_right_btn.setVisibility(View.VISIBLE);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示右边图片按钮、可设置图片
     *
     * @param resid 图片资源id
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:08
     */
    protected void showRightImg(int resid) {
        iv_right_btn.setVisibility(View.VISIBLE);
        iv_right_btn.setImageResource(resid);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)隐藏右边图片按钮
     *
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:27:05
     */
    protected void hideRightImg() {
        iv_right_btn.setVisibility(View.GONE);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示右边文字按钮、使用默认文字
     *
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:25
     */
    protected void showRightText() {
        tv_right_btn.setVisibility(View.VISIBLE);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示右边文字按钮、可改变文字
     *
     * @param resid 文字资源id
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:08
     */
    protected void showRightText(int resid) {
        tv_right_btn.setVisibility(View.VISIBLE);
        tv_right_btn.setText(resid);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示右边文字按钮、可改变文字
     *
     * @param s 文字
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:08
     */
    protected void showRightText(String s) {
        tv_right_btn.setVisibility(View.VISIBLE);
        tv_right_btn.setText(s);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)隐藏右边文字按钮
     *
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:27:05
     */
    protected void hideRightText() {
        tv_right_btn.setVisibility(View.GONE);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示左边图片按钮、使用默认图片
     *
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:25
     */
    protected void showLeftImg() {
        iv_left_btn.setVisibility(View.VISIBLE);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)展示左边图片按钮、可设置图片
     *
     * @param resid 图片资源id
     * @return void
     * @author wangj@bluemobi.sh.cn 2014-10-15 下午2:26:08
     */
    protected void showLeftImg(int resid) {
        iv_left_btn.setVisibility(View.VISIBLE);
        iv_left_btn.setImageResource(resid);
    }

    /**
     * TODO(这里用一句话描述这个方法的作用)隐藏左边图片按钮
     *
     * @return void
     * @author wj 2014-10-15 下午2:27:05
     */
    protected void hideLeftImg() {
        iv_left_btn.setVisibility(View.GONE);
    }

    protected void showTopLine() {
        if (v_title_line != null) {
            v_title_line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFragmentStart(BaseFragment fragment) {

    }

    @Override
    public void onFragmentStartForResult(BaseFragment fragment, int
            requestCode) {

    }

    @Override
    public void onFragmentFinish() {

    }

    public void httpRequest(String url, String jsonParams, Callback callback,
                            int id, Object tag, long timeout) {
        LogUtil.e("Request", "[class=" + (tag == null ? "null" : tag.getClass()) +
                ", url=" + url + ", params=" + jsonParams + "]");
        OkHttpUtils.postString().id(id).url(url)
                .content(jsonParams)
                .mediaType(MediaType.parse("application/json;charset=utf-8")).tag(tag).build()
                .readTimeOut(timeout).writeTimeOut(timeout).execute(callback);
    }

    /**
     * @param url      请求地址
     * @param params   参数
     * @param callback 请求回调
     * @param id       请求标示
     * @param tag      绑定当前activity或者fragment，随着tag同步销毁网络请求
     * @desc 网络请求
     * @author wangjie
     * @date 2016/12/26 10:59
     */
    public void httpRequest(String url, LinkedHashMap<String, String> params, Callback callback,
                            int id, Object tag) {
        httpRequest(url, toJsonString(params), callback, id, tag, 15_000L);
    }

    public void httpRequest(String url, LinkedHashMap<String, String> params, Callback callback,
                            int id, Object tag, long timeout) {
        httpRequest(url, toJsonString(params), callback, id, tag, timeout);
    }

    private String toJsonString(LinkedHashMap<String, String> params) {
        String values = "";
        JSONObject jsonObject = new JSONObject();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                Object obj = null;
                try {
                    Object object = new JSONTokener(value).nextValue();
                    if (object instanceof JSONArray) {
                        obj = (JSONArray) object;
                    } else if (object instanceof JSONObject) {
                        obj = (JSONObject) object;
                    } else {
                        obj = value;
                    }
                } catch (Exception e) {
                    obj = value;
                }
                try {
                    jsonObject.put(entry.getKey(), obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        values = jsonObject.toString();
        return values;
    }
}
