package com.jeeagile.core.security.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Accessors
public class AgileLoginUser implements Serializable {
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 租户签名
     */
    private String tenantSign;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 记住我
     */
    private boolean rememberMe;
    /**
     * 登录验证码
     */
    private String captchaCode;
    /**
     * 登录UUID 用于校验验证码使用
     */
    private String uuid;
}
