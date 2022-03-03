package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotEmpty;
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
public class AgileSysRole extends AgileBaseModel<AgileSysRole> {
    /**
     * 角色编码
     */
    @NotEmpty(message = "角色编码不能为空！")
    @Size(max = 30, message = "角色编码长度最大值为30！")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空！")
    @Size(max = 50, message = "角色名称长度最大值为50！")
    private String roleName;

    /**
     * 角色状态（0:正常 1:停用）
     */
    @Pattern(regexp = "[01]", message = "角色状态值必须为0或1（0：正常 1：停用）！")
    private String roleStatus;

    /**
     * 角色排序
     */
    @NotEmpty(message = "角色排序不能为空！")
    private Integer roleSort;

    /**
     * 数据范围
     */
    private String dataScope;

    /**
     * 备注
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;

}
