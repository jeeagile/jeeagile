package com.jeeagile.core.security;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.security.annotation.AgileRequiresRoles;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.security.user.AgileOnlineUser;
import com.jeeagile.core.util.AgileStringUtil;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSecurity {
    /**
     * 用户登录
     */
    void userLogin(AgileLoginUser agileLoginUser);

    /**
     * 用户退出
     */
    void userLogout();

    /**
     * 用户是否验证通过
     *
     * @return
     */
    boolean checkAuthenticated();

    /**
     * 验证用户
     *
     * @return
     */
    void checkUser();

    /**
     * 获取当前session用户信息
     *
     * @return
     */
    AgileBaseUser getUserData();

    /**
     * 获取在线用户列表
     *
     * @return
     */
    List<AgileOnlineUser> getOnlineUserList();

    /**
     * 在线人数
     *
     * @return
     */
    long countOnlineUser();

    /**
     * 用户强退
     *
     * @param tokenId
     */
    boolean forceLogout(String tokenId);

    /**
     * 校验用户权限
     *
     * @return
     */
    void checkPermission(AgileRequiresPermissions agileRequiresPermissions);

    /**
     * 校验用户权限
     *
     * @return
     */
    void checkPermission(AgilePermissionsPrefix agilePermissionsPrefix, AgileRequiresPermissions agileRequiresPermissions);

    /**
     * 拼接权限字符串
     *
     * @param agilePermissionsPrefix
     * @param agileRequiresPermissions
     * @return
     */
    default String[] getPermissions(AgilePermissionsPrefix agilePermissionsPrefix, AgileRequiresPermissions agileRequiresPermissions) {
        String prefix = agilePermissionsPrefix.value();
        String[] perms = agileRequiresPermissions.value();
        if (AgileStringUtil.isNotEmpty(prefix)) {
            String[] newPerms = new String[perms.length];
            for (int i = 0; i < perms.length; i++) {
                newPerms[i] = prefix + ":" + perms[i];
            }
            return newPerms;
        }
        return perms;
    }

    /**
     * 校验用户角色
     *
     * @return
     */
    void checkRole(AgileRequiresRoles agileRequiresRoles);
}
