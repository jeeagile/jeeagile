package com.jeeagile.shiro.realm;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileAgentUtil;
import com.jeeagile.core.util.AgileNetUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileServletUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import com.jeeagile.shiro.authc.AgileUsernamePasswordToken;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileAuthorizingRealm extends AuthorizingRealm {

    @Lazy
    @AgileReference
    private IAgileUserDetailsService agileUserDetailsService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        if (agileUserDetailsService == null) {
            throw new AgileFrameException(AgileResultCode.FAIL_SERVER_EXCEPTION, "请配置用户信息接口实现类！");
        }
        String loginName = (String) authenticationToken.getPrincipal();// 获取用户名
        char[] credentials = (char[]) authenticationToken.getCredentials();// 字符类型密码获取(用户输入的密码)
        if (credentials == null || credentials.length < 1) {
            return null;
        }
        String password = new String(credentials);// 把字符数组转换为String类型(用户输入的密码)
        try {
            AgileBaseUser userData;
            if (AgileTenantUtil.isTenantEnable()) {
                AgileUsernamePasswordToken agileUsernamePasswordToken = (AgileUsernamePasswordToken) authenticationToken;
                userData = agileUserDetailsService.getUserData(loginName, agileUsernamePasswordToken.getTenantId(), agileUsernamePasswordToken.getTenantSign());
            } else {
                userData = agileUserDetailsService.getUserData(loginName);
            }
            if (userData != null && AgileStringUtil.isNotEmpty(userData.getUserId())) {
                if (AgileSecurityUtil.encryptPassword(password).equals(userData.getPassword())) {
                    userData.setUserToken(SecurityUtils.getSubject().getSession().getId().toString());
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
                    return new SimpleAuthenticationInfo(userData, password, userData.getUserName());
                } else {
                    throw new AgileAuthException(AgileResultCode.FAIL_USER_PWD);
                }
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_NAME);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException(AgileResultCode.FAIL_AUTH_EXCEPTION, ex);
        }
    }

    /**
     * 用户授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        try {
            AgileBaseUser userData = (AgileBaseUser) principalCollection.getPrimaryPrincipal();

            if (userData == null || AgileStringUtil.isEmpty(userData.getUserId())) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
            SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
            List<String> userPermList = userData.getUserPermList();
            if (userPermList == null || userPermList.isEmpty()) {
                userPermList = agileUserDetailsService.getUserPermList(userData);
                userData.setUserPermList(userPermList);
            }
            authenticationInfo.addStringPermissions(userPermList);

            List<String> userRoleList = userData.getUserRoleList();
            if (userRoleList == null || userRoleList.isEmpty()) {
                userRoleList = agileUserDetailsService.getUserRoleList(userData);
                userData.setUserRoleList(userRoleList);
            }
            authenticationInfo.addRoles(userRoleList);
            return authenticationInfo;
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException(AgileResultCode.FAIL_AUTH_EXCEPTION, ex.getMessage());
        }
    }
}
