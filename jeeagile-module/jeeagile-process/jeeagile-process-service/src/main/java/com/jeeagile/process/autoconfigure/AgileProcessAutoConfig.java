package com.jeeagile.process.autoconfigure;

import com.jeeagile.process.support.activiti.listener.AgileActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AgileProcessAutoConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private AgileActivitiEventListener agileActivitiEventListener;
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

    @Override
    public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        List<ActivitiEventListener> activitiEventListenerList=new ArrayList<>();
        activitiEventListenerList.add(agileActivitiEventListener);
        springProcessEngineConfiguration.setEventListeners(activitiEventListenerList);
    }
}
