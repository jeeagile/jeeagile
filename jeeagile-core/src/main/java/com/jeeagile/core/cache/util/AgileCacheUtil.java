package com.jeeagile.core.cache.util;

import com.jeeagile.core.util.spring.SpringContextUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Set;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 缓存工具类
 */
public class AgileCacheUtil {

    private AgileCacheUtil() {
    }

    private static CacheManager getCacheManager() {
        return ((CacheManager) SpringContextUtil.getBean("cacheManager"));
    }

    /**
     * 获取缓存对象
     *
     * @param cacheName
     * @return
     */
    private static Cache getCache(String cacheName) {
        return getCacheManager().getCache(cacheName);
    }

    /**
     * 获取缓存值对象
     *
     * @param cacheName
     * @param key
     * @return
     */
    private static Cache.ValueWrapper getValueWrapper(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (cache != null) {
            return cache.get(key);
        } else {
            return null;
        }
    }

    /**
     * 存储缓存
     *
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    /**
     * 获取缓存
     *
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key) {
        Cache.ValueWrapper valueWrapper = getValueWrapper(cacheName, key);
        if (valueWrapper != null) {
            return valueWrapper.get();
        } else {
            return null;
        }
    }


    /**
     * 获取缓存
     *
     * @param cacheName
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String cacheName, String key, Class<T> clazz) {
        Cache cache = getCache(cacheName);
        if (cache != null) {
            return cache.get(key, clazz);
        } else {
            return null;
        }
    }

    /**
     * 获取缓存所有缓存名称
     *
     * @return
     */
    public static Set<String> getCacheNames() {
        return (Set<String>) getCacheManager().getCacheNames();
    }

    /**
     * 根据key清除缓存
     *
     * @param cacheName
     * @param key
     */
    public static void evict(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    /**
     * 清空所有缓存
     *
     * @param cacheName
     */
    public static void clear(String cacheName) {
        Cache cache = getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
