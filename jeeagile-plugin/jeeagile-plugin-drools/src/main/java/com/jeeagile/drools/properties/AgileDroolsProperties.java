package com.jeeagile.drools.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述  自定义 drools 配置
 */
@Data
@Component
@ConfigurationProperties(AgileDroolsProperties.PREFIX)
public class AgileDroolsProperties implements Serializable {
    public static final String PREFIX = "agile.drools";
    /**
     * 默认kieBase
     */
    private String defaultKieBaseName = "";
    /**
     * KieBase
     */
    private Map<String, AgileKieBaseProperties> kieBase = new LinkedHashMap<>();
}
