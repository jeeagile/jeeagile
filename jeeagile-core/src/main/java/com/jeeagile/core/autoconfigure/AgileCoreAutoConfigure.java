package com.jeeagile.core.autoconfigure;

import com.jeeagile.core.cache.code.AgileCodeCacheListener;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.protocol.processor.LocalProviderBeanPostProcessor;
import com.jeeagile.core.protocol.processor.LocalServiceBeanPostProcessor;
import com.jeeagile.core.protocol.properties.AgileProtocolProperties;
import com.jeeagile.core.security.properties.AgileSecurityProperties;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description JeeAgile核心启动配置
 */
@Configurable
@EnableCaching
@ComponentScan("com.jeeagile.core")
@EnableConfigurationProperties({AgileSecurityProperties.class, AgileProtocolProperties.class})
public class AgileCoreAutoConfigure {

    @Bean
    @ConditionalOnProperty(prefix = AgileConstants.AGILE_PROTOCOL, name = AgileConstants.AGILE_PROTOCOL_TYPE, havingValue = "local", matchIfMissing = true)
    public LocalProviderBeanPostProcessor localProviderBeanPostProcessor() {
        return new LocalProviderBeanPostProcessor();
    }

    @Bean
    @ConditionalOnProperty(prefix = AgileConstants.AGILE_PROTOCOL, name = AgileConstants.AGILE_PROTOCOL_TYPE, havingValue = "local", matchIfMissing = true)
    public LocalServiceBeanPostProcessor localServiceBeanPostProcessor() {
        return new LocalServiceBeanPostProcessor();
    }

    @Bean
    public AgileCodeCacheListener agileCodeCacheListener() {
        return new AgileCodeCacheListener();
    }
}
