package com.wt.springboot.cache;

import javax.annotation.PostConstruct;

/**
 * 于此定义各种缓存信息<br>
 *
 * @className: CacheHelper
 * @package: com.wt.springboot.cache
 * @author: wangtong
 * @date: 2021/11/30 4:43 pm
 **/

public class CacheHelper {

    private static final CacheManager<String , DicVOCache> DICT_CACHE = new CacheManager<>();

    private static CacheHelper cacheHelper;


    @PostConstruct
    public void init(){
        cacheHelper = this;
        DICT_CACHE.setCacheItemRetriever(new CacheManager.CacheItemRetriever<String, DicVOCache>() {
            @Override
            public DicVOCache retrieveItem(String key, String... extra) {

                // 初始化操作

                return null;
            }

            @Override
            public boolean isCacheValid(String key) {
                return true;
            }
        });
    }

    public static CacheHelper getCacheHelper() {
        return cacheHelper;
    }
}
