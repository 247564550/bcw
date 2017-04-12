package com.kswl.baimucai.bean;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc
 * @date 2017-2017/2/15-9:13
 */

public class CouponBean {

    private String id;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 面额(分)
     */
    private String denomination;

    /**
     * 限额(分)
     */
    private String quota;

    /**
     * 使用开始时间，豪秒数
     */
    private String useStart;

    /**
     * 使用结束时间,豪秒数
     */
    private String useEnd;

    /**
     * 0:生效 1:失效
     */
    private String isEffect;

    /**
     * 0:未使用1:已使用
     */
    private String useStatus;

    /**
     * 是否已领取(0:未领取1:已领取)
     */
    private String receiveNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getUseStart() {
        return useStart;
    }

    public void setUseStart(String useStart) {
        this.useStart = useStart;
    }

    public String getUseEnd() {
        return useEnd;
    }

    public void setUseEnd(String useEnd) {
        this.useEnd = useEnd;
    }

    public String getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(String isEffect) {
        this.isEffect = isEffect;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(String receiveNum) {
        this.receiveNum = receiveNum;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
