package com.jeeagile.process.service;

import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-06-07
 * @description 流程设计
 */
public interface IAgileProcessDesignerService {
    /**
     * 分页查询用户列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable);

    /**
     * 根据用户ID获取用户昵称
     *
     * @param userIds
     * @return
     */
    List<String> detailUserNickName(String userIds);
}
