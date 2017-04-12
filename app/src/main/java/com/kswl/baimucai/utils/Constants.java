package com.kswl.baimucai.utils;

import java.text.DecimalFormat;

public class Constants {

    /**
     * 格式化数字，加上千分号，保留两位小数点
     */
    public static DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

    /**
     * @author wangjie
     * @desc 数据请求url
     * @date 2016/12/5 15:47
     */
    public static class Url {

        //        private static final String IP = "a7842752.imwork.net:8092";
        private static final String IP = "116.231.117.14:8092";

        public static final String BASE_URL = "http://" + IP + "/ECPartyRest";

//        public static final String ORDER_SOCKET = "ws://" + IP + "/ggtRest/orderConnect/Owner/";

        /**
         * 注册
         **/
        public static final String REGISTER = BASE_URL + "/rest/AppUserRest/register";

        /**
         * 登录
         **/
        public static final String LOGIN = BASE_URL + "/rest/AppUserRest/login";

        /**
         * 忘记密码
         **/
        public static final String FORGET_PWD = BASE_URL + "/rest/AppUserRest/forgetPasswd";

        /**
         * 修改密码
         **/
        public static final String CHANGE_PWD = BASE_URL + "/rest/AppUserRest/changePasswd";

        /**
         * 获取验证码
         **/
        public static final String GET_VERIFY_CODE = BASE_URL + "/rest/AppUserRest/getVerifyCode";

        /**
         * 校验验证码
         **/
        public static final String CHECK_VERIFY_CODE = BASE_URL +
                "/rest/AppUserRest/checkVerifyCode";

        /**
         * 首页轮播图接口
         **/
        public static final String GET_HOME_BANNER = BASE_URL +
                "/rest/MainRest/getTurnRoundImage";

        /**
         * 获取总后台活动
         **/
        public static final String GET_EVENT = BASE_URL +
                "/rest/MainRest/getActivity";

        /**
         * 活动详情
         **/
        public static final String GET_EVENT_DETAIL = BASE_URL +
                "/rest/MainRest/getActivityDetail";

        /**
         * 活动倒计时
         **/
        public static final String GET_EVENT_TIME = BASE_URL +
                "/rest/MainRest/getActivityDetailTime";

        /**
         * 获取一级分类
         **/
        public static final String GET_FIRST_CLASSIFY = BASE_URL +
                "/rest/MdseTypeRest/getOneType";

        /**
         * 获取其他分类
         **/
        public static final String GET_OTHER_CLASSIFY = BASE_URL +
                "/rest/MdseTypeRest/getOtherType";

        /**
         * 修改用户头像
         **/
        public static final String UPLOAD_USER_PHOTO = BASE_URL +
                "/rest/UserRest/uploadUserPhoto";

        /**
         * 修改用户密码
         **/
        public static final String UPDATE_PWD = BASE_URL +
                "/rest/AppUserRest/changePasswd";

        /**
         * 获取地址列表
         **/
        public static final String ADDRESS_LIST = BASE_URL +
                "/rest/AddressRest/getAddressList";

        /**
         * 新建地址
         **/
        public static final String ADDRESS_INSERT = BASE_URL +
                "/rest/AddressRest/createAddress";

        /**
         * 修改地址
         **/
        public static final String ADDRESS_UPDATE = BASE_URL +
                "/rest/AddressRest/updateAddress";

        /**
         * 删除地址
         **/
        public static final String ADDRESS_DELETE = BASE_URL +
                "/rest/AddressRest/deleteAddress";

        /**
         * 设置默认地址
         **/
        public static final String ADDRESS_SET_DEFAULT = BASE_URL +
                "/rest/AddressRest/setDefault";

        /**
         * 获取默认地址
         **/
        public static final String ADDRESS_GET_DEFAULT = BASE_URL +
                "/rest/AddressRest/getDefaultAddress";

        /**
         * 分类获取商品
         **/
        public static final String GOODS_LIST_BY_TYPE = BASE_URL +
                "/rest/MdseTypeRest/getMdseByType";

        /**
         * 搜索商品及获取店铺商品
         **/
        public static final String GOODS_LIST_BY_SEARCH = BASE_URL +
                "/rest/SearchRest/findByMdse";

        /**
         * 历史浏览记录
         **/
        public static final String GOODS_LIST_BY_HISTORY = BASE_URL +
                "/mdse/getMdseHistory";

        /**
         * 获取热搜列表
         **/
        public static final String SEARCH_HOT = BASE_URL +
                "/rest/SearchRest/getHotSearch";

        /**
         * 搜索店铺
         **/
        public static final String SHOP_LIST_BY_SEARCH = BASE_URL +
                "/rest/SearchRest/findByShop";

        /**
         * 店铺详情
         **/
        public static final String SHOP_GET_DETAIL = BASE_URL +
                "/rest/ShopRest/getShopDetail";

        /**
         * 店铺图片
         **/
        public static final String SHOP_GET_ICON = BASE_URL +
                "/mdse/getShopInformation";

        /**
         * 关注(type 0:收藏 1:点赞 2:关注, status 01900001:商品 01900002:话题 01900003:店铺)
         **/
        public static final String COLLECT_INSERT = BASE_URL +
                "/rest/CollectionRest/addCollection";

        /**
         * 取消关注
         **/
        public static final String COLLECT_CANCEL = BASE_URL +
                "/rest/CollectionRest/delCollection";

        /**
         * 收藏列表
         **/
        public static final String COLLECT_LIST_OWN = BASE_URL +
                "/rest/CollectionRest/getMyCollection";

        /**
         * 领取优惠券
         **/
        public static final String COUPON_INSERT = BASE_URL +
                "/rest/CouponRest/getCouponUser";

        /**
         * 我的优惠券
         **/
        public static final String COUPON_LIST_OWN = BASE_URL +
                "/rest/AboutUsRest/getMyCoupon";

        /**
         * 删除优惠券
         **/
        public static final String COUPON_DELETE = BASE_URL +
                "/rest/CouponRest/delCouponUser";

        /**
         * 常见问题-列表
         **/
        public static final String PROBLEM_LIST = BASE_URL +
                "/rest/CommProblemRest/getList";

        /**
         * 常见问题-详情
         **/
        public static final String PROBLEM_DETAIL = BASE_URL +
                "/rest/CommProblemRest/getAnswer";

        /**
         * 关于我们-详情
         **/
        public static final String ABOUT_DETAIL = BASE_URL +
                "/rest/AboutUsRest/getInfoMain";

        /**
         * 商品详情
         **/
        public static final String GOODS_DETAIL = BASE_URL +
                "/mdse/getMdseDetail";

        /**
         * 购物车列表
         **/
        public static final String SHOPPINGCART_LIST = BASE_URL +
                "/shoppingCart/myCart";

        /**
         * 添加商品到购物车
         **/
        public static final String SHOPPINGCART_ADD = BASE_URL +
                "/shoppingCart/addCart";

        /**
         * 删除购物车单品
         **/
        public static final String SHOPPINGCART_DELETE = BASE_URL +
                "/shoppingCart/delete";

        /**
         * 删除购物车商品-多选
         **/
        public static final String SHOPPINGCART_DELETE_SELECT = BASE_URL +
                "/shoppingCart/deleteSelectedItem";

        /**
         * 更改购物车商品数量
         **/
        public static final String SHOPPINGCART_CHANGE_COUNT = BASE_URL +
                "/shoppingCart/changePurchaseQuantity";

        /**
         * 搜索招标信息
         **/
        public static final String TENDER_SEARCH = BASE_URL +
                "/rest/TenderRest/getTenders";

        /**
         * 招标类型
         **/
        public static final String TENDER_TYPES = BASE_URL +
                "/rest/TenderRest/getTenderTypes";

        /**
         * 招标详情
         **/
        public static final String TENDER_DETAIL = BASE_URL +
                "/rest/TenderRest/getTenderDetail";

        /**
         * 招标内容
         **/
        public static final String TENDER_CONTENT = BASE_URL +
                "/rest/TenderRest/getTenderContent";

    }

    /**
     * @author wangjie
     * @desc 数字常量
     * @date 2016/12/5 15:46
     */
    public static class Code {

        /**
         * 用户注册协议
         */
        public static final int TEXT_PROTOCOL = 100;

        /**
         * 问题解答
         */
        public static final int TEXT_PROBLEM = 101;

        /**
         * 网站介绍
         */
        public static final int WEB_SITES = 102;

        /**
         * 公司介绍
         */
        public static final int WEB_COMPANY = 103;

        /**
         * 法律声明
         */
        public static final int WEB_LEGAL_NOTICES = 104;

        /**
         * 账户信息编辑-昵称
         */
        public static final int REQUEST_ACCOUNT_NICKNAME = 200;

        /**
         * 账户信息编辑-邮箱
         */
        public static final int REQUEST_ACCOUNT_EMAIL = 201;

        /**
         * 账户信息编辑-公司名称
         */
        public static final int REQUEST_ACCOUNT_COMPANY = 202;

        /**
         * 账户信息编辑-所在地区
         */
        public static final int REQUEST_ACCOUNT_AREA = 203;

        /**
         * 账户信息编辑-qq
         */
        public static final int REQUEST_ACCOUNT_QQ = 204;

        /**
         * 账户信息编辑-手机
         */
        public static final int REQUEST_ACCOUNT_PHONE = 205;

        /**
         * 订单类型-待付款
         */
        public static final int REQUEST_ORDER_PAYMENT = 206;

        /**
         * 订单类型-待发货
         */
        public static final int REQUEST_ORDER_SEND = 207;

        /**
         * 订单类型-待收货
         */
        public static final int REQUEST_ORDER_RECEIVE = 208;

        /**
         * 订单类型-退货订单
         */
        public static final int REQUEST_ORDER_REFUND = 209;

        /**
         * 订单类型-已完成订单
         */
        public static final int REQUEST_ORDER_COMPLETE = 210;

        /**
         * 订单类型-全部订单
         */
        public static final int REQUEST_ORDER_ALL = 211;

        /**
         * 优惠券类型-选择优惠券
         */
        public static final int REQUEST_COUPON_CHOOSE = 212;

        /**
         * 优惠券类型-我的优惠券
         */
        public static final int REQUEST_COUPON_OWN = 213;

        /**
         * 地址类型- 我的地址管理
         */
        public static final int REQUEST_ADDRESS_OWN = 214;

        /**
         * 地址类型- 订单选择地址
         */
        public static final int REQUEST_ADDRESS_CHOOSE = 215;

        /**
         * 商品列表选择城市
         */
        public static final int REQUEST_CHOOSE_CITY = 216;

        /**
         * 店铺详情页去登录
         */
        public static final int REQUEST_SHOP_LOGIN = 217;

        /**
         * 店铺详情分类筛选商品
         */
        public static final int REQUEST_SHOP_FILTER = 218;

        /**
         * 招标筛选
         */
        public static final int REQUEST_TENDER_FILTER = 219;

    }

    /**
     * @author wangjie
     * @desc 字符常量
     * @date 2016/12/5 15:46
     */
    public static class Char {

        /**
         * http请求成功
         */
        public static final String RESULT_OK = "0000";

        /**
         * 跳转FragmentActivity时默认tag
         */
        public static final String FRAGMENT_INTENT_TAG = "fragmentIntentTag";

        /**
         * 跳转纯文字界面类型
         */
        public static final String INTENT_TEXT_TYPE = "intentTextType";

        /**
         * 跳转富文本界面类型
         */
        public static final String INTENT_WEB_TYPE = "intentWebType";

        /**
         * 账户信息文字
         */
        public static final String ACCOUNT_TEXT = "accountText";

        /**
         * 订单类型
         */
        public static final String ORDER_TYPE = "orderType";

        /**
         * 订单商品信息
         */
        public static final String ORDER_GOODS_INFO = "orderGoodsInfo";

        /**
         * 搜索关键字
         */
        public static final String SEARCH_KEY = "searchKey";

        /**
         * 优惠券类型
         */
        public static final String COUPON_TYPE = "couponType";

        /**
         * 地址类型,true ：选择地址，false ：地址管理
         */
        public static final String ADDRESS_TYPE = "addressType";

        /**
         * 地址数据
         */
        public static final String ADDRESS_DATA = "addressData";

        /**
         * 活动数据
         */
        public static final String EVENT_DATA = "eventDate";

        /**
         * 分类id
         */
        public static final String CLASSIFY_ID = "classifyId";

        /**
         * 分类名称
         */
        public static final String CLASSIFY_NAME = "classifyName";

        /**
         * 商品id
         */
        public static final String GOODS_ID = "goodsId";

        /**
         * 店铺id
         */
        public static final String SHOP_ID = "shopId";

        /**
         * 城市数据
         */
        public static final String CITY_DATA = "cityData";

        /**
         * 常见问题id
         */
        public static final String PROBLEM_ID = "problemId";

        /**
         * 招标id
         */
        public static final String TENDER_ID = "tenderId";

        /**
         * 招标筛选
         */
        public static final String TENDER_FILTER = "tenderFilter";

        public static final String TENDER_DD = "&&";

    }

    /**
     * @author wangjie
     * @desc fragment提交标示tag
     * @date 2017/1/17 12:44
     */
    public static class Tag {

        /**
         * 登录
         */
        public static final String USER_LOGIN = "userLogin";

        /**
         * 注册
         */
        public static final String USER_REGISTER = "userRegister";

    }


}
