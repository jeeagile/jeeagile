package com.jeeagile.frame.autoconfigure;

import com.jeeagile.frame.properties.AgileServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@ComponentScan({"com.jeeagile"})
@EnableConfigurationProperties({AgileServiceProperties.class})
public class AgileServiceAutoConfiguration {

}
