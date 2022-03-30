package com.jeeagile.dubbo.autoconfigure;

import com.jeeagile.core.autoconfigure.AgileCoreAutoConfigure;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.protocol.properties.AgileProtocolProperties;
import com.jeeagile.dubbo.processor.AgileDubboReferencePostProcessor;
import com.jeeagile.dubbo.processor.AgileDubboServicePostProcessor;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.apache.dubbo.spring.boot.autoconfigure.DubboConfigurationProperties;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.apache.dubbo.spring.boot.util.DubboUtils.BASE_PACKAGES_PROPERTY_NAME;
import static org.apache.dubbo.spring.boot.util.DubboUtils.DUBBO_SCAN_PREFIX;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@EnableConfigurationProperties({AgileProtocolProperties.class, DubboConfigurationProperties.class})
@ConditionalOnProperty(prefix = AgileConstants.AGILE_PROTOCOL, name = "type", havingValue = "dubbo")
@AutoConfigureAfter({AgileCoreAutoConfigure.class, ReferenceAnnotationBeanPostProcessor.class})
@ImportResource("classpath:dubbo/dubbo-provider.xml")
public class AgileDubboAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = DUBBO_SCAN_PREFIX, name = BASE_PACKAGES_PROPERTY_NAME)
    public AgileDubboServicePostProcessor agileDubboServicePostProcessor(PropertyResolver propertyResolver) {
        Set<String> packagesToScan = propertyResolver.getProperty(DUBBO_SCAN_PREFIX + BASE_PACKAGES_PROPERTY_NAME, Set.class, emptySet());
        packagesToScan.add("com.jeeagile");
        return new AgileDubboServicePostProcessor(packagesToScan);
    }

    @ConditionalOnMissingBean
    @Bean(name = AgileDubboReferencePostProcessor.BEAN_NAME)
    public AgileDubboReferencePostProcessor agileDubboReferencePostProcessor(ConfigurableListableBeanFactory beanFactory) {
        AgileDubboReferencePostProcessor agileDubboReferencePostProcessor = new AgileDubboReferencePostProcessor();
        beanFactory.addBeanPostProcessor(agileDubboReferencePostProcessor);
        return agileDubboReferencePostProcessor;
    }

    @Bean
    @Primary
    public PropertyResolver primaryPropertyResolver(Environment environment) {
        return environment;
    }
}