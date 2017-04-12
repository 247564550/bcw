package com.kswl.baimucai.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kswl.baimucai.app.App;
import com.kswl.baimucai.bean.CityBean;
import com.kswl.baimucai.bean.FilterBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjie
 * @package com.kswl.baimucai.utils
 * @desc
 * @date 2017-2017/3/24-17:22
 */

public class DBCityHelper {

    SQLiteDatabase db;

    public static final String path = Tools.getCachePath(App.app) + "/BXHArea.db";

    private static DBCityHelper instance;

    private DBCityHelper() {
    }

    public static DBCityHelper getInstance() {
        if (null == instance) {
            synchronized (DBCityHelper.class) {
                instance = new DBCityHelper();
            }
        }
        return instance;
    }

    public void openDB() {
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    private boolean isOpen() {
        if (null != db && db.isOpen())
            return true;
        return false;
    }

    public void closeDB() {
        if (null != db) {
            db.close();
        }
    }

    public List<CityBean> getProvList() {
        if (!isOpen()) {
            throw new NullPointerException("must be open SQLiteDatabase");
        }
        List<CityBean> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "area_level = ?", new String[]{"0"});
        while (cursor.moveToNext()) {
            String provId = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
            String provName = cursor.getString(1);//获取第二列的值
            CityBean provBean = new CityBean();
            provBean.setCode(provId);
            provBean.setName(provName);
            mList.add(provBean);
        }
        cursor.close();
        return mList;
    }

    public String[] getProvArray() {
        if (!isOpen()) {
            throw new NullPointerException("must be open SQLiteDatabase");
        }
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "area_level = ?", new String[]{"0"});
        String[] arr = new String[cursor.getCount()];
        int index = 0;
        while (cursor.moveToNext()) {
            String provName = cursor.getString(0);
            arr[index] = provName;
            index++;
        }
        cursor.close();
        return arr;
    }

    public List<FilterBean> getTenderProvList() {
        if (!isOpen()) {
            openDB();
        }
        List<FilterBean> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "area_level = ?", new String[]{"0"});
        while (cursor.moveToNext()) {
            String provId = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
            String provName = cursor.getString(1);//获取第二列的值
            mList.add(new FilterBean("", provId, provName));
        }
        cursor.close();
        return mList;
    }

    public ArrayList<CityBean> getAllProvCityList() {
        if (!isOpen()) {
            openDB();
        }
        ArrayList<CityBean> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "area_level = ?", new String[]{"0"});
        while (cursor.moveToNext()) {
            String provId = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
            String provName = cursor.getString(1);//获取第二列的值
            CityBean bean = new CityBean();
            bean.setCode(provId);
            bean.setName(provName);
            bean.setChild(getCityListWithProvId(provId));
            mList.add(bean);
        }
        cursor.close();
        return mList;
    }

    public ArrayList<CityBean> getCityListWithProvId(String provId) {
        if (!isOpen()) {
            throw new NullPointerException("must be open SQLiteDatabase");
        }
        ArrayList<CityBean> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "parent_id = ?", new String[]{provId});
        while (cursor.moveToNext()) {
            String cityId = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
            String cityName = cursor.getString(1);//获取第二列的值
            CityBean cityBean = new CityBean();
            cityBean.setCode(cityId);
            cityBean.setName(cityName);
            mList.add(cityBean);
        }
        cursor.close();
        return mList;
    }

    public ArrayList<CityBean> getAreaListWithCityId(String cityId) {
        if (!isOpen()) {
            throw new NullPointerException("must be open SQLiteDatabase");
        }
        ArrayList<CityBean> mList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "parent_id = ?", new String[]{cityId});
        while (cursor.moveToNext()) {
            String areaId = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
            String areaName = cursor.getString(1);//获取第二列的值
            CityBean areaBean = new CityBean();
            areaBean.setCode(areaId);
            areaBean.setName(areaName);
            mList.add(areaBean);
        }
        cursor.close();
        return mList;
    }

    public Map<String, Object> getAllDataArr() {
        if (!isOpen()) {
            throw new NullPointerException("must be open SQLiteDatabase");
        }
        Cursor cursor = db.rawQuery("select area_id, area_name from t_sse_param_area where " +
                "area_level = ?", new String[]{"0"});
        int provCount = cursor.getCount();
        String[] provArr = new String[provCount];
        String[][] cityArr = new String[provCount][];
        String[][][] areaArr = new String[provCount][][];
        int i = 0;
        while (cursor.moveToNext()) {
            String provId = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
            String provName = cursor.getString(1);//获取第二列的值
            Cursor cityCursor = db.rawQuery("select area_id, area_name from t_sse_param_area " +
                    "where parent_id = ?", new String[]{provId});
            provArr[i] = provName;
            int cityCount = cityCursor.getCount();
            cityArr[i] = new String[cityCount];
            areaArr[i] = new String[cityCount][];
            int j = 0;
            while (cityCursor.moveToNext()) {
                String cityId = cityCursor.getString(0); //获取第一列的值,第一列的索引从0开始
                String cityName = cityCursor.getString(1);//获取第二列的值
                Cursor areaCursor = db.rawQuery("select area_id, area_name from t_sse_param_area " +
                        "where parent_id = ?", new String[]{cityId});
                cityArr[i][j] = cityName;
                int areaCount = areaCursor.getCount();
                areaArr[i][j] = new String[areaCount];
                int k = 0;
                while (areaCursor.moveToNext()) {
                    String areaId = areaCursor.getString(0);
                    String areaName = areaCursor.getString(1);
                    areaArr[i][j][k] = areaName;
                    k++;
                }
                areaCursor.close();
                j++;
            }
            cityCursor.close();
            i++;
        }
        cursor.close();
        Map<String, Object> map = new HashMap<>();
        map.put("prov", provArr);
        map.put("city", cityArr);
        map.put("area", areaArr);
        return map;
    }

    public String getIdWithName(String name, String parentId) {
        if (!isOpen()) {
            throw new NullPointerException("must be open SQLiteDatabase");
        }
        Cursor cursor = db.rawQuery("select area_id from t_sse_param_area where " +
                "parent_id = ? and area_name = ?", new String[]{parentId, name});
        String id = "";
        if (cursor.moveToNext()) {
            id = cursor.getString(0);
        }
        cursor.close();
        return id;
    }
}
