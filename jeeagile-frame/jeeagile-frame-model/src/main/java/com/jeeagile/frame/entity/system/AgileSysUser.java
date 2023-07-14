package com.jeeagile.frame.entity.system;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@HeadRowHeight(value = 20)
@ContentRowHeight(value = 18)
public class AgileSysUser extends AgileBaseTenantModel<AgileSysUser> {
    /**
     * 用户名
     */
    @ExcelProperty(value = {"用户信息", "用户名"}, index = 0)
    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空!")
    @Size(max = 30, message = "用户名最大长度为30!")
    private String userName;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = {"用户信息", "用户昵称"}, index = 1)
    @ApiModelProperty("用户昵称")
    @NotEmpty(message = "用户昵称不能为空!")
    @Size(max = 50, message = "用户昵称最大长度为50")
    private String nickName;

    /**
     * 用户密码
     */
    @ExcelIgnore
    @ApiModelProperty("用户密码")
    private String userPwd;

    /**
     * 用户性别
     */
    @ExcelProperty(value = {"用户信息", "用户性别"}, index = 2)
    @ApiModelProperty("用户性别")
    private String userSex;

    /**
     * 联系电话
     */
    @ExcelProperty(value = {"用户信息", "联系电话"}, index = 3)
    @ApiModelProperty("联系电话")
    @Size(max = 20, message = "联系电话最大长度为20!")
    private String userPhone;


    /**
     * 邮箱地址
     */
    @ExcelProperty(value = {"用户信息", "邮箱地址"}, index = 4)
    @ApiModelProperty("邮箱地址")
    @Size(max = 50, message = "邮箱地址最大长度为50！")
    private String userEmail;

    /**
     * 用户排序
     */
    @ExcelProperty(value = {"用户信息", "用户排序"}, index = 5)
    @ApiModelProperty("用户排序")
    private Integer userSort;

    /**
     * 部门ID
     */
    @ExcelIgnore
    @ApiModelProperty("所属部门")
    @NotNull(message = "所属部门不能为空！")
    private String deptId;

    /**
     * 部门名称
     */
    @ExcelProperty(value = {"用户信息", "部门名称"}, index = 6)
    @ApiModelProperty(value = "部门名称", hidden = true)
    @TableField(exist = false)
    private String deptName;
    /**
     * 用户状态（0正常 1停用 )
     */
    @ExcelProperty(value = {"用户信息", "用户状态"}, index = 7)
    @ApiModelProperty("用户状态")
    @Pattern(regexp = "[01]", message = "用户状态必须为0或1（0正常 1停用）！")
    private String userStatus;

    /**
     * 用户头像
     */
    @ExcelIgnore
    @ApiModelProperty("用户头像")
    @Size(max = 50, message = "用户头像最大长度为50")
    private String userAvatar;

    /**
     * 备注
     */
    @ExcelIgnore
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注最大长度为150！")
    private String remark;


    /**
     * 用户已分配角色
     */
    @ExcelIgnore
    @ApiModelProperty("用户已分配角色")
    @TableField(exist = false)
    private List<String> roleIdList = new ArrayList<>();
    /**
     * 用户已分配岗位
     */
    @ExcelIgnore
    @ApiModelProperty("用户已分配岗位")
    @TableField(exist = false)
    private List<String> postIdList = new ArrayList<>();
}
