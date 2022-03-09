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

import java.util.ArrayList;
import java.util.List;


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
    @ApiModelProperty(value = "用户名", position = 1)
    @NotEmpty(message = "用户名不能为空!")
    @Size(max = 30, message = "用户名最大长度为30!")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", position = 2)
    @NotEmpty(message = "用户昵称不能为空!")
    @Size(max = 50, message = "用户昵称最大长度为50")
    private String nickName;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码", position = 3)
    private String userPwd;

    /**
     * 用户性别
     */
    @ApiModelProperty(value = "用户性别", position = 4)
    private String userSex;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", position = 5)
    @Size(max = 20, message = "联系电话最大长度为20!")
    private String userPhone;


    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "邮箱地址", position = 6)
    @Size(max = 50, message = "邮箱地址最大长度为50！")
    private String userEmail;

    /**
     * 用户排序
     */
    @ApiModelProperty(value = "用户排序", position = 7)
    private Integer userSort;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "所属部门", position = 8)
    @NotNull(message = "所属部门不能为空！")
    private String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", position = 9, hidden = true)
    @TableField(exist = false)
    private String deptName;
    /**
     * 用户状态（0正常 1停用 )
     */
    @ApiModelProperty(value = "用户状态", position = 10)
    @Pattern(regexp = "[01]", message = "用户状态必须为0或1（0正常 1停用）！")
    private String userStatus;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像", position = 11)
    @Size(max = 50, message = "用户头像最大长度为50")
    private String userAvatar;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", position = 12)
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;
    /**
     * 用户分配角色
     */
    @ApiModelProperty(value = "用户分配角色", position = 13)
    @TableField(exist = false)
    private List<String> roleIdList = new ArrayList<>();
    /**
     * 用户分配岗位
     */
    @ApiModelProperty(value = "用户分配岗位", position = 14)
    @TableField(exist = false)
    private List<String> postIdList = new ArrayList<>();
}
