package com.jeeagile.core.cache.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 缓存配置常量
 */
public class AgileCacheConstants {

    private AgileCacheConstants() {
    }

    /**
     * 缓存统一前缀
     */
    public static final String AGILE_CACHE_PREFIX = "agile:cache:";

    /**
     * 默认缓存key值
     */
    public static final String AGILE_CACHE_DEFAULT_NAME = "default";

    /**
     * 代码缓存key
     */
    public static final String AGILE_CACHE_CODE_NAME = "code";

    /**
     * session key
     */
    public static final String AGILE_CACHE_SESSION_NAME = "session";

    /**
     * 认证缓存key
     */
    public static final String AGILE_CACHE_AUTHENTICATION_NAME="authentication";

    /**
     * 授权缓存key
     */
    public static final String AGILE_CACHE_AUTHORIZATION_NAME="authorization";

    /**
     * 验证码
     */
    public static final String AGILE_CACHE_KAPTCHA_NAME = "kaptcha";

    public static final List<String> getInitCacheName() {
        List<String> cacheNames = new ArrayList<>();
        cacheNames.add(AGILE_CACHE_DEFAULT_NAME);
        cacheNames.add(AGILE_CACHE_CODE_NAME);
        cacheNames.add(AGILE_CACHE_SESSION_NAME);
        cacheNames.add(AGILE_CACHE_AUTHENTICATION_NAME);
        cacheNames.add(AGILE_CACHE_AUTHORIZATION_NAME);
        cacheNames.add(AGILE_CACHE_KAPTCHA_NAME);
        return cacheNames;
    }
}
