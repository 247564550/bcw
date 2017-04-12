package com.kswl.baimucai.bean;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc 商品类型-子类
 * @date 2017-2017/3/8-17:17
 */

public class GoodsModelChildBean {

    private String id;

    private String name;

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

    @Override
    public boolean equals(Object obj) {
        if (null != obj && obj instanceof GoodsModelChildBean) {
            return ((GoodsModelChildBean) obj).getId().equals(id);
        }
        return false;
    }
}
