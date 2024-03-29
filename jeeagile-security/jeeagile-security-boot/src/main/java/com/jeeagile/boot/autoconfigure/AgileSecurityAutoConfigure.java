package com.jeeagile.boot.autoconfigure;


import com.jeeagile.boot.access.AgileAccessDeniedHandler;
import com.jeeagile.boot.access.AgileAuthenticationEntryPoint;
import com.jeeagile.boot.crypto.AgilePasswordEncoder;
import com.jeeagile.boot.filter.AgileUserTokenFilter;
import com.jeeagile.boot.provider.AgileAuthenticationProvider;
import com.jeeagile.boot.security.AgileBootSecurity;
import com.jeeagile.boot.strategy.AgileInvalidSessionStrategy;
import com.jeeagile.boot.userdetails.AgileUserDetailsService;
import com.jeeagile.core.security.properties.AgileSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.Resource;

@Configuration
@ComponentScan("com.jeeagile.boot")
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


    @Bean("AgileSecurity")
    @ConditionalOnMissingBean
    public AgileBootSecurity agileSpringSecurity() {
        return new AgileBootSecurity();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
        httpSecurity.sessionManagement().invalidSessionStrategy(new AgileInvalidSessionStrategy()).sessionFixation();
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
