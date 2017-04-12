package com.kswl.baimucai.bean;

import java.io.Serializable;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc
 * @date 2017-2017/2/17-17:21
 */

public class EventBean implements Serializable {

    private String id;

    private String shopId;

    /**
     * 活动名称
     */
    private String name;

    private String imageApp;

    private String activityImageApp;

    /**
     * 活动介绍
     */
    private String abstracts;

    private String startDate;

    private String endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    public String getActivityImageApp() {
        return activityImageApp;
    }

    public void setActivityImageApp(String activityImageApp) {
        this.activityImageApp = activityImageApp;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
