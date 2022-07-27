package com.jeeagile.frame.autoconfigure;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.properties.AgileProperties;
import com.jeeagile.core.security.properties.AgileSecurityProperties;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.file.AgileFileUtil;
import com.jeeagile.frame.support.interceptor.AgileSecurityInterceptor;
import com.jeeagile.frame.support.resolver.SingleRequestBodyResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@EnableAsync
@Configuration
@ComponentScan({"com.jeeagile"})
@EnableConfigurationProperties({AgileSecurityProperties.class, AgileProperties.class})
public class AgileWebAutoConfigure implements WebMvcConfigurer {
    @Resource
    private AgileSecurityProperties agileSecurityProperties;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SingleRequestBodyResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 本地文件上传路径
        registry.addResourceHandler(AgileConstants.AGILE_RESOURCE_PREFIX + "/**").addResourceLocations("file:" + AgileUtil.getUploadPath() + AgileFileUtil.getFileSeparator());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义的拦截器 并拦截所有请求
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new AgileSecurityInterceptor());
        if (agileSecurityProperties.getAnonUrl() != null && !agileSecurityProperties.getAnonUrl().isEmpty()) {
            interceptorRegistration.excludePathPatterns(agileSecurityProperties.getAnonUrl());
        }
        interceptorRegistration.excludePathPatterns("/system/auth/login", "/system/kaptcha/**");
    }

    @Bean
    @ConditionalOnMissingBean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Bean
    @ConditionalOnMissingBean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat
        );
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return fastConverter;
    }

    @Bean("AgileAsyncTask")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(80);
        threadPoolTaskExecutor.setQueueCapacity(30);
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        threadPoolTaskExecutor.setThreadNamePrefix("AgileAsyncTask");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}