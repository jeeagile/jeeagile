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
@ConfigurationProperties(prefix = "agile.service")
public class AgileServiceProperties {

}
