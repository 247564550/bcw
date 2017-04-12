package com.kswl.baimucai.activity.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.message
 * @desc
 * @date 2017-2017/3/24-13:14
 */

public class ShopIconCache {

    private static ShopIconCache instance;

    private ShopIconCache() {
    }

    public static ShopIconCache getInstance() {
        if (null == instance) {
            synchronized (ShopIconCache.class) {
                instance = new ShopIconCache();
            }
        }
        return instance;
    }

    private Map<String, String> map;

    public static final String VALUE_ERR = "valueErr";

    public static final String VALUE_LOADING = "valueLoading";

    public void setValue(String key, String value) {
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
    }

    public String getValue(String key) {
        if (null == map) {
            return VALUE_ERR;
        } else {
            return map.get(key);
        }
    }

    public void destory() {
        map = null;
        instance = null;
    }

}
