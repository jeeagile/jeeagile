package com.jeeagile.frame.entity.system;

import com.jeeagile.frame.entity.AgileBaseTenantModel;
import jakarta.validation.constraints.NotEmpty;
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
public class AgileSysRoleMenu extends AgileBaseTenantModel<AgileSysRoleMenu> {
    /**
     * 角色uuid
     */
    @NotEmpty(message = "角色id不能为空！")
    private String roleId;

    /**
     * 菜单uuid
     */
    @NotEmpty(message = "菜单id不能为空！")
    private String menuId;

}
