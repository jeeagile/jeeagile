package com.jeeagile.core.security.util;

import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.cache.util.AgileCacheUtil;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.IAgileSecurity;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.security.user.AgileOnlineUser;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.core.util.spring.AgileServletUtil;
import com.jeeagile.core.util.spring.AgileSpringUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import org.springframework.beans.BeansException;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户安全工具类
 */
public class AgileSecurityUtil {


    private AgileSecurityUtil() {
    }

    /**
     * 用户登录
     *
     * @param agileLoginUser
     */
    public static void userLogin(AgileLoginUser agileLoginUser) {
        if (AgileUtil.isCaptchaEnabled()) {
            String code = (String) AgileCacheUtil.get(AgileCacheConstants.AGILE_CACHE_KAPTCHA_NAME, agileLoginUser.getUuid());
            if (AgileStringUtil.isEmpty(code)) {
                throw new AgileFrameException(AgileResultCode.FAIL_KCAPTCHA_EXPIRE, "验证码已失效！");
            }
            if (!code.equals(agileLoginUser.getCaptchaCode())) {
                throw new AgileFrameException(AgileResultCode.FAIL_KCAPTCHA_ERROR, "验证码错误！");
            }
        }
        getAgileSecurity().userLogin(agileLoginUser);
    }

    /**
     * 用户退出
     */
    public static void userLogout() {
        getAgileSecurity().userLogout();
    }

    /**
     * 用户是否认证通过
     *
     * @return
     */
    public static boolean checkAuthenticated() {
        return getAgileSecurity().checkAuthenticated();
    }


    /***
     * 获取当前登录用户信息
     * @param <T>
     * @return
     */
    public static <T extends AgileBaseUser> T getUserData() {
        return (T) getAgileSecurity().getUserData();
    }

    /**
     * 获取用户ID
     */
    public static String getUserId() {
        AgileBaseUser agileBaseUser = getUserData();
        if (agileBaseUser != null) {
            return agileBaseUser.getUserId();
        } else {
            return null;
        }
    }

    /**
     * 获取用户租户ID
     */
    public static String getTenantId() {
        AgileBaseUser agileBaseUser = getUserData();
        if (agileBaseUser != null) {
            return agileBaseUser.getTenantId();
        } else {
            return AgileSecurityContext.getTenantId();
        }
    }

    /**
     * 获取用户租户编码
     */
    public static String getTenantCode() {
        AgileBaseUser agileBaseUser = getUserData();
        if (agileBaseUser != null) {
            return agileBaseUser.getTenantCode();
        }
        return null;
    }

    /**
     * 获取在线用户列表
     */
    public static List<AgileOnlineUser> getOnlineUserList() {
        return getAgileSecurity().getOnlineUserList();
    }

    /**
     * 在线人数
     *
     * @return
     */
    public static long countOnlineUser() {
        return getAgileSecurity().countOnlineUser();
    }

    /**
     * 剔除用户
     */
    public static boolean forceLogout(String tokenId) {
        return getAgileSecurity().forceLogout(tokenId);
    }

    /**
     * 获取用户安全接口bean
     *
     * @return
     */
    public static IAgileSecurity getAgileSecurity() {
        IAgileSecurity agileSecurity;
        try {
            agileSecurity = (IAgileSecurity) AgileSpringUtil.getBean("AgileSecurity");
        } catch (BeansException ex) {
            throw new AgileAuthException("请检查安全接口实现类《IAgileSecurity》是否配置正确！");
        }
        if (agileSecurity == null) {
            throw new AgileAuthException("请检查安全接口实现类《IAgileSecurity》是否配置正确！");
        }
        return agileSecurity;
    }

    /**
     * 密码加密
     *
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
