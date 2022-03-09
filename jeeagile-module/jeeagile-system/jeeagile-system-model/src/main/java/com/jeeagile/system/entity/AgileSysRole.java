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
public class AgileSysRole extends AgileBaseModel<AgileSysRole> {
    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码", position = 1)
    @NotEmpty(message = "角色编码不能为空！")
    @Size(max = 30, message = "角色编码长度最大值为30！")
    private String roleCode;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称", position = 2)
    @NotEmpty(message = "角色名称不能为空！")
    @Size(max = 50, message = "角色名称长度最大值为50！")
    private String roleName;

    /**
     * 角色状态（0:正常 1:停用）
     */
    @ApiModelProperty(value = "角色状态", position = 3)
    @Pattern(regexp = "[01]", message = "角色状态值必须为0或1（0：正常 1：停用）！")
    private String roleStatus;

    /**
     * 角色排序
     */
    @ApiModelProperty(value = "角色排序", position = 4)
    @NotNull(message = "角色排序不能为空！")
    private Integer roleSort;

    /**
     * 数据范围
     */
    @ApiModelProperty(value = "数据范围", position = 5)
    private String dataScope;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", position = 6)
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;

    /**
     * 角色已分配菜单
     */
    @ApiModelProperty(value = "已分配菜单", position = 7)
    @TableField(exist = false)
    private List<String> menuIdList = new ArrayList<>();

    /**
     * 角色已分配部门权限
     */
    @ApiModelProperty(value = "已分配部门权限", position = 8)
    @TableField(exist = false)
    private List<String> deptIdList = new ArrayList<>();
}
