package com.jeeagile.redis.serializer;

import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 自定义 Redis KEY序列化 模板
 */
public class AgileStringRedisSerializer implements RedisSerializer<String> {
    @Autowired
    private CacheProperties cacheProperties;

    private final Charset charset;
    public static final StringRedisSerializer US_ASCII;
    public static final StringRedisSerializer ISO_8859_1;
    public static final StringRedisSerializer UTF_8;

    public AgileStringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public AgileStringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    /**
     * 反序列化key
     *
     * @param bytes
     * @return
     */
    public String deserialize(@Nullable byte[] bytes) {
        String keyPrefix = getKeyPrefix();
        String saveKey = new String(bytes, this.charset);
        if (StringUtil.isNotEmpty(saveKey) && saveKey.startsWith(keyPrefix)) {
            saveKey = saveKey.substring(keyPrefix.length());
        }
        return (saveKey.getBytes() == null ? null : saveKey);
    }

    /**
     * 序列化key
     *
     * @param string
     * @return
     */
    public byte[] serialize(@Nullable String string) {
        String keyPrefix = getKeyPrefix();
        String key = keyPrefix + string;
        return key.getBytes(this.charset);
    }

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }

    /**
     * 获取统一前缀
     *
     * @return
     */
    private String getKeyPrefix() {
        String keyPrefix;
        if (cacheProperties != null && StringUtil.isNotEmpty(cacheProperties.getRedis().getKeyPrefix())) {
            keyPrefix = cacheProperties.getRedis().getKeyPrefix();
        } else {
            keyPrefix = AgileCacheConstants.AGILE_CACHE_PREFIX;
        }
        if (keyPrefix.endsWith(":")) {
            keyPrefix = keyPrefix + "redis::";
        } else {
            keyPrefix = keyPrefix + ":redis::";
        }
        return keyPrefix;
    }

    static {
        US_ASCII = new StringRedisSerializer(StandardCharsets.US_ASCII);
        ISO_8859_1 = new StringRedisSerializer(StandardCharsets.ISO_8859_1);
        UTF_8 = new StringRedisSerializer(StandardCharsets.UTF_8);
    }
}
