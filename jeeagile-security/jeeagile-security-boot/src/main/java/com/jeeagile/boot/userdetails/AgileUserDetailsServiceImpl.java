package com.jeeagile.boot.userdetails;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.util.AgileAgentUtil;
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
public class AgileUserDetailsServiceImpl implements UserDetailsService {
    @Lazy
    @AgileReference
    private IAgileUserDetailsService agileUserDetailsService;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        try {
            if (agileUserDetailsService == null) {
                throw new AgileFrameException(AgileResultCode.FAIL_SERVER_EXCEPTION, "请设置用户验证接口实现类！");
            }
            AgileBaseUser userData = agileUserDetailsService.getUserDataByLoginName(loginName);
            if (userData != null && AgileStringUtil.isNotEmpty(userData.getUserId())) {
                userData.setUserToken(AgileStringUtil.getUuid());
                userData.setUserPerm(agileUserDetailsService.getUserPerm(userData));
                userData.setUserRole(agileUserDetailsService.getUserRole(userData));
                HttpServletRequest httpServletRequest = AgileServletUtil.getHttpServletRequest();
                if (httpServletRequest != null) {
                    UserAgent userAgent = AgileAgentUtil.getUserAgent(httpServletRequest);
                    userData.setLoginIp(AgileAgentUtil.getUserClientIp(httpServletRequest));
                    userData.setOsName(userAgent.getOperatingSystem().getName());
                    userData.setDeviceName(userAgent.getOperatingSystem().getDeviceType().getName());
                    userData.setBrowserName(userAgent.getBrowser().getName());
                }
                List<SimpleGrantedAuthority> authorities = userData.getUserRole().stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                AgileUserDetails agileUserDetails = new AgileUserDetails();
                agileUserDetails.setUserData(userData);
                agileUserDetails.setAuthorities(authorities);
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