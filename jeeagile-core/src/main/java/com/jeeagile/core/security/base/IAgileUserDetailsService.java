package com.jeeagile.core.security.base;

import com.jeeagile.core.security.user.AgileBaseUser;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户信息接口
 */
public interface IAgileUserDetailsService {

    /**
     * 根据用户名、密码获取用户登录信息
     *
     * @param loginName
     * @param password
     * @return
     */
    AgileBaseUser userLogin(String loginName, String password);

    /**
     * 根据用户名加载用户信息
     *
     * @param loginName
     * @return
     */
    AgileBaseUser getUserDataByLoginName(String loginName);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    AgileBaseUser getUserDataByUserId(String userId);

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
}
