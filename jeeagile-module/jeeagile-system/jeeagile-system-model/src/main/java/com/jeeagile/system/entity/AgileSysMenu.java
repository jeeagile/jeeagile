package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileTreeModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
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
public class AgileSysMenu extends AgileTreeModel<AgileSysMenu> {
    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    @NotEmpty(message = "菜单名称不能为空!")
    @Size(max = 50, message = "菜单名称最大长度为50!")
    private String menuName;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @NotEmpty(message = "显示顺序不能为空!")
    private String menuSort;

    /**
     * 组件地址
     */
    @ApiModelProperty("组件地址")
    @Size(max = 100, message = "组件地址最大长度为100!")
    private String menuComp;

    /**
     * 路由地址
     */
    @ApiModelProperty("路由地址")
    @Size(max = 100, message = "路由地址最大长度为100!")
    private String menuPath;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    @Size(max = 100, message = "菜单图标地址最大长度为100!")
    private String menuIcon;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @ApiModelProperty("菜单类型")
    @NotEmpty(message = "菜单类型不能为空!")
    private String menuType;

    /**
     * 菜单显示状态（0显示 1隐藏）
     */
    @ApiModelProperty("菜单显示状态")
    @NotEmpty(message = "菜单显示状态不能为空!")
    private String menuVisible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @ApiModelProperty("菜单状态")
    @NotEmpty(message = "菜单状态不能为空!")
    private String menuStatus;

    /**
     * 外链标识（0是 1否）
     */
    @ApiModelProperty("外链标识")
    @NotEmpty(message = "外链标识不能为空!")
    private String menuFrame = "0";

    /**
     * 权限标识
     */
    @ApiModelProperty("权限标识")
    @Size(max = 100, message = "菜单权限标识最大长度为100!")
    private String menuPerm;

    /**
     * 备注信息
     */
    @ApiModelProperty("备注")
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remark;
}
