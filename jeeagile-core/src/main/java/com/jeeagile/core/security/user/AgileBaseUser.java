package com.jeeagile.core.security.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeeagile.core.util.AgileUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户基础信息
 */
@Data
@Accessors
public abstract class AgileBaseUser implements Serializable {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    @JSONField(serialize = false)
    private String password;

    /**
     * 用户TOKEN
     */
    private String userToken;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 用户状态（0正常 1停用 )
     */
    private String userStatus;

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
     * 登录IP
     */
    private String loginIp;
    /**
     * 登录地址
     */
    private String loginAddress;

    /**
     * 登录时间
     */
    private Date loginTime = new Date();

    /**
     * 用户权限编码
     */
    private List<String> userPerm;

    /**
     * 用户角色编码
     */
    private List<String> userRole;

    /**
     * 判断用户是否为超管用户
     * @return
     */
    public boolean isSuperAdmin() {
        return this.userId.equals("0") || AgileUtil.isSuperAdmin(this.userName);
    }

    /**
     * 设置缓存key值为用户ID
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getUserId();
    }

}
