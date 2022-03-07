package com.jeeagile.springsecurity.autoconfigure;


import com.jeeagile.core.security.properties.AgileSecurityProperties;
import com.jeeagile.springsecurity.access.AgileAccessDeniedHandler;
import com.jeeagile.springsecurity.access.AgileAuthenticationEntryPoint;
import com.jeeagile.springsecurity.crypto.AgilePasswordEncoder;
import com.jeeagile.springsecurity.filter.AgileUserTokenFilter;
import com.jeeagile.springsecurity.security.AgileSpringSecurity;
import com.jeeagile.springsecurity.userdetails.AgileUserDetailsServiceImpl;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.Resource;

@Configuration
@ComponentScan("com.jeeagile.springsecurity")
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
//    @Autowired
//    private SwztAuthenticationEntryPoint swztAuthenticationEntryPoint;
//    @Autowired
//    private SwztAuthenticationTokenFilter swztAuthenticationTokenFilter;
//    @Autowired
//    private SwztLogoutSuccessHandler swztLogoutSuccessHandler;
//    @Autowired
//    private SwztAccessDeniedHandler swztAccessDeniedHandler;




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
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.cors().disable();
        httpSecurity.csrf().disable();

        // 过滤请求 对于登录login 允许匿名访问
        httpSecurity.authorizeRequests().antMatchers(
                "/system/user/login" ,
                "/system/kaptcha/image" ,
                "/system/kaptcha/valid")
                .permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET,
                "/static/**" ,
                "/index.html")
                .permitAll();

        for (String strUrl : agileSecurityProperties.getAnonUrl()) {
            httpSecurity.authorizeRequests().antMatchers(strUrl).permitAll();
        }

        httpSecurity.authorizeRequests().anyRequest().authenticated().and().headers().frameOptions().disable();
//        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(swztLogoutSuccessHandler);
        httpSecurity.addFilterBefore(agileUserTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling().accessDeniedHandler(agileAccessDeniedHandler);
        httpSecurity.exceptionHandling().authenticationEntryPoint(agileAuthenticationEntryPoint);
    }

    @Bean
    public AgilePasswordEncoder agilePasswordEncoder() {
        return new AgilePasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AgileUserDetailsServiceImpl();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(agilePasswordEncoder());
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
