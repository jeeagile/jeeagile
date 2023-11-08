package com.jeeagile.spring.autoconfigure;


import com.jeeagile.core.security.annotation.AgileRequiresGuest;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.spring.access.AgileAccessDeniedHandler;
import com.jeeagile.spring.access.AgileAuthenticationEntryPoint;
import com.jeeagile.spring.crypto.AgilePasswordEncoder;
import com.jeeagile.spring.filter.AgileUserTokenFilter;
import com.jeeagile.spring.provider.AgileAuthenticationProvider;
import com.jeeagile.spring.security.AgileSpringSecurity;
import com.jeeagile.spring.userdetails.AgileUserDetailsService;
import com.jeeagile.core.security.properties.AgileSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.servlet.ControllerEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
@ComponentScan("com.jeeagile.spring")
@EnableConfigurationProperties(AgileSecurityProperties.class)
public class AgileSecurityAutoConfigure extends WebSecurityConfigurerAdapter {
    @Resource
    private AgileSecurityProperties agileSecurityProperties;
    @Autowired
    private AgileUserTokenFilter agileUserTokenFilter;
    @Autowired
    private AgileAccessDeniedHandler agileAccessDeniedHandler;
    @Autowired
    private AgileAuthenticationEntryPoint agileAuthenticationEntryPoint;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean("AgileSecurity")
    @ConditionalOnMissingBean
    public AgileSpringSecurity agileSpringSecurity() {
        return new AgileSpringSecurity();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        WebSecurity and = webSecurity.ignoring().and();
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethodMap.forEach(((requestMappingInfo, handlerMethod) -> {
            AgileRequiresGuest agileRequiresGuest = handlerMethod.getBeanType().getAnnotation(AgileRequiresGuest.class);
            RequestMapping requestMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
            if (agileRequiresGuest != null && requestMapping != null) {
                String path = requestMapping.path()[0];
                if (path.lastIndexOf("/") != path.length() - 1) {
                    and.ignoring().antMatchers(path + "/**");
                } else {
                    and.ignoring().antMatchers(path + "**");
                }
            }
            agileRequiresGuest = handlerMethod.getMethodAnnotation(AgileRequiresGuest.class);
            if (agileRequiresGuest != null) {
                requestMappingInfo.getMethodsCondition().getMethods().forEach(requestMethod -> {
                    switch (requestMethod) {
                        case POST:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.POST, pattern);
                            });
                            break;
                        case GET:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.GET, pattern);
                            });
                            break;
                        case DELETE:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.DELETE, pattern);
                            });
                            break;
                        case PUT:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(HttpMethod.PUT, pattern);
                            });
                            break;
                        default:
                            requestMappingInfo.getPatternsCondition().getPatterns().forEach(pattern -> {
                                and.ignoring().antMatchers(pattern);
                            });
                    }
                });
            }
        }));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.cors().disable();
        httpSecurity.csrf().disable();

        // 过滤请求 对于登录login 允许匿名访问
        httpSecurity.authorizeRequests().antMatchers(
                "/system/auth/login",
                "/system/tenant/info",
                "/system/kaptcha/image",
                "/system/kaptcha/valid")
                .permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET,
                "/static/**",
                "/index.html")
                .permitAll();

        for (String strUrl : agileSecurityProperties.getAnonUrl()) {
            httpSecurity.authorizeRequests().antMatchers(strUrl).permitAll();
        }

        httpSecurity.authorizeRequests().anyRequest().authenticated().and().headers().frameOptions().disable();
        httpSecurity.addFilterBefore(agileUserTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling().accessDeniedHandler(agileAccessDeniedHandler);
        httpSecurity.exceptionHandling().authenticationEntryPoint(agileAuthenticationEntryPoint);
    }

    @Bean
    public AgilePasswordEncoder agilePasswordEncoder() {
        return new AgilePasswordEncoder();
    }

    @Bean
    public AgileUserDetailsService agileUserDetailsService() {
        return new AgileUserDetailsService();
    }

    @Bean
    public AgileAuthenticationProvider agileAuthenticationProvider() {
        return new AgileAuthenticationProvider();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(agileAuthenticationProvider());
//        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(agilePasswordEncoder());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
