package com.jeeagile.springsecurity.security;


import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.cache.util.AgileCacheUtil;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgileLogical;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.security.annotation.AgileRequiresRoles;
import com.jeeagile.core.security.base.IAgileSecurity;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.security.user.AgileOnlineUser;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.springsecurity.userdetails.AgileUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-07-09
 * @description
 */
@Slf4j
public class AgileSpringSecurity implements IAgileSecurity {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void userLogin(AgileLoginUser agileLoginUser) {
        try {
            UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(agileLoginUser.getUserName(), agileLoginUser.getPassword());
            Authentication authentication = authenticationManager.authenticate(passwordAuthenticationToken);
            AgileUserDetails agileUserDetails = (AgileUserDetails) authentication.getPrincipal();
            if (agileUserDetails != null && StringUtil.isNotEmpty(agileUserDetails.getUsername())) {
                String userToken = agileUserDetails.getUserData().getUserToken();
                AgileCacheUtil.put(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, userToken, agileUserDetails);
                sessionRegistry.registerNewSession(userToken, userToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex.getCause() instanceof AgileBaseException) {
                throw (AgileBaseException) ex.getCause();
            } else if (ex instanceof BadCredentialsException) {
                throw new AgileAuthException("用户登录密码错误！");
            } else {
                log.error("Spring Security用户登录认证出现异常" , ex);
                throw new AgileAuthException("Spring Security用户登录认证出现异常！");
            }
        }
    }

    @Override
    public void userLogout() {
        AgileBaseUser userData = getUserData();
        if (userData != null && StringUtil.isNotEmpty(userData.getUserId())) {
            AgileCacheUtil.evict(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, userData.getUserToken());
            sessionRegistry.removeSessionInformation(userData.getUserToken());
        }
    }

    @Override
    public AgileBaseUser getUserData() {
        return getUserDetails().getUserData();
    }

    @Override
    public boolean checkAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    @Override
    public void checkUser() {
        try {
            AgileBaseUser userData = getUserData();
            if (userData == null || StringUtil.isEmpty(userData.getUserId())) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("用户验证异常" , ex);
            throw new AgileAuthException("用户权限验证异常！");
        }
    }

    @Override
    public void checkPermission(AgileRequiresPermissions agileRequiresPermissions) {
        try {
            if (getUserData().isSuperAdmin()) {
                return;
            }
            String[] perms = agileRequiresPermissions.value();
            if (perms.length == 1) {
                checkPermissions(perms[0]);
                return;
            }
            if (AgileLogical.AND.equals(agileRequiresPermissions.logical())) {
                checkPermissions(perms);
                return;
            }
            if (AgileLogical.OR.equals(agileRequiresPermissions.logical())) {
                boolean hasAtLeastOnePermission = false;
                for (String permission : perms) {
                    if (hasPermission(permission)) {
                        hasAtLeastOnePermission = true;
                        break;
                    }
                }
                if (!hasAtLeastOnePermission) {
                    throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
                }
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("用户权限校验异常" , ex);
            throw new AgileAuthException("用户权限验证异常！");
        }
    }

    @Override
    public void checkRole(AgileRequiresRoles agileRequiresRoles) {
        try {
            if (getUserData().isSuperAdmin()) {
                return;
            }
            String[] roles = agileRequiresRoles.value();
            if (roles.length == 1) {
                checkRoles(roles[0]);
                return;
            }
            if (AgileLogical.AND.equals(agileRequiresRoles.logical())) {
                checkRoles(roles);
                return;
            }
            if (AgileLogical.OR.equals(agileRequiresRoles.logical())) {
                boolean hasAtLeastOneRole = false;
                for (String role : roles) {
                    if (hasRole(role)) {
                        hasAtLeastOneRole = true;
                        break;
                    }
                }
                if (!hasAtLeastOneRole) {
                    throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
                }
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("用户角色校验异常" , ex);
            throw new AgileAuthException("用户权限验证异常！");
        }
    }

    private boolean hasPermission(String permission) {
        return ArrayUtils.contains(this.getUserData().getUserPerm().toArray(), permission);
    }

    private void checkPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
            }
        }
    }

    private boolean hasRole(String role) {
        return ArrayUtils.contains(this.getUserData().getUserRole().toArray(), role);
    }

    private void checkRoles(String... roles) {
        for (String role : roles) {
            if (!hasRole(role)) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
            }
        }
    }

    @Override
    public List<AgileOnlineUser> getOnlineUserList() {
        List<AgileOnlineUser> onlineUserList = new ArrayList<>();
        List<Object> sessionIdList = sessionRegistry.getAllPrincipals();
        for (Object sessionId : sessionIdList) {
            AgileUserDetails agileUserDetails = (AgileUserDetails) AgileCacheUtil.get(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, sessionId.toString());
            if (agileUserDetails == null || StringUtil.isEmpty(agileUserDetails.getUsername())) {
                sessionRegistry.removeSessionInformation(sessionId.toString());
                continue;
            }
            SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId.toString());
            if (sessionInformation == null) {
                sessionRegistry.removeSessionInformation(sessionId.toString());
                continue;
            }
            AgileOnlineUser agileOnlineUser = new AgileOnlineUser();
            BeanUtils.copyProperties(agileUserDetails.getUserData(), agileOnlineUser);
            agileOnlineUser.setStartAccessTime(agileUserDetails.getUserData().getLoginTime());
            agileOnlineUser.setLastAccessTime(sessionInformation.getLastRequest());
            onlineUserList.add(agileOnlineUser);
        }
        return onlineUserList;
    }

    @Override
    public long countOnlineUser() {
        return getOnlineUserList().size();
    }

    @Override
    public boolean forceLogout(String tokenId) {
        AgileCacheUtil.evict(AgileCacheConstants.AGILE_CACHE_SESSION_NAME, tokenId);
        sessionRegistry.removeSessionInformation(tokenId);
        return true;
    }

    public static AgileUserDetails getUserDetails() {
        try {
            return (AgileUserDetails) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new AgileAuthException("获取用户信息异常");
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}