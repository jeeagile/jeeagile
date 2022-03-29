package com.jeeagile.springsecurity.method;

import com.jeeagile.springsecurity.access.AgilePermissionEvaluator;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled  = true, securedEnabled = true)
public class AgileMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        AgileMethodSecurityExpressionHandler expressionHandler = new AgileMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new AgilePermissionEvaluator());
        return expressionHandler;
    }
}
