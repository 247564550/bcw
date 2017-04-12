package com.kswl.baimucai.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc
 * @date 2017-2017/2/13-11:57
 */

public class CityBean implements Serializable {

    private String code;

    private String name;

    private ArrayList<CityBean> child;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CityBean> getChild() {
        return child;
    }

    public void setChild(ArrayList<CityBean> child) {
        this.child = child;
    }
}
