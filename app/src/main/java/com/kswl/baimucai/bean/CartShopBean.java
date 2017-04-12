package com.kswl.baimucai.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc 购物车店铺
 * @date 2017-2017/3/21-14:29
 */

public class CartShopBean implements Serializable {

    private String shopId;

    private String shopName;

    private ArrayList<CartGoodsBean> cartMdseDto;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ArrayList<CartGoodsBean> getCartMdseDto() {
        return cartMdseDto;
    }

    public void setCartMdseDto(ArrayList<CartGoodsBean> cartMdseDto) {
        this.cartMdseDto = cartMdseDto;
    }
}
