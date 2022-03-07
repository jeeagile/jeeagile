package com.jeeagile.core.cache.code;

import com.jeeagile.core.cache.code.annotation.AgileCodeCache;
import com.jeeagile.core.util.AgileArrayUtil;
import com.jeeagile.core.util.AgileStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 代码缓存Listener
 */
public class AgileCodeCacheListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器为Spring容器
        if (event.getApplicationContext().getParent() == null) {
            scanCodeCacheBean(event.getApplicationContext().getBeansWithAnnotation(Component.class));
        }

        //初始化代码表缓存
        if (AgileCodeCacheManager.countCodeCacheConfig() > 0) {
            initCodeCache();
        }
    }

    /**
     * 扫描对应的bean
     *
     * @param beans
     */
    private void scanCodeCacheBean(Map<String, Object> beans) {
        for (Object bean : beans.values()) {
            String className = bean.getClass().getName();
            if (className.startsWith("org.springframework")
                    || className.startsWith("com.baomidou")
                    || className.startsWith("org.mybatis")
                    || className.startsWith("com.alibaba")) {
                continue;
            }
            scanCodeMethod(bean);
        }
    }

    /**
     * 扫描对应方法
     *
     * @param bean
     */
    private void scanCodeMethod(Object bean) {
        Method[] methodList = bean.getClass().getMethods();
        for (Method method : methodList) {
            AgileCodeCache agileCodeCache = AnnotationUtils.findAnnotation(method, AgileCodeCache.class);
            if (agileCodeCache != null && checkCodeMethod(bean, method, agileCodeCache)) {
                loadCodeMethod(bean, method, agileCodeCache);
            }
        }
    }

    /**
     * 校验代码缓存方法是否符合要求
     *
     * @param bean
     * @param method
     * @return
     */
    private boolean checkCodeMethod(Object bean, Method method, AgileCodeCache agileCodeCache) {
        if (method.getParameterCount() > 0) {
            logger.error("代码表缓存方法不能有参数！！！");
            return false;
        }

        if (AgileStringUtil.isEmpty(agileCodeCache.value())) {
            logger.error("代码缓存方法没有设置缓存名称：{}.{}", bean.getClass().getName(), method.getName());
            return false;
        }

        if (AgileCodeCacheManager.getCodeCacheConfig(agileCodeCache.value()) != null) {
            logger.error("代码缓存{}已存在", agileCodeCache.value());
            return false;
        }

        ResolvableType rtnType = ResolvableType.forMethodReturnType(method);
        if (rtnType.getGenerics().length < 1) {
            logger.error("代码缓存《{}》没有设置返回泛型！", agileCodeCache.value());
            return false;
        }
        Class<?> rtnClass = rtnType.getRawClass();
        if (rtnClass == null || !rtnClass.equals(List.class)) {
            logger.error("代码表缓存方法返回类型必须为List！");
            return false;
        }

        return true;
    }

    /**
     * 加载代码表方法
     *
     * @param bean
     * @param method
     */
    private void loadCodeMethod(Object bean, Method method, AgileCodeCache agileCodeCache) {
        AgileCodeCacheConfig agileCodeCacheConfig = new AgileCodeCacheConfig();
        agileCodeCacheConfig.setBean(bean);
        agileCodeCacheConfig.setMethod(method);
        ResolvableType codeObjectType = ResolvableType.forMethodReturnType(method).getGeneric(0);
        if (codeObjectType.resolve() != null) {
            if (codeObjectType.resolve() == Map.class) {
                loadCodeOptionField(agileCodeCacheConfig, agileCodeCache);
            }
            if (codeObjectType.resolve() != AgileBaseCodeCache.class) {
                loadCodeBeanOptionField(codeObjectType, agileCodeCacheConfig, agileCodeCache);
            }
        } else {
            loadCodeOptionField(agileCodeCacheConfig, agileCodeCache);
        }
        agileCodeCacheConfig.setCacheName(agileCodeCache.value());
        agileCodeCacheConfig.setCacheDesc(agileCodeCache.value());
        AgileCodeCacheManager.addCodeCacheConfig(agileCodeCacheConfig);
    }

    /**
     * 加载Map类型代码配置属性值
     *
     * @param agileCodeCacheConfig
     * @param agileCodeCache
     */
    private void loadCodeOptionField(AgileCodeCacheConfig agileCodeCacheConfig, AgileCodeCache agileCodeCache) {
        //加载代码值字段
        if (AgileArrayUtil.isNotEmpty(agileCodeCache.optionCodeField())) {
            for (String fieldName : agileCodeCache.optionCodeField()) {
                agileCodeCacheConfig.getOptionCodeField().add(fieldName);
            }
        } else {
            agileCodeCacheConfig.getOptionCodeField().add("optionCode");
        }
        //加载代码名称字段
        if (AgileArrayUtil.isNotEmpty(agileCodeCache.optionNameField())) {
            for (String fieldName : agileCodeCache.optionNameField()) {
                agileCodeCacheConfig.getOptionNameField().add(fieldName);
            }
        } else {
            agileCodeCacheConfig.getOptionNameField().add("optionName");
        }
    }

    /**
     * 根据注解加载代码配置属性字段
     *
     * @param codeObject
     * @param agileCodeCacheConfig
     * @param agileCodeCache
     */
    private void loadCodeBeanOptionField(ResolvableType codeObject, AgileCodeCacheConfig agileCodeCacheConfig, AgileCodeCache agileCodeCache) {
        for (Field field : codeObject.resolve().getDeclaredFields()) {
            String fieldName = field.getName();
            if (AgileArrayUtil.contains(agileCodeCache.optionCodeField(), fieldName)) {
                agileCodeCacheConfig.getObjectOptionCodeField().add(field);
            }
            if (AgileArrayUtil.contains(agileCodeCache.optionNameField(), fieldName)) {
                agileCodeCacheConfig.getObjectOptionNameField().add(field);
            }
            Annotation codeAnnotation = field.getAnnotation(AgileCodeCache.class);
            if (codeAnnotation != null) {
                agileCodeCacheConfig.getObjectOptionCodeField().add(field);
            }
            Annotation nameAnnotation = field.getAnnotation(AgileCodeCache.class);
            if (nameAnnotation != null) {
                agileCodeCacheConfig.getObjectOptionNameField().add(field);
            }
        }
    }

    /**
     * 初始化代码表缓存
     */
    private void initCodeCache() {
        try {
            logger.info("==========================加载代码表缓存开始==========================");
            AgileCodeCacheManager.initCodeCache();
            logger.info("==========================加载代码表缓存结束==========================");
        } catch (Exception ex) {
            logger.error("代码表初始化异常，将退出程序", ex);
            configurableApplicationContext.close();
        }
    }
}
