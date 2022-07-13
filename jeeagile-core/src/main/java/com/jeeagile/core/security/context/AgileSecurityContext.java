package com.jeeagile.core.security.context;

import com.jeeagile.core.protocol.enums.AgileProtocolType;
import com.jeeagile.core.protocol.properties.AgileProtocolProperties;
import com.jeeagile.core.security.IAgileSecurity;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.spring.AgileSpringUtil;
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
    protected static final ThreadLocal<AgileBaseUser> userThreadLocal = new InheritableThreadLocal<>();

    //存储当前线程租户ID
    protected static final ThreadLocal<String> tenantThreadLocal = new InheritableThreadLocal<>();

    //存储当前线程租户禁用标志
    protected static final ThreadLocal<Boolean> disableTenantThreadLocal = new InheritableThreadLocal<>();

    /**
     * 存放当前用户信息
     *
     * @param agileBaseUser
     */
    public static void putUserData(AgileBaseUser agileBaseUser) {
        userThreadLocal.set(agileBaseUser);
    }

    /**
     * 存放当前租户ID
     *
     * @param tenantId
     */
    public static void putTenantId(String tenantId) {
        tenantThreadLocal.set(tenantId);
    }

    public static void putDisableTenant(boolean flag) {
        disableTenantThreadLocal.set(flag);
    }

    /**
     * 获取当前线程用户信息
     *
     * @return
     */
    public static AgileBaseUser getUserData() {
        if (agileProtocolProperties.getType() == AgileProtocolType.LOCAL) {
            AgileBaseUser agileBaseUser;
            try {
                agileBaseUser = getAgileSecurity();
                if (agileBaseUser == null) {
                    agileBaseUser = userThreadLocal.get();
                }
            } catch (Exception ex) {
                agileBaseUser = userThreadLocal.get();
            }
            return agileBaseUser;
        } else {
            return userThreadLocal.get();
        }
    }

    /**
     * 从安全认证接口类中获取用户信息
     *
     * @return
     */
    private static AgileBaseUser getAgileSecurity() {
        try {
            IAgileSecurity agileSecurity = (IAgileSecurity) AgileSpringUtil.getBean("AgileSecurity");
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
    public static String getUserId() {
        AgileBaseUser userData = getUserData();
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
    public static String getUserName() {
        AgileBaseUser userData = getUserData();
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
    public static String getUserToken() {
        AgileBaseUser userData = getUserData();
        if (userData != null) {
            return userData.getUserToken();
        } else {
            return null;
        }
    }

    public static String getTenantId() {
        return tenantThreadLocal.get();
    }

    public static boolean getDisableTenant() {
        return disableTenantThreadLocal.get() == null ? false : disableTenantThreadLocal.get();
    }

    /**
     * 移除当前线程
     */
    public static void removeUser() {
        userThreadLocal.remove();
    }

    public static void removeTenant() {
        tenantThreadLocal.remove();
    }

    public static void removeDisableTenant() {
        disableTenantThreadLocal.remove();
    }
}
