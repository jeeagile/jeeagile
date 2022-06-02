package com.jeeagile.process.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AgileProcessAutoConfig {

    /**
     * 临时解决activit用户安全问题
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeRequests()
                .anyRequest().permitAll().and().logout().permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable().build();
    }
}
