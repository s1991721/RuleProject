package com.ljf.ruleproject.base.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

/**
 * Created by mr.lin on 2020/7/15
 */
@Component
public class EhcacheServiceComponent {

    @Autowired
    private EhCacheCacheManager cacheCacheManager;

    //获取缓存
    public Object get(String cacheName,String key){
        Cache cache = cacheCacheManager.getCacheManager().getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    //放入缓存
    public void put(String cacheName,String key,Object value){
        Cache cache = cacheCacheManager.getCacheManager().getCache(cacheName);
        Element element = new Element(key,value);
        cache.put(element);
    }

    //清空缓存
    public void evict(String cacheName,String key){
        Cache cache = cacheCacheManager.getCacheManager().getCache(cacheName);
        cache.remove(key);
    }

}
