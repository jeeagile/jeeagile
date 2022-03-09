package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.vo.AgileUpdateStatus;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysRoleService extends IAgileBaseService<AgileSysRole> {
    /**
     * 修改角色状态
     */
    boolean changeRoleStatus(AgileUpdateStatus agileUpdateStatus);

    /**
     * 修改角色数据范围
     */
    boolean updateRoleDataScope(AgileSysRole agileSysRole);
}
