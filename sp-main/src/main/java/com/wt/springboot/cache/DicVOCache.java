package com.wt.springboot.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据字典缓存对象 <br>
 *
 * @className: DicVOCache
 * @package: com.wt.springboot.cache
 * @author: wangtong
 * @date: 2021/11/30 4:48 pm
 **/

public class DicVOCache {
    private final List<DatadictVo> dicItemList;
    private final HashMap<String, DatadictVo> itemMap;

    public DicVOCache(List<DatadictVo> list){
        if(list == null){
            dicItemList = new ArrayList<>();
            itemMap = new HashMap<>();
            return;
        }
        dicItemList  = new ArrayList<>(list.size());
        itemMap = new HashMap<>(list.size());
        // 初始化操作
    }

    public List<DatadictVo> getDicItemList() {
        return dicItemList;
    }

    public HashMap<String, DatadictVo> getItemMap() {
        return itemMap;
    }
}
