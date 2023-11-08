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
@Component
@ConfigurationProperties(prefix = "agile.web")
public class AgileWebProperties {
    private AgilePoolProperties loggerPool;

    public AgilePoolProperties getLoggerPool() {
        if (this.loggerPool == null) {
            return new AgilePoolProperties();
        } else {
            return this.loggerPool;
        }
    }
}
