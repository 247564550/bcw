package com.kswl.baimucai.activity.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.address.AddressActivity;
import com.kswl.baimucai.activity.base.BaseActivity;
import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.UserBean;
import com.kswl.baimucai.utils.Constants;
import com.kswl.baimucai.utils.DialogUtils;
import com.kswl.baimucai.utils.GetImg;
import com.kswl.baimucai.utils.LogUtil;
import com.kswl.baimucai.utils.ToastUtil;
import com.kswl.baimucai.utils.Tools;
import com.kswl.baimucai.view.CustomBottomDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;

import okhttp3.Call;

/**
 * @author wangjie
 * @desc 账户信息
 * @date 2017/2/6 15:27
 */
public class AccountShowActivity extends BaseActivity {

    /**
     * 头像
     */
    ImageView iv_portrait;

    /**
     * tv_nickname 昵称<br>
     * tv_email 邮箱<br>
     * tv_company 公司名称<br>
     * tv_sex 性别<br>
     * tv_area 所在地区<br>
     * tv_qq QQ<br>
     * tv_phone 手机
     */
    TextView tv_nickname, tv_email, tv_company, tv_sex, tv_area, tv_qq, tv_phone;

    Dialog dialog;

    GetImg getImg;

    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_show);
        showTopTitle(R.string.account_title);
        showTopLine();

        getImg = new GetImg(this);

        iv_portrait = (ImageView) findViewById(R.id.iv_portrait);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_qq = (TextView) findViewById(R.id.tv_qq);
        tv_phone = (TextView) findViewById(R.id.tv_phone);

        Glide.with(this).load(App.app.getUser().getPhoto())
                .placeholder(R.drawable.default_portrait).error(R.drawable.default_portrait)
                .dontAnimate().into(iv_portrait);
        tv_nickname.setText(App.app.getUser().getNickName());
        tv_email.setText(App.app.getUser().getEmail());
        tv_company.setText(App.app.getUser().getCompanyName());
        tv_sex.setText(App.app.getUser().getSex());
        tv_area.setText(App.app.getUser().getAddress());
        tv_qq.setText(App.app.getUser().getQq());
        tv_phone.setText(App.app.getUser().getPhone());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_portrait:
                // 头像
                showChoosePhotoView();
                break;

            case R.id.rl_nickname:
                // 昵称
                startActivityForResult(new Intent(this, AccountEditActivity.class),
                        Constants.Code.REQUEST_ACCOUNT_NICKNAME);
                break;

            case R.id.rl_email:
                // 邮箱
                startActivityForResult(new Intent(this, AccountEditActivity.class),
                        Constants.Code.REQUEST_ACCOUNT_EMAIL);
                break;

            case R.id.rl_company:
                // 公司名称
                startActivityForResult(new Intent(this, AccountEditActivity.class),
                        Constants.Code.REQUEST_ACCOUNT_COMPANY);
                break;

            case R.id.rl_qq:
                // qq
                startActivityForResult(new Intent(this, AccountEditActivity.class),
                        Constants.Code.REQUEST_ACCOUNT_QQ);
                break;

            case R.id.rl_sex:
                // 性别
                startActivity(new Intent(this, SexActivity.class));
                break;

            case R.id.rl_area:
                // 所在地区
                showChooseAreaView();
                break;

            case R.id.rl_address:
                // 地址管理
                startActivity(new Intent(this, AddressActivity.class));
                break;

            case R.id.rl_pwd:
                // 修改密码
                startActivity(new Intent(this, UpdatePwdActivity.class));
                break;

            default:
                break;
        }
    }

    /**
     * @desc 选择地区
     * @author wangjie
     * @date 2017/2/20 12:52
     */
    private void showChooseAreaView() {
        View view = getLayoutInflater().inflate(R.layout.v_choose_area, null);
        NumberPicker np_province = (NumberPicker) view.findViewById(R.id.np_province);
        NumberPicker np_city = (NumberPicker) view.findViewById(R.id.np_city);
        NumberPicker np_district = (NumberPicker) view.findViewById(R.id.np_district);

        String[] province = {"北京", "上海", "广东省", "广西省", "湖北省", "湖南省"};
        np_province.setDisplayedValues(province);
        np_province.setMinValue(0);
        np_province.setMaxValue(province.length - 1);
        np_province.setValue(0);
        // 设置是否循环滚动。而且必须在setMaxValue()和setMinValue()的后面调用
        np_province.setWrapSelectorWheel(false);

        String[] city = {"北京市", "上海市", "天津市"};
        np_city.setDisplayedValues(city);
        np_city.setMinValue(0);
        np_city.setMaxValue(city.length - 1);
        np_city.setValue(0);
        // 设置是否循环滚动。而且必须在setMaxValue()和setMinValue()的后面调用
        np_city.setWrapSelectorWheel(false);

        String[] district = {"普陀区", "杨浦区", "宝山区", "徐汇区", "黄浦区", "青浦区"};
        np_district.setDisplayedValues(district);
        np_district.setMinValue(0);
        np_district.setMaxValue(district.length - 1);
        np_district.setValue(0);
        // 设置是否循环滚动。而且必须在setMaxValue()和setMinValue()的后面调用
        np_district.setWrapSelectorWheel(false);

        new CustomBottomDialog.Builder(this).setContentView(view)
                .setPositiveButton(null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    /**
     * @return void
     * @Description: 选择照片来源
     * @author wangjie
     * @date 2015-12-28 下午1:44:47
     */
    private void showChoosePhotoView() {
        dialog = new Dialog(this, R.style.DialogFullscreen);
        View view = LayoutInflater.from(this).inflate(R.layout.v_choose_photo, null);
        Button btn_albums = (Button) view.findViewById(R.id.btn_albums);
        Button btn_camera = (Button) view.findViewById(R.id.btn_camera);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getImg.goToGallery();
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getImg.goToCamera();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 此处可以设置dialog显示的位置
        window.setGravity(Gravity.BOTTOM);
        // 添加动画
        window.setWindowAnimations(R.style.dialog_animation);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            String text = null == intent ? "" : intent.getStringExtra(Constants.Char.ACCOUNT_TEXT);
            switch (requestCode) {
                case GetImg.go_to_gallery_code:
                    String path1 = getImg.getGalleryPath(intent);
                    LogUtil.e("相册路径:" + path1);
                    if (Tools.isNull(path1)) {
                        ToastUtil.showToast("照片获取失败");
                        return;
                    }
                    mUri = Uri.fromFile(new File(path1));
                    getImg.gotoCutImage(mUri, 1, 1, 200, 200);
                    break;

                case GetImg.go_to_camera_code:
                    String path2 = getImg.file_save.getAbsolutePath();
                    LogUtil.e("拍照数据:" + path2);
                    mUri = Uri.fromFile(new File(path2));
                    getImg.gotoCutImage(mUri, 1, 1, 200, 200);
                    break;

                case GetImg.go_to_cutimg_code:
                    if (mUri == null) {
                        break;
                    }
                    Bitmap mBitmap = getImg.getBitmapFromUri(mUri);
                    if (mBitmap != null) {
                        uploadUserPhoto(mBitmap);
                    }
                    break;

                case Constants.Code.REQUEST_ACCOUNT_NICKNAME:
                    tv_nickname.setText(text);
                    break;

                case Constants.Code.REQUEST_ACCOUNT_EMAIL:
                    tv_email.setText(text);
                    break;

                case Constants.Code.REQUEST_ACCOUNT_COMPANY:
                    tv_company.setText(text);
                    break;

                case Constants.Code.REQUEST_ACCOUNT_AREA:
                    tv_area.setText(text);
                    break;

                case Constants.Code.REQUEST_ACCOUNT_QQ:
                    tv_qq.setText(text);
                    break;

                case Constants.Code.REQUEST_ACCOUNT_PHONE:
                    tv_phone.setText(text);
                    break;

                default:
                    break;
            }
        }
    }

    private void uploadUserPhoto(Bitmap mBitmap) {
        DialogUtils.getInstance().show(this);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("id", App.app.getUser().getId());
        params.put("photo", GetImg.getByteByBitmap(mBitmap));
        httpRequest(Constants.Url.UPLOAD_USER_PHOTO, params, httpCallBack, 0, this);
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
                            String url = jsonObj.optString("data");
                            Glide.with(AccountShowActivity.this).load(url)
                                    .placeholder(R.drawable.default_portrait)
                                    .error(R.drawable.default_portrait)
                                    .dontAnimate().into(iv_portrait);
                            UserBean user = App.app.getUser();
                            user.setPhoto(url);
                            App.app.setUser(user);
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
