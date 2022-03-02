package com.jeeagile.core.security.context;

import com.jeeagile.core.protocol.enums.AgileProtocolType;
import com.jeeagile.core.protocol.properties.AgileProtocolProperties;
import com.jeeagile.core.security.IAgileSecurity;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.util.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户信息上下文
 */
@Slf4j
@Component
public class AgileSecurityContext {

    private static AgileProtocolProperties agileProtocolProperties;

    private AgileSecurityContext(AgileProtocolProperties agileProtocolProperties) {
        this.agileProtocolProperties = agileProtocolProperties;
    }

    //存储当前线程用户信息
    protected static final ThreadLocal<AgileBaseUser> threadLocal = new InheritableThreadLocal<>();

    /**
     * 存放当前用户信息
     *
     * @param agileBaseUser
     */
    public static void putCurrentUser(AgileBaseUser agileBaseUser) {
        threadLocal.set(agileBaseUser);
    }

    /**
     * 获取当前线程用户信息
     *
     * @return
     */
    public static AgileBaseUser getCurrentUser() {
        if (agileProtocolProperties.getType() == AgileProtocolType.LOCAL) {
            return getAgileSecurity();
        } else {
            return threadLocal.get();
        }
    }

    /**
     * 从安全认证接口类中获取用户信息
     *
     * @return
     */
    private static AgileBaseUser getAgileSecurity() {
        try {
            IAgileSecurity agileSecurity = (IAgileSecurity) SpringContextUtil.getBean("AgileSecurity");
            if (agileSecurity != null) {
                return agileSecurity.getUserData();
            }
        } catch (BeansException ex) {
            log.warn("未获取到安全接口《IAgileSecurity》配置Bean！");
        }
        return null;
    }

    /**
     * 获取当前线程用户ID
     *
     * @return
     */
    public static String getCurrentUserId() {
        AgileBaseUser userData = getCurrentUser();
        if (userData != null) {
            return userData.getUserId();
        } else {
            return null;
        }
    }

    /**
     * 获取当前线程用户名称
     *
     * @return
     */
    public static String getCurrentUserName() {
        AgileBaseUser userData = getCurrentUser();
        if (userData != null) {
            return userData.getUserName();
        } else {
            return null;
        }
    }

    /**
     * 获取当前线程用户名称
     *
     * @return
     */
    public static String getCurrentUserToken() {
        AgileBaseUser userData = getCurrentUser();
        if (userData != null) {
            return userData.getUserToken();
        } else {
            return null;
        }
    }

    /**
     * 清除当前线程
     */
    public void remove() {
        threadLocal.remove();
    }

}
