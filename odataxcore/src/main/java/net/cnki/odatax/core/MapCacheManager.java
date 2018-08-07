package net.cnki.odatax.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hudianwei
 * @date 2018/8/3 8:30
 */
/*HashMap与ConcurrentHashMap的区别
 * 从JDK1.2起，就有了HashMap，正如前一篇文章所说，HashMap不是线程安全的，因此多线程操作时需要格外小心。
 *在JDK1.5中给我们带来了concurrent包，从此Map也有安全的了。
 * */
public class MapCacheManager {

    private volatile long updateTime = 0L;//更新缓存时记录的时间
    private volatile boolean updateFlag = true;//正在更新时的阀门，为false时表示当前没有更新缓存，为true时表示当前正在更新缓存
    private volatile static MapCacheManager mapCacheObject;//缓存实例对象
    private static Map<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();//缓存容器

    private MapCacheManager() {
        this.LoadCache();//加载缓存
        updateTime = System.currentTimeMillis();//缓存更新时间
    }

    /*采用单例模式获取缓存对象实例*/
    public static MapCacheManager getInstance() {
        if (null == mapCacheObject) {
            synchronized (MapCacheManager.class) {
                if (null == mapCacheObject) {
                    mapCacheObject = new MapCacheManager();
                }
            }
        }
        return mapCacheObject;
    }

    /*加载缓存*/
    private void LoadCache() {
        this.updateFlag = true;// 正在更新
        /********** 数据处理，将数据放入cacheMap缓存中 **begin ******/
        cacheMap.put("key1", "value1");
        cacheMap.put("key2", "value2");
        cacheMap.put("key3", "value3");
        cacheMap.put("key4", "value4");
        cacheMap.put("key5", "value5");
        /********** 数据处理，将数据放入cacheMap缓存中 ***end *******/
        this.updateFlag = false;// 更新已完成
    }

    /*返回缓存对象*/
    public Map<String, Object> getMapCache() {
        long currentTime = System.currentTimeMillis();
        if (this.updateFlag) {
            return null;
        }
        if (this.IsTimeOut(currentTime)) {//
            synchronized (this) {
                this.ReLoadCache();
                this.updateTime = currentTime;
            }
        }
        return this.cacheMap;
    }


    private boolean IsTimeOut(long currentTime) {
        return ((currentTime - this.updateTime) > 100000);//超时
    }

    /*获取缓存项大小*/
    private int getCacheSize() {
        return cacheMap.size();
    }

    /*获取更新时间*/
    private long getUpdateTime() {
        return this.updateTime;
    }

    /*重新装载*/
    private void ReLoadCache() {
        this.cacheMap.clear();
        this.LoadCache();
    }
}
