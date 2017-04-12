package com.kswl.baimucai.bean;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc 商品分类组合
 * @date 2017-2017/3/9-14:10
 */

public class GoodsModelGroupBean {

    private String id;

    /**
     * 组合，多个以,隔开
     */
    private String propertyValue;

    /**
     * 库存
     */
    private String amount;

    /**
     * 商品价格
     */
    private String unitPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
