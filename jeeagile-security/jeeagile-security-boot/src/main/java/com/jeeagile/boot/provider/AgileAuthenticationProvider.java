package com.jeeagile.boot.provider;

import com.jeeagile.boot.authc.AgileUsernamePasswordToken;
import com.jeeagile.boot.crypto.AgilePasswordEncoder;
import com.jeeagile.boot.userdetails.AgileUserDetails;
import com.jeeagile.boot.userdetails.AgileUserDetailsService;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileServletUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpSession;

public class AgileAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AgileUserDetailsService agileUserDetailsService;
    @Autowired
    private AgilePasswordEncoder agilePasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        try {
            if (agileUserDetailsService == null) {
                throw new AgileFrameException(AgileResultCode.FAIL_SERVER_EXCEPTION, "请设置用户验证接口实现类！");
            }
            AgileUserDetails agileUserDetails;
            if (AgileTenantUtil.isTenantEnable()) {
                AgileUsernamePasswordToken agileUsernamePasswordToken = (AgileUsernamePasswordToken) authentication;
                agileUserDetails = (AgileUserDetails) agileUserDetailsService.loadUserByUsername(username, agileUsernamePasswordToken.getTenantId(), agileUsernamePasswordToken.getTenantSign());
            } else {
                agileUserDetails = (AgileUserDetails) agileUserDetailsService.loadUserByUsername(username);
            }
            if (agileUserDetails == null || AgileStringUtil.isEmpty(agileUserDetails.getUsername())) {
                throw new AgileAuthException("获取用户信息失败!");
            }
            if (!agilePasswordEncoder.matches(password, agileUserDetails.getUserData().getPassword())) {
                throw new AgileAuthException("用户名或密码不正确!");
            }
            HttpSession httpSession = AgileServletUtil.getHttpServletRequest().getSession();
            httpSession.setMaxInactiveInterval(60);
            return new AgileUsernamePasswordToken(agileUserDetails, password, agileUserDetails.getAuthorities());
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException(AgileResultCode.FAIL_AUTH_EXCEPTION, ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
