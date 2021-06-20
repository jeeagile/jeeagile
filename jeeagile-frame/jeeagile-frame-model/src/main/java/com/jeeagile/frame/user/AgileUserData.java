package com.jeeagile.frame.user;

import com.jeeagile.core.security.user.AgileBaseUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileUserData extends AgileBaseUser {
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 联系手机
     */
    private String userMobile;

    /**
     * 邮箱地址
     */
    private String userEmail;

    /**
     * 用户头像
     */
    private String userAvatar;

    @Override
    public String toString(){
       return this.getUserName();
    }
}
