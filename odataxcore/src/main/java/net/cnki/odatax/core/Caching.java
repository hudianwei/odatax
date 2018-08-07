package net.cnki.odatax.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hudianwei
 * @date 2018/8/2 15:15
 */
/*缓存*/
public class Caching {
    private static Map<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();

    public static Object get(String key) {
        return cacheMap.get(key);
    }

    public static void put(String key, Object value) {
        cacheMap.put(key, value);
    }

    public static void putAll(Map<? extends String, ? extends Object> map) {
        cacheMap.putAll(map);
    }
}
