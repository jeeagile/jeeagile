package com.jeeagile.shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
public class AgileShiroCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {
    //定义key存储便于获取所有key
    private Set<K> keySet = new ConcurrentSkipListSet<>();
    //定义spring的缓存
    private Cache cache;

    /**
     * 通过构造方法来对spring的CacheManager和Cache进行初始化
     *
     * @param cache
     */
    public AgileShiroCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * 实现shiro的Cache中的get方法
     */
    @Override
    public V get(K k) {
        //调用spring的Cache的get方法
        Cache.ValueWrapper valueWrapper = this.cache.get(k);
        if (valueWrapper == null) {
            this.keySet.remove(k);
            return null;
        }
        return (V) valueWrapper.get();
    }

    /**
     * 实现shiro的Cache中的put方法
     */
    @Override
    public V put(K k, V v) {
        try {
            this.cache.put(k, v);
            this.keySet.add(k);
        } catch (Exception ex) {
            log.warn("SHIRO缓存PUT方法出现异常:{}" , ex.getMessage());
        }
        return v;
    }

    /**
     * 实现shiro的Cache中的remove方法
     */
    @Override
    public V remove(K k) {
        V v = get(k);
        try {
            //调用spring的Cache的evict方法
            this.cache.evict(k);
        } catch (Exception ex) {
            log.warn("SHIRO缓存REMOVE方法出现异常:{}" , ex.getMessage());
        }
        this.keySet.remove(k);
        return v;
    }

    /**
     * 实现shiro的Cache中的clear方法
     */
    @Override
    public void clear() {
        this.cache.clear();
        this.keySet.clear();
    }

    /**
     * 实现shiro的Cache中的size方法
     */
    @Override
    public int size() {
        return keys().size();
    }

    /**
     * 实现shiro的Cache中的keys方法
     */
    @Override
    public Set<K> keys() {
        return this.keySet;
    }

    /**
     * 实现shiro的Cache中的values方法
     */
    @Override
    public Collection<V> values() {
        List<V> list = new ArrayList<>();
        Set<K> keys = keys();
        for (K k : keys) {
            V v = get(k);
            if (v != null) {
                list.add(v);
            } else {
                this.keySet.remove(k);
            }
        }
        return list;
    }
}
