package com.jeeagile.core.autoconfigure;

import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.util.AgileStringUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description Redis启动参数配置类
 */
@Configuration
@ConditionalOnClass({RedisConnectionFactory.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
@ConditionalOnMissingBean({CacheManager.class})
@EnableConfigurationProperties({CacheProperties.class, RedisProperties.class})
public class AgileRedisAutoConfigure {

    @Bean
    public RedisCacheManager cacheManager(CacheProperties cacheProperties, ObjectProvider<RedisCacheConfiguration> redisCacheConfigurationObjectProvider, ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers, RedisConnectionFactory redisConnectionFactory, ResourceLoader resourceLoader) {
        RedisCacheConfiguration redisCacheConfiguration = this.determineConfiguration(cacheProperties, redisCacheConfigurationObjectProvider, resourceLoader.getClassLoader());
        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration);
        List<String> cacheNames = cacheProperties.getCacheNames();
        cacheNames.addAll(AgileCacheConstants.getInitCacheName());
        redisCacheManagerBuilder.initialCacheNames(new LinkedHashSet<>(cacheNames));
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put(AgileCacheConstants.AGILE_CACHE_CODE_NAME, redisCacheConfiguration.entryTtl(Duration.ofDays(30)));
        configMap.put(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, redisCacheConfiguration.entryTtl(Duration.ofMinutes(30)));
        configMap.put(AgileCacheConstants.AGILE_CACHE_AUTHENTICATION_NAME, redisCacheConfiguration.entryTtl(Duration.ofMinutes(30)));
        configMap.put(AgileCacheConstants.AGILE_CACHE_AUTHORIZATION_NAME, redisCacheConfiguration.entryTtl(Duration.ofMinutes(30)));
        configMap.put(AgileCacheConstants.AGILE_CACHE_KAPTCHA_NAME, redisCacheConfiguration.entryTtl(Duration.ofMinutes(1)));

        redisCacheManagerBuilder.withInitialCacheConfigurations(configMap);
        redisCacheManagerBuilderCustomizers.orderedStream().forEach(customizer ->
                customizer.customize(redisCacheManagerBuilder)
        );
        return redisCacheManagerBuilder.build();
    }

    private RedisCacheConfiguration determineConfiguration(CacheProperties cacheProperties, ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration, ClassLoader classLoader) {
        return redisCacheConfiguration.getIfAvailable(() ->
                this.createConfiguration(cacheProperties, classLoader)
        );
    }

    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, ClassLoader classLoader) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
        if (redisProperties.getTimeToLive() != null) {
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(redisProperties.getTimeToLive());
        } else {
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofDays(7));
        }

        if (AgileStringUtil.isNotEmpty(redisProperties.getKeyPrefix())) {
            redisCacheConfiguration = redisCacheConfiguration.prefixCacheNameWith(redisProperties.getKeyPrefix());
        } else {
            redisCacheConfiguration = redisCacheConfiguration.prefixCacheNameWith(AgileCacheConstants.AGILE_CACHE_PREFIX);
        }

        if (!redisProperties.isCacheNullValues()) {
            redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            redisCacheConfiguration = redisCacheConfiguration.disableKeyPrefix();
        }

        return redisCacheConfiguration;
    }
}

