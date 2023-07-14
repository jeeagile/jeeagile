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
public class AgileSysRole extends AgileBaseTenantModel<AgileSysRole> {
    /**
     * 角色编码
     */
    @ExcelProperty(value = {"角色信息", "角色编码"}, index = 0)
    @ApiModelProperty("角色编码")
    @NotEmpty(message = "角色编码不能为空！")
    @Size(max = 30, message = "角色编码最大长度为30！")
    private String roleCode;

    /**
     * 角色名称
     */
    @ExcelProperty(value = {"角色信息", "角色名称"}, index = 1)
    @ApiModelProperty("角色名称")
    @NotEmpty(message = "角色名称不能为空！")
    @Size(max = 50, message = "角色名称最大长度为50！")
    private String roleName;

    /**
     * 角色状态（0:正常 1:停用）
     */
    @ExcelProperty(value = {"角色信息", "角色状态"}, index = 2)
    @ApiModelProperty("角色状态")
    @Pattern(regexp = "[01]", message = "角色状态值必须为0或1（0：正常 1：停用）！")
    private String roleStatus;

    /**
     * 角色排序
     */
    @ExcelProperty(value = {"角色信息", "角色排序"}, index = 3)
    @ApiModelProperty("角色排序")
    @NotNull(message = "角色排序不能为空！")
    private Integer roleSort;

    /**
     * 数据范围
     */
    @ExcelProperty(value = {"角色信息", "数据范围"}, index = 4)
    @ApiModelProperty("数据范围")
    private String dataScope;

    /**
     * 备注
     */
    @ExcelIgnore
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注最大长度为150！")
    private String remark;

    /**
     * 角色已分配菜单
     */
    @ExcelIgnore
    @ApiModelProperty("已分配菜单")
    @TableField(exist = false)
    private List<String> menuIdList = new ArrayList<>();

    /**
     * 角色已分配部门权限
     */
    @ExcelIgnore
    @ApiModelProperty("已分配部门权限")
    @TableField(exist = false)
    private List<String> deptIdList = new ArrayList<>();
}
