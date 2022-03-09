package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.vo.AgileUpdatePwd;
import com.jeeagile.system.vo.AgileUpdateStatus;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysUserService extends IAgileBaseService<AgileSysUser> {
    /**
     * 重置用户密码
     */
    boolean resetUserPwd(AgileUpdatePwd agileUpdatePwd);

    /**
     * 修改用户状态
     */
    boolean changeUserStatus(AgileUpdateStatus agileUpdateStatus);
}
