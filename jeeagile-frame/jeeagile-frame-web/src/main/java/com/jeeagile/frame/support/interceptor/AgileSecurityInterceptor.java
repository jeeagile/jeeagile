package com.jeeagile.frame.support.interceptor;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileDemoException;
import com.jeeagile.core.security.annotation.AgileRequiresAuthentication;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.security.annotation.AgileRequiresRoles;
import com.jeeagile.core.security.annotation.AgileRequiresUser;
import com.jeeagile.core.security.base.IAgileSecurity;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
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
            if(handlerMethod.getBean() instanceof ErrorController){
                return true;
            }
            checkUserSecurity(handlerMethod);
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
            IAgileSecurity agileSecurity = AgileSecurityUtil.getAgileSecurity();
            if (agileSecurity == null) {
                throw new AgileAuthException("请设置用户安全接口类《UserSecurity》");
            }

            AgileDemo agileDemo = handlerMethod.getMethodAnnotation(AgileDemo.class);
            if (agileDemo != null && AgileUtil.isDemoEnabled()) {
                throw new AgileDemoException();
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

            AgileRequiresPermissions agileRequiresPermissions = handlerMethod.getMethodAnnotation(AgileRequiresPermissions.class);
            if (agileRequiresPermissions != null) {
                agileSecurity.checkPermission(agileRequiresPermissions);
            }
            AgileSecurityContext.putCurrentUser(agileSecurity.getUserData());
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("用户权限验证异常", ex);
            throw new AgileAuthException("用户权限验证异常！");
        }
    }
}