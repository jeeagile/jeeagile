package com.jeeagile.process.autoconfigure;

import com.jeeagile.process.support.activiti.listener.AgileActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AgileProcessAutoConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private AgileActivitiEventListener agileActivitiEventListener;
    /**
     * 临时解决用户安全问题
     * TODO 如果使用shiro需放开此处代码，如果使用SpringSecurity此处代码需屏蔽
     */
    @Bean
    public SecurityFilterChain agileSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeRequests()
                .anyRequest().permitAll().and().logout().permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable().build();
    }

    @Override
    public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        List<ActivitiEventListener> activitiEventListenerList=new ArrayList<>();
        activitiEventListenerList.add(agileActivitiEventListener);
        springProcessEngineConfiguration.setEventListeners(activitiEventListenerList);
    }
}
