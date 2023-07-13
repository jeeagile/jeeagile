package com.jeeagile.spring.userdetails;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.util.AgileAgentUtil;
import com.jeeagile.core.util.AgileNetUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileServletUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
public class AgileUserDetailsService implements UserDetailsService {
    @Lazy
    @AgileReference
    private IAgileUserDetailsService agileUserDetailsService;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        try {
            if (agileUserDetailsService == null) {
                throw new AgileFrameException(AgileResultCode.FAIL_SERVER_EXCEPTION, "请设置用户验证接口实现类！");
            }
            return this.loadUserDetails(agileUserDetailsService.getUserData(loginName));
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException(AgileResultCode.FAIL_AUTH_EXCEPTION, ex);
        }
    }


    public UserDetails loadUserByUsername(String loginName, String tenantId, String tenantSign) throws UsernameNotFoundException {
        try {
            if (agileUserDetailsService == null) {
                throw new AgileFrameException(AgileResultCode.FAIL_SERVER_EXCEPTION, "请设置用户验证接口实现类！");
            }
            return this.loadUserDetails(agileUserDetailsService.getUserData(loginName, tenantId, tenantSign));
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException(AgileResultCode.FAIL_AUTH_EXCEPTION, ex);
        }
    }

    private UserDetails loadUserDetails(AgileBaseUser userData) {
        try {
            if (userData != null && AgileStringUtil.isNotEmpty(userData.getUserId())) {
                userData.setUserToken(AgileStringUtil.getUuid());
                userData.setUserPermList(agileUserDetailsService.getUserPermList(userData));
                userData.setUserRoleList(agileUserDetailsService.getUserRoleList(userData));
                HttpServletRequest httpServletRequest = AgileServletUtil.getHttpServletRequest();
                if (httpServletRequest != null) {
                    UserAgent userAgent = AgileAgentUtil.getUserAgent(httpServletRequest);
                    userData.setLoginIp(AgileAgentUtil.getUserClientIp(httpServletRequest));
                    userData.setLoginAddress(AgileNetUtil.getAddressByIp(userData.getLoginIp()));
                    userData.setOsName(userAgent.getOperatingSystem().getName());
                    userData.setDeviceName(userAgent.getOperatingSystem().getDeviceType().getName());
                    userData.setBrowserName(userAgent.getBrowser().getName());
                }
                List<SimpleGrantedAuthority> authorities = userData.getUserRoleList().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                AgileUserDetails agileUserDetails = new AgileUserDetails();
                agileUserDetails.setUserData(userData);
                agileUserDetails.setAuthorities(authorities);
                AgileSecurityContext.putUserData(userData);
                return agileUserDetails;
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_NAME);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException(AgileResultCode.FAIL_AUTH_EXCEPTION, ex);
        }
    }
}