package com.jeeagile.shiro.security;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.IAgileSecurity;
import com.jeeagile.core.security.annotation.AgileLogical;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.security.annotation.AgileRequiresRoles;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.security.user.AgileOnlineUser;
import com.jeeagile.core.util.AgileStringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileShiroSecurity implements IAgileSecurity {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public void userLogin(AgileLoginUser agileLoginUser) {
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(agileLoginUser.getUserName(), agileLoginUser.getPassword());
            token.setRememberMe(agileLoginUser.isRememberMe());
            SecurityUtils.getSubject().login(token);
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            if (ex.getCause() instanceof AgileBaseException) {
                throw (AgileBaseException) ex.getCause();
            }
            throw new AgileAuthException("SHIRO用户登录认证出现异常！");
        }
    }

    @Override
    public void userLogout() {
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception ex) {
            logger.error("SHIRO用户退出出现异常", ex);
            throw new AgileAuthException("SHIRO用户退出出现异常！");
        }
    }

    @Override
    public AgileBaseUser getUserData() {
        try {
            return (AgileBaseUser) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception ex) {
            logger.error("SHIRO获取当前登录用户信息异常", ex);
            throw new AgileAuthException("获取登录用户信息异常！");
        }
    }

    @Override
    public boolean checkAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    @Override
    public void checkUser() {
        try {
            if (SecurityUtils.getSubject().getPrincipal() == null) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
            }
        } catch (Exception ex) {
            logger.error("SHIRO用户验证异常", ex);
            throw new AgileAuthException("SHIRO用户验证异常！");
        }
    }

    /**
     * 校验用户权限
     *
     * @param perms
     * @param agileLogical
     */
    private void checkPermission(String[] perms, AgileLogical agileLogical) {
        try {
            Subject subject = SecurityUtils.getSubject();
            if (perms.length == 1) {
                subject.checkPermission(perms[0]);
                return;
            }
            if (AgileLogical.AND.equals(agileLogical)) {
                subject.checkPermissions(perms);
                return;
            }
            if (AgileLogical.OR.equals(agileLogical)) {
                boolean hasAtLeastOnePermission = false;
                for (String permission : perms)
                    if (subject.isPermitted(permission)) hasAtLeastOnePermission = true;
                if (!hasAtLeastOnePermission) subject.checkPermission(perms[0]);
            }
        } catch (Exception ex) {
            logger.error("用户权限校验异常:{}", ex.getMessage());
            throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
        }
    }

    @Override
    public void checkPermission(AgileRequiresPermissions agileRequiresPermissions) {
        this.checkPermission(agileRequiresPermissions.value(), agileRequiresPermissions.logical());
    }

    @Override
    public void checkPermission(AgilePermissionsPrefix agilePermissionsPrefix, AgileRequiresPermissions agileRequiresPermissions) {
        String[] perms = this.getPermissions(agilePermissionsPrefix, agileRequiresPermissions);
        this.checkPermission(perms, agileRequiresPermissions.logical());
    }


    @Override
    public void checkRole(AgileRequiresRoles agileRequiresRoles) {
        try {
            String[] roles = agileRequiresRoles.value();
            Subject subject = SecurityUtils.getSubject();
            if (roles.length == 1) {
                subject.checkRole(roles[0]);
                return;
            }
            if (AgileLogical.AND.equals(agileRequiresRoles.logical())) {
                subject.checkRoles(Arrays.asList(roles));
                return;
            }
            if (AgileLogical.OR.equals(agileRequiresRoles.logical())) {
                boolean hasAtLeastOneRole = false;
                for (String role : roles) if (subject.hasRole(role)) hasAtLeastOneRole = true;
                if (!hasAtLeastOneRole) subject.checkRole(roles[0]);
            }
        } catch (Exception ex) {
            logger.error("SHIRO用户角色校验异常", ex);
            throw new AgileAuthException(AgileResultCode.FAIL_USER_PERMS);
        }
    }

    @Override
    public List<AgileOnlineUser> getOnlineUserList() {
        List<AgileOnlineUser> onlineUserList = new ArrayList<>();
        Collection<Session> sessionCollection = sessionDAO.getActiveSessions();
        for (Session session : sessionCollection) {
            Object pc = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (pc instanceof PrincipalCollection) {
                AgileBaseUser userData = (AgileBaseUser) ((PrincipalCollection) pc).getPrimaryPrincipal();
                if (userData == null || AgileStringUtil.isEmpty(userData.getUserName())) {
                    continue;
                }
                AgileOnlineUser agileOnlineUser = new AgileOnlineUser();
                BeanUtils.copyProperties(userData, agileOnlineUser);
                agileOnlineUser.setStartAccessTime(session.getStartTimestamp());
                agileOnlineUser.setLastAccessTime(session.getLastAccessTime());
                onlineUserList.add(agileOnlineUser);
            }
        }
        return onlineUserList;
    }

    @Override
    public long countOnlineUser() {
        return sessionDAO.getActiveSessions().size();
    }

    @Override
    public boolean forceLogout(String tokenId) {
        Session session = sessionDAO.readSession(tokenId);
        if (session != null) {
            session.setTimeout(0);
            session.stop();
            sessionDAO.delete(session);
        }
        return true;
    }
}
