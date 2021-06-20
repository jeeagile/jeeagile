package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.vo.AgileSysUserInfo;
import com.jeeagile.system.vo.AgileUpdatePwd;
import com.jeeagile.system.vo.AgileUpdateStatus;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserService extends IAgileBaseService<AgileSysUser> {
    /**
     * 获取用户信息
     */
    AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable);

    /**
     * 根据用户ID查询用户信息
     */
    AgileSysUserInfo selectUserById(String userId);

    /**
     * 新增用户信息
     */
    AgileSysUserInfo saveUser(AgileSysUserInfo agileSysUserInfo);

    /**
     * 更新用户信息
     */
    boolean updateUserById(AgileSysUserInfo agileSysUserInfo);

    /**
     * 根据用户ID删除用户信息
     */
    boolean deleteUserById(String userId);

    /**
     * 重置用户密码
     */
    boolean resetUserPwd(AgileUpdatePwd agileUpdatePwd);

    /**
     * 修改用户状态
     */
    boolean changeUserStatus(AgileUpdateStatus agileUpdateStatus);

}
