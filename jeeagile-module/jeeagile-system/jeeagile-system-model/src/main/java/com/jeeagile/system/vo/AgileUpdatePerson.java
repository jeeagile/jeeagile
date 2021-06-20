package com.jeeagile.system.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 个人用户信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileUpdatePerson {
    /**
     * 用户昵称
     */
    @NotNull(message = "用户昵称不能为空!")
    @Size(max = 50, message = "用户昵称最大长度为150")
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
     * 邮箱地址
     */
    private String userEmail;
}
