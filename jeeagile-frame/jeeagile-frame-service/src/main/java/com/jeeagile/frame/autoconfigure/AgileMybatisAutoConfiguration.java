package com.jeeagile.frame.autoconfigure;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jeeagile.frame.annotation.AgileMapperScan;
import com.jeeagile.frame.handler.AgileMetaObjectHandler;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@InterceptorIgnore
public class AgileMybatisAutoConfiguration {

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer(MybatisPlusProperties mybatisPlusProperties) {
        mybatisPlusProperties.getGlobalConfig().setBanner(false);
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.jeeagile");
        mapperScannerConfigurer.setAnnotationClass(AgileMapperScan.class);
        return mapperScannerConfigurer;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new AgileMetaObjectHandler();
    }
}
