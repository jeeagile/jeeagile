package com.jeeagile.frame.autoconfigure;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jeeagile.frame.annotation.AgileDataScope;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.handler.AgileMetaObjectHandler;
import com.jeeagile.frame.plugins.datascope.AgileDataScopeInterceptor;
import com.jeeagile.frame.plugins.injector.AgileCustomSqlInjector;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.Properties;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@EnableConfigurationProperties({MybatisPlusProperties.class})
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@InterceptorIgnore
public class AgileMybatisAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer(MybatisPlusProperties mybatisPlusProperties) {
        mybatisPlusProperties.getGlobalConfig().setBanner(false);
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.jeeagile");
        mapperScannerConfigurer.setAnnotationClass(AgileMapper.class);
        mybatisPlusProperties.getGlobalConfig().getDbConfig().setWhereStrategy(FieldStrategy.NOT_EMPTY);
        return mapperScannerConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        mybatisPlusInterceptor.addInnerInterceptor(new AgileDataScopeInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new AgileMetaObjectHandler();
    }

    @Bean
    public AgileCustomSqlInjector agileSqlInjector() {
        return new AgileCustomSqlInjector();
    }

    @Bean
    @ConditionalOnMissingBean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.put("Oracle", "oracle");
        properties.put("MySQL", "mysql");
        properties.put("SQLServer", "sqlServer");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(AgileDataScope.class);
            System.out.println("");
        }
    }
}
