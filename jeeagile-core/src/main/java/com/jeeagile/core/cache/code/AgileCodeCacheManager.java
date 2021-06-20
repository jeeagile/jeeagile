package com.jeeagile.core.cache.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 代码表缓存管理类
 */
public class AgileCodeCacheManager {

    private AgileCodeCacheManager() {
    }

    private static Logger logger = LoggerFactory.getLogger(AgileCodeCacheManager.class);

    private static Map<String, AgileCodeCacheConfig> codeCacheConfigMap = new HashMap<>();

    public static int countCodeCacheConfig() {
        return codeCacheConfigMap.size();
    }

    public static void addCodeCacheConfig(AgileCodeCacheConfig agileCodeCacheConfig) {
        codeCacheConfigMap.put(agileCodeCacheConfig.getCacheName(), agileCodeCacheConfig);
    }

    public static AgileCodeCacheConfig getCodeCacheConfig(String agileCodeCacheName) {
        return codeCacheConfigMap.get(agileCodeCacheName);
    }

    /**
     * 初始化代码表缓存
     *
     * @throws Exception
     */
    public static void initCodeCache() {
        for (AgileCodeCacheConfig agileCodeCacheConfig : codeCacheConfigMap.values()) {
            agileCodeCacheConfig.putAgileCodeCache();
        }
    }


    /**
     * 更新缓存
     *
     * @param codeCacheKey
     * @return
     */
    public static boolean updateCodeCache(String codeCacheKey) {
        try {
            AgileCodeCacheConfig agileCodeCacheConfig = getCodeCacheConfig(codeCacheKey);
            if (agileCodeCacheConfig != null) {
                agileCodeCacheConfig.updateAgileCodeCache();
            }
            return true;
        } catch (Exception ex) {
            logger.error("更新代码表缓存失败", ex);
        }
        return false;
    }

    /**
     * 获取指定代码表的List集合对象
     *
     * @param codeCacheKey
     * @return
     */
    public static List<Object> getCodeDataList(String codeCacheKey) {
        AgileCodeCacheConfig agileCodeCacheConfig = getCodeCacheConfig(codeCacheKey);
        if (agileCodeCacheConfig != null) {
            return agileCodeCacheConfig.getAgileCodeDataList();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取指定代码表、指定代码的元对象
     *
     * @param codeCacheKey 缓存名称
     * @param optionCode   代码值
     * @return optionObject
     */
    public static <T> T getBeanByCode(String codeCacheKey, String optionCode) {
        return (T) getObjectByCode(codeCacheKey, optionCode);
    }

    /**
     * 获取指定代码表、指定代码的元对象
     *
     * @param codeCacheKey
     * @param optionCode
     * @return optionObject
     */
    public static Object getObjectByCode(String codeCacheKey, String optionCode) {
        AgileCodeCacheConfig agileCodeCacheConfig = getCodeCacheConfig(codeCacheKey);
        if (agileCodeCacheConfig != null) {
            return agileCodeCacheConfig.getObjectByCode(optionCode);
        } else {
            return null;
        }
    }

    /**
     * 获取指定代码表、指定代码的元对象对应的代码名称
     *
     * @param codeCacheKey
     * @param optionCode
     * @return optionName
     */
    public static String getNameByCode(String codeCacheKey, String optionCode) {
        AgileCodeCacheConfig agileCodeCacheConfig = getCodeCacheConfig(codeCacheKey);
        if (agileCodeCacheConfig != null) {
            return agileCodeCacheConfig.getNameByCode(optionCode);
        } else {
            return null;
        }
    }

}
