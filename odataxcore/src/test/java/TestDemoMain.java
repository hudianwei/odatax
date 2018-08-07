import net.cnki.odatax.core.MapCacheManager;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hudianwei
 * @date 2018/8/3 11:01
 */
public class TestDemoMain {
    static Map<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();

    public static void main(String[] args) {
        MapCacheManager cache = MapCacheManager.getInstance();
        cacheMap = cache.getMapCache();
        Set<String> set = cacheMap.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key + "=" + cacheMap.get(key));
        }
    }
}
