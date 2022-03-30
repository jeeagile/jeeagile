package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysUser;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserService extends IAgileBaseService<AgileSysUser> {
    /**
     * 重置用户密码
     */
    boolean resetUserPassword(Serializable userId, String password);

    /**
     * 修改用户状态
     */
    boolean changeUserStatus(Serializable userId, String userStatus);
}
