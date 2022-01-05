package com.wt.springboot.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义cache 基类 <br>
 *
 * @className: CacheManager
 * @package: com.wt.springboot.cache
 * @author: wangtong
 * @date: 2021/11/30 4:18 pm
 **/

public class CacheManager<K,V> {
    private static final long MAX_VALID_PERIOD = 600000;
    private final ConcurrentHashMap<K,CacheItem> cache = new ConcurrentHashMap<>();
    private CacheItemRetriever<K,V> cacheItemRetriever;

    public final synchronized V getCacheObject(K key, K... extra){
        if(key == null){
            return null;
        }
        if(cacheItemRetriever == null){
            return null;
        }
        if(!cacheItemRetriever.isCacheValid(key)){
            cache.remove(key);
            return null;
        }
        CacheItem cacheItem = cache.get(key);
        if(cacheItem != null && cacheItem.invalidTime >= System.currentTimeMillis()){
            return  cacheItem.cacheObj;
        }

        V v = cacheItemRetriever.retrieveItem(key,extra);
        if(v != null){
            cacheItem = new CacheItem();
            cacheItem.cacheObj = v;
            cacheItem.invalidTime = System.currentTimeMillis() + MAX_VALID_PERIOD;
            cache.put(key, cacheItem);
            return v;
        }
        return null;
    }

    public void setCacheItemRetriever(CacheItemRetriever<K,V> retriever){
        cacheItemRetriever = retriever;
    }

    public void clear(){
        cache.clear();
    }

    private class CacheItem{
        V cacheObj;
        long invalidTime;
    }

    public interface CacheItemRetriever<K1,V1>{
        V1 retrieveItem(K1 key, K1... extra);

        boolean isCacheValid(K1 key);
    }
}
