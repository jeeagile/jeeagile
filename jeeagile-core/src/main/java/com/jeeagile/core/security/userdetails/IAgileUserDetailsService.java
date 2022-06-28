package com.jeeagile.core.security.userdetails;

import com.jeeagile.core.security.user.AgileBaseUser;

import java.util.List;
import java.util.Set;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户信息接口
 */
public interface IAgileUserDetailsService {

    /**
     * 根据用户名加载用户信息
     *
     * @param loginName
     * @return
     */
    AgileBaseUser getUserData(String loginName);

    /**
     * 租户模式获取用户信息
     *
     * @param loginName
     * @param tenantId
     * @param tenantSign
     * @return
     */
    AgileBaseUser getUserData(String loginName, String tenantId, String tenantSign);

    /**
     * 获取用户权限
     *
     * @param userData
     * @return
     */
    List<String> getUserPerm(AgileBaseUser userData);

    /**
     * 获取用户角色列表
     *
     * @param userData
     * @return
     */
    List<String> getUserRole(AgileBaseUser userData);

    /**
     * 获取用户权限菜单
     *
     * @param userData
     * @return
     */
    List<Object> getUserMenu(AgileBaseUser userData);

    /**
     * 获取用户数据权限类型
     *
     * @param agileBaseUser
     * @return
     */
    List<String> getUserDataScope(AgileBaseUser agileBaseUser);

    /**
     * 获取用户部门权限
     *
     * @param agileBaseUser
     * @param dataScopeType
     * @return
     */
    Set<String> getUserDeptScopeList(AgileBaseUser agileBaseUser, String dataScopeType);
}
