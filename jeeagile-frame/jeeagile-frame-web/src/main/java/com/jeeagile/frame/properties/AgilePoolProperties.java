package com.jeeagile.frame.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
public class AgilePoolProperties {
    /**
     * 核心线程数
     */
    private int corePoolSize = 30;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 50;
    /**
     *
     */
    private int keepAliveSeconds = 60;
    /**
     *
     */
    private int queueCapacity = 1000;
}
