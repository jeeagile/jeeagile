package com.jeeagile.shiro.autoconfigure;

import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.security.properties.AgileSecurityProperties;
import com.jeeagile.shiro.cache.AgileShiroCacheManager;
import com.jeeagile.shiro.filter.AgileAuthenticationFilter;
import com.jeeagile.shiro.listener.AgileSessionListener;
import com.jeeagile.shiro.realm.AgileAuthorizingRealm;
import com.jeeagile.shiro.security.AgileShiroSecurity;
import com.jeeagile.shiro.session.AgileSessionIdGenerator;
import com.jeeagile.shiro.session.AgileWebSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@EnableConfigurationProperties(AgileSecurityProperties.class)
public class AgileShiroAutoConfigure {

    @Resource
    private AgileSecurityProperties agileSecurityProperties;

    @Bean("AgileSecurity")
    @ConditionalOnMissingBean
    public AgileShiroSecurity agileShiroSecurity() {
        return new AgileShiroSecurity();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 配置会话ID生成器
     *
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new AgileSessionIdGenerator();
    }


    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterpriseCacheSessionDAO.setCacheManager(agileShiroCacheManager());
        enterpriseCacheSessionDAO.setActiveSessionsCacheName(AgileCacheConstants.AGILE_CACHE_SESSION_NAME);
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    /**
     * 配置自定义认证类
     *
     * @return
     */
    @Bean("agileAuthorizingRealm")
    public AgileAuthorizingRealm agileAuthorizingRealm() {
        AgileAuthorizingRealm agileAuthorizingRealm = new AgileAuthorizingRealm();
        agileAuthorizingRealm.setCachingEnabled(true);
        agileAuthorizingRealm.setAuthorizationCacheName(AgileCacheConstants.AGILE_CACHE_AUTHORIZATION_NAME);
        agileAuthorizingRealm.setAuthenticationCacheName(AgileCacheConstants.AGILE_CACHE_AUTHENTICATION_NAME);
        return agileAuthorizingRealm;
    }


    /**
     * 设置ShiroCacheManager
     */
    @Bean
    public AgileShiroCacheManager agileShiroCacheManager() {
        return new AgileShiroCacheManager();
    }

    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("agile.session.id");
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置会话管理器，设定会话超时及保存
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        AgileWebSessionManager sessionManager = new AgileWebSessionManager();
        Collection<SessionListener> sessionListenerList = new ArrayList<>();
        sessionListenerList.add(new AgileSessionListener());
        sessionManager.setSessionListeners(sessionListenerList);
        sessionManager.setGlobalSessionTimeout(1800000L);
        sessionManager.setCacheManager(agileShiroCacheManager());
        sessionManager.setSessionDAO(sessionDAO());

        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(1800000L);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(simpleCookie());
        // 去掉 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 权限管理
     */
    @Bean
    public SecurityManager agileShiroSecurityManager(AgileAuthorizingRealm agileAuthorizingRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(agileAuthorizingRealm);
        securityManager.setCacheManager(agileShiroCacheManager());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * Filter工厂，设置对应的过滤条件和跳转条件
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        filterMap.put("user", new AgileAuthenticationFilter());

        Map<String, String> filterChainMap = new HashMap<>();
        filterChainMap.put("/static/**", "anon");
        filterChainMap.put("/index.html", "anon");
        filterChainMap.put("/system/kaptcha/**", "anon");
        filterChainMap.put("/system/auth/login", "anon");
        filterChainMap.put("/system/tenant/info", "anon");
        for (String strUrl : agileSecurityProperties.getAnonUrl()) {
            filterChainMap.put(strUrl, "anon");
        }

        filterChainMap.put("/**", "user");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
