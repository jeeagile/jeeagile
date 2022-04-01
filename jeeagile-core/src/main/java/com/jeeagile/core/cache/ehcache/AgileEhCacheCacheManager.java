package com.jeeagile.core.cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.util.Assert;

/**
 * @author JeeAgile
 * @date 2022-04-01
 * @description 自定义EhCache 解决CacheName不存在时安装默认进行创建
 */
public class AgileEhCacheCacheManager extends EhCacheCacheManager {
    public AgileEhCacheCacheManager(CacheManager cacheManager) {
        super(cacheManager);
    }

    protected Cache getMissingCache(String name) {
        CacheManager cacheManager = this.getCacheManager();
        Assert.state(cacheManager != null, "No CacheManager set");
        Ehcache ehcache = cacheManager.getEhcache(name);
        if (ehcache == null) {
            cacheManager.addCache(name);
            ehcache = cacheManager.getEhcache(name);
        }
        return ehcache != null ? new EhCacheCache(ehcache) : null;
    }
}
