package com.jeeagile.core.security.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@Accessors
public class AgileOnlineUser implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户TOKEN
     */
    private String userToken;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 登录IP
     */
    private String loginIp;
    /**
     * 登录地址
     */
    private String loginAddress;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 操作系统类型
     */
    private String osName;
    /**
     * 客户端类型
     */
    private String deviceName;
    /**
     * 浏览器类型
     */
    private String browserName;
    /**
     * 首次登陆时间
     */
    private Date startAccessTime;
    /**
     * 最后访问时间
     */
    private Date lastAccessTime;
}
