package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileTreeModel;
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
public class AgileSysDept extends AgileTreeModel<AgileSysDept> {
    /**
     * 部门编码
     */
    @NotEmpty(message = "部门编码不能为空！")
    @Size(max = 20, message = "部门编码最大长度为20！")
    private String deptCode;
    /**
     * 部门名称
     */
    @NotEmpty(message = "部门名称不能为空！")
    @Size(max = 50, message = "部门名称最大长度为50！")
    private String deptName;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空！")
    private Integer deptSort;

    /**
     * 使用状态（0:正常 1:停用）
     */
    @Pattern(regexp = "[01]", message = "部门状态必须为0或1（0:正常 1:停用）！")
    private String deptStatus = "0";
}
