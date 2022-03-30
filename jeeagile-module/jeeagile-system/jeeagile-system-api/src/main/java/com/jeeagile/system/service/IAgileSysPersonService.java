package com.jeeagile.system.service;


import com.jeeagile.system.vo.AgilePersonInfo;
import com.jeeagile.system.vo.AgileSysPerson;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysPersonService {
    /**
     * 获取个人详细信息
     */
    AgileSysPerson getAgileSysPerson();

    /**
     * 更新个人信息
     */
    boolean updatePersonInfo(AgilePersonInfo agilePersonInfo);

    /**
     * 更新个人密码
     * @param oldPwd 老密码
     * @param newPwd 新密码
     * @return
     */
    boolean updatePersonPassword(String oldPwd, String newPwd);

    /**
     * 更新用户头像
     *
     * @param userAvatar
     * @return
     */
    boolean updatePersonAvatar(String userAvatar);
}
