package com.kswl.baimucai.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author wangjie
 * @package com.kswl.baimucai.bean
 * @desc
 * @date 2017-2017/2/17-15:52
 */

public class ClassifyBean {

    private String id;

    /**
     * 分类
     */
    private String name;

    /**
     * 分类图片
     */
    private String image;

    /**
     * 父类id
     */
    private String parentId;

    private String typeSort;

    /**
     * 分类级别
     */
    private String level;

    /**
     * 子分类
     */
    private ArrayList<ClassifyBean> mdseTypeDtoList;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTypeSort() {
        return typeSort;
    }

    public void setTypeSort(String typeSort) {
        this.typeSort = typeSort;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<ClassifyBean> getMdseTypeDtoList() {
        return mdseTypeDtoList;
    }

    public void setMdseTypeDtoList(ArrayList<ClassifyBean> mdseTypeDtoList) {
        this.mdseTypeDtoList = mdseTypeDtoList;
    }

    @Override
    public String toString() {
        return null == name ? "" : name;
    }

    /**
     * 解析实体类
     *
     * @param jsonArray
     * @return
     */
    public static ArrayList<ClassifyBean> jsonToBean(JSONArray jsonArray) {
        ArrayList<ClassifyBean> mList = null;
        if (null != jsonArray) {
            mList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.optJSONObject(i);
                ClassifyBean bean = new ClassifyBean();
                bean.setId(obj.optString("id"));
                bean.setName(obj.optString("name"));
                bean.setImage(obj.optString("image"));
                bean.setParentId(obj.optString("parentId"));
                bean.setTypeSort(obj.optString("typeSort"));
                bean.setLevel(obj.optString("level"));
                JSONArray childArr = obj.optJSONArray("mdseTypeDtoList");
                bean.setMdseTypeDtoList(jsonToBean(childArr));
                mList.add(bean);
            }
        }
        return mList;
    }
}
