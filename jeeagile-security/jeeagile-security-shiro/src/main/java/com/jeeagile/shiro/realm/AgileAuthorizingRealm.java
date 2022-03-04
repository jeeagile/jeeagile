package com.jeeagile.shiro.realm;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileNetUtil;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.UserAgentUtil;
import com.jeeagile.core.util.spring.SpringServletUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileAuthorizingRealm extends AuthorizingRealm {

    @AgileReference
    private IAgileUserDetailsService agileUserDetailsService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        if (agileUserDetailsService == null) {
            throw new AgileFrameException(AgileResultCode.FAIL_SERVER_EXCEPTION, "请设置用户验证接口实现类！");
        }
        String loginName = (String) authenticationToken.getPrincipal();// 获取用户名
        char[] credentials = (char[]) authenticationToken.getCredentials();// 字符类型密码获取(用户输入的密码)
        if (credentials == null || credentials.length < 1) {
            return null;
        }
        String password = new String(credentials);// 把字符数组转换为String类型(用户输入的密码)
        try {
            AgileBaseUser userData = agileUserDetailsService.getUserDataByLoginName(loginName);
            if (userData != null && StringUtil.isNotEmpty(userData.getUserId())) {
                if (AgileSecurityUtil.encryptPassword(password).equals(userData.getPassword())) {
                    userData.setUserToken(SecurityUtils.getSubject().getSession().getId().toString());
                    userData.setUserPerm(agileUserDetailsService.getUserPerm(userData));
                    userData.setUserRole(agileUserDetailsService.getUserRole(userData));
                    HttpServletRequest httpServletRequest = SpringServletUtil.getHttpServletRequest();
                    if (httpServletRequest != null) {
                        UserAgent userAgent = UserAgentUtil.getUserAgent(httpServletRequest);
                        userData.setLoginIp(UserAgentUtil.getUserIp(httpServletRequest));
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

            if (userData == null || StringUtil.isEmpty(userData.getUserId())) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
            SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
            List<String> userPermList = userData.getUserPerm();
            if (userPermList == null || userPermList.isEmpty()) {
                userPermList = agileUserDetailsService.getUserPerm(userData);
                userData.setUserPerm(userPermList);
            }
            authenticationInfo.addStringPermissions(userPermList);

            List<String> userRoleList = userData.getUserRole();
            if (userRoleList == null || userRoleList.isEmpty()) {
                userRoleList = agileUserDetailsService.getUserRole(userData);
                userData.setUserRole(userRoleList);
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
