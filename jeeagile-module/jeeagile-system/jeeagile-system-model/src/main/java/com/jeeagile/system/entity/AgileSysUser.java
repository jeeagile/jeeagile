package com.jeeagile.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class AgileSysUser extends AgileBaseModel<AgileSysUser> {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空!")
    @Size(max = 30, message = "用户名最大长度为30!")
    private String userName;

    /**
     * 用户昵称
     */
    @NotEmpty(message = "用户昵称不能为空!")
    @Size(max = 50, message = "用户昵称最大长度为50")
    private String nickName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 联系电话
     */
//    @Pattern(regexp = Validator.PHONE_REGEX, message = "联系电话必须按照国家规定填写!")
    @Size(max = 20, message = "联系电话最大长度为20!")
    private String userPhone;


    /**
     * 邮箱地址
     */
//    @Pattern(regexp = Validator.EMAIL_REGEX, message = "邮箱地址不正确！")
    @Size(max = 50, message = "邮箱地址最大长度为50！")
    private String userEmail;

    /**
     * 用户排序
     */
    private Integer userSort;

    /**
     * 部门ID
     */
    @NotNull(message = "所属部门不能为空！")
    private String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String deptName;
    /**
     * 用户状态（0正常 1停用 )
     */
    @Pattern(regexp = "[01]", message = "用户状态必须为0或1（0正常 1停用）！")
    private String userStatus;

    /**
     * 用户头像
     */
    @Size(max = 50, message = "用户头像最大长度为50")
    private String userAvatar;

    /**
     * 备注
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;
}
