package com.kswl.baimucai.bean;

import java.io.Serializable;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc 购物车商品类型
 * @date 2017-2017/3/23-13:06
 */

public class CartGoodsTypeBean implements Serializable {

    private String type;

    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
