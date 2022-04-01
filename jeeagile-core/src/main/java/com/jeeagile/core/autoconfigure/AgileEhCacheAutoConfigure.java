package com.jeeagile.core.autoconfigure;

import com.jeeagile.core.cache.ehcache.AgileEhCacheCacheManager;
import net.sf.ehcache.Cache;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description EhCache启动配置类
 */
@Configuration
@ConditionalOnClass({Cache.class, EhCacheCacheManager.class})
@ConditionalOnMissingBean({CacheManager.class})
@EnableConfigurationProperties(CacheProperties.class)
public class AgileEhCacheAutoConfigure {
    AgileEhCacheAutoConfigure() {
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
        return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    @ConditionalOnMissingBean
    AgileEhCacheCacheManager cacheManager(CacheManagerCustomizers customizers, net.sf.ehcache.CacheManager ehCacheCacheManager) {
        return customizers.customize(new AgileEhCacheCacheManager(ehCacheCacheManager));
    }

    @Bean
    @ConditionalOnMissingBean
    net.sf.ehcache.CacheManager ehCacheCacheManager(CacheProperties cacheProperties) {
        if (cacheProperties.getEhcache().getConfig() == null) {
            cacheProperties.getEhcache().setConfig(new ClassPathResource("config/ehcache.xml"));
        }
        Resource location = cacheProperties.resolveConfigLocation(cacheProperties.getEhcache().getConfig());
        return location != null ? EhCacheManagerUtils.buildCacheManager(location) : EhCacheManagerUtils.buildCacheManager();
    }
}
