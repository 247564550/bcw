package com.kswl.baimucai.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc 购物车商品
 * @date 2017-2017/3/21-14:29
 */

public class CartGoodsBean implements Serializable {

    /**
     * 唯一标示
     */
    private String id;

    /**
     * 商品编号
     */
    private String mdseId;

    /**
     * 商品唯一标示
     */
    private String mdsePropertyId;

    /**
     * 商品名称
     */
    private String mdseName;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 单价
     */
    private String unitPrice;

    /**
     * 总价
     */
    private String unitSum;

    /**
     * 购买数量
     */
    private String amount;

    /**
     * 库存
     */
    private String stock;

    private String yunfei;

    private ArrayList<CartGoodsTypeBean> typeDtos;

    private boolean isCheck = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMdseName() {
        return mdseName;
    }

    public void setMdseName(String mdseName) {
        this.mdseName = mdseName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitSum() {
        return unitSum;
    }

    public void setUnitSum(String unitSum) {
        this.unitSum = unitSum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMdsePropertyId() {
        return mdsePropertyId;
    }

    public void setMdsePropertyId(String mdsePropertyId) {
        this.mdsePropertyId = mdsePropertyId;
    }

    public String getMdseId() {
        return mdseId;
    }

    public void setMdseId(String mdseId) {
        this.mdseId = mdseId;
    }

    public String getYunfei() {
        return yunfei;
    }

    public void setYunfei(String yunfei) {
        this.yunfei = yunfei;
    }

    public ArrayList<CartGoodsTypeBean> getTypeDtos() {
        return typeDtos;
    }

    public void setTypeDtos(ArrayList<CartGoodsTypeBean> typeDtos) {
        this.typeDtos = typeDtos;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
