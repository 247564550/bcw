package com.kswl.baimucai.activity.message;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.Call;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.message
 * @desc 加载店家icon
 * @date 2017-2017/3/24-13:12
 */

public class LoadShopIconHelper {

    private ImageView imageView;

    private BaseActivity mContext;

    public void load(Context context, ImageView imageView, String shopId) {
        if (!(context instanceof BaseActivity)) {
            throw new IllegalArgumentException("context must be instance of BaseActivity");
        }
        this.mContext = (BaseActivity) context;
        imageView.setTag(shopId);
        if (TextUtils.isEmpty(shopId)) {
            imageView.setImageResource(R.drawable.default_loading);
            return;
        }
        String value = ShopIconCache.getInstance().getValue(shopId);
        if (value.equals(ShopIconCache.VALUE_LOADING)) {
            return;
        }
        if (!TextUtils.isEmpty(value) && value.equals(ShopIconCache.VALUE_ERR)) {
            Glide.with(mContext).load(value)
                    .placeholder(R.drawable.default_loading).error(R.drawable.default_loading)
                    .dontAnimate().into(imageView);
            return;
        }
        ShopIconCache.getInstance().setValue(shopId, ShopIconCache.VALUE_LOADING);
        this.imageView = imageView;
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("id", shopId);
        mContext.httpRequest(Constants.Url.SHOP_GET_ICON, params, httpCallBack, 0, mContext);

    }

    StringCallback httpCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            LogUtil.e("onError: " + e.toString());
        }

        @Override
        public void onResponse(String response, int id) {
            LogUtil.e("onResponse: " + response);
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status = jsonObj.optString("code");
                String msg = jsonObj.optString("message");
                if (Constants.Char.RESULT_OK.equals(status)) {
                    switch (id) {
                        case 0:
                            JSONObject obj = jsonObj.optJSONObject("data");
                            String name = obj.optString("name");
                            String image = obj.optString("image");

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
