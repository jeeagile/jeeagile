package com.jeeagile.system.service;


import com.jeeagile.system.vo.AgilePersonInfo;
import com.jeeagile.system.vo.AgileUpdatePerson;
import com.jeeagile.system.vo.AgileUpdatePwd;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysPersonService {
    /**
     * 获取个人详细信息
     */
    AgilePersonInfo getPersonInfo();

    /**
     * 更新个人信息
     */
    boolean updatePersonInfo(AgileUpdatePerson agileUpdatePerson);

    /**
     * 更新个人密码
     *
     * @param agileUpdatePwd
     * @return
     */
    boolean updatePersonPwd(AgileUpdatePwd agileUpdatePwd);

    /**
     * 更新用户头像
     *
     * @param userAvatar
     * @return
     */
    boolean updateUserAvatar(String userAvatar);
}
