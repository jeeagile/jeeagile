package com.jeeagile.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileShiroCacheManager extends AbstractCacheManager {
    @Autowired()
    private org.springframework.cache.CacheManager cacheManager;

    @Override
    protected Cache createCache(String cacheName) throws CacheException {
        return new AgileShiroCache<>(cacheManager.getCache(cacheName));
    }
}
