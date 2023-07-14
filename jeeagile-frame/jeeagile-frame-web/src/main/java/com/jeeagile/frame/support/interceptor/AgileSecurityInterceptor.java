package com.jeeagile.frame.support.interceptor;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileDemoException;
import com.jeeagile.core.security.IAgileSecurity;
import com.jeeagile.core.security.annotation.*;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.controller.AgileCrudController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileSecurityInterceptor implements AsyncHandlerInterceptor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (!(handlerMethod.getBean() instanceof ErrorController)) {
                checkUserSecurity(handlerMethod);
            }
        }
        return true;
    }

    /**
     * 权限校验
     *
     * @param handlerMethod
     */
    private void checkUserSecurity(HandlerMethod handlerMethod) {
        try {
            //获取当前用户安全认证
            IAgileSecurity agileSecurity = AgileSecurityUtil.getAgileSecurity();
            if (agileSecurity == null) {
                throw new AgileAuthException("请设置用户安全接口类《UserSecurity》");
            }


            AgileRequiresGuest agileRequiresGuest = handlerMethod.getBeanType().getAnnotation(AgileRequiresGuest.class);
            if (agileRequiresGuest != null) {
                return;
            }

            agileRequiresGuest = handlerMethod.getMethodAnnotation(AgileRequiresGuest.class);
            if (agileRequiresGuest != null) {
                return;
            }

            //获取用户信息
            AgileBaseUser agileBaseUser = agileSecurity.getUserData();
            //当前线程存放用户信息
            AgileSecurityContext.putUserData(agileBaseUser);

            if (AgileUtil.isDemoEnabled()) {
                AgileDemo agileDemo = handlerMethod.getMethodAnnotation(AgileDemo.class);
                if (AgileTenantUtil.isTenantEnable()) {
                    if (AgileTenantUtil.getDefaultTenant().equals(agileBaseUser.getTenantId())) {
                        if (agileDemo != null) {
                            throw new AgileDemoException();
                        }
                    }
                } else {
                    if (agileDemo != null) {
                        throw new AgileDemoException();
                    }
                }
            }

            //如果为超管用户则不在进行权限校验 或者为租户管理员
            if (agileSecurity.getUserData().isSuperAdmin() || agileBaseUser.getUserName().equals(agileBaseUser.getTenantCode())) {
                return;
            }

            AgileRequiresAuthentication agileRequiresAuthentication = handlerMethod.getMethodAnnotation(AgileRequiresAuthentication.class);
            if (agileRequiresAuthentication != null && !agileSecurity.checkAuthenticated()) {
                throw new AgileAuthException("用户未验证通过！");
            }

            AgileRequiresUser agileRequiresUser = handlerMethod.getMethodAnnotation(AgileRequiresUser.class);
            if (agileRequiresUser != null) {
                agileSecurity.checkUser();
            }

            AgileRequiresRoles agileRequiresRoles = handlerMethod.getMethodAnnotation(AgileRequiresRoles.class);
            if (agileRequiresRoles != null) {
                agileSecurity.checkRole(agileRequiresRoles);
            }

            AgilePermissionsPrefix agilePermissionsPrefix = handlerMethod.getBeanType().getAnnotation(AgilePermissionsPrefix.class);
            AgileRequiresPermissions agileRequiresPermissions = handlerMethod.getMethodAnnotation(AgileRequiresPermissions.class);
            if (agileRequiresPermissions != null) {
                if (agilePermissionsPrefix != null) {
                    agileSecurity.checkPermission(agilePermissionsPrefix, agileRequiresPermissions);
                } else {
                    if (!(handlerMethod.getBean() instanceof AgileCrudController)) {
                        agileSecurity.checkPermission(agileRequiresPermissions);
                    }
                }
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("用户权限验证异常", ex);
            throw new AgileAuthException("用户权限验证异常！");
        }
    }
}
