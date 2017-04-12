package com.kswl.baimucai.bean;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc
 * @date 2017-2017/2/20-14:42
 */

public class GoodsBean {

    /**
     *
     */
    private String id;


    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 价格
     */
    private String unitPrice;

    /**
     * 数量
     */
    private String amount;

    /**
     * 评论数
     */
    private String assessNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAssessNum() {
        return assessNum;
    }

    public void setAssessNum(String assessNum) {
        this.assessNum = assessNum;
    }
}
