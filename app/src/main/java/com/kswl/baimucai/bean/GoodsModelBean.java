package com.kswl.baimucai.bean;

import java.util.ArrayList;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc 商品类型
 * @date 2017-2017/3/8-17:17
 */
public class GoodsModelBean {

    private String name;

    private ArrayList<GoodsModelChildBean> mdseTypePropertyDetailDtos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GoodsModelChildBean> getMdseTypePropertyDetailDtos() {
        return mdseTypePropertyDetailDtos;
    }

    public void setMdseTypePropertyDetailDtos(ArrayList<GoodsModelChildBean>
                                                      mdseTypePropertyDetailDtos) {
        this.mdseTypePropertyDetailDtos = mdseTypePropertyDetailDtos;
    }
}
