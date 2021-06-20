package com.jeeagile.system.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotNull;
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
public class AgileSysUserRole extends AgileBaseModel<AgileSysUserRole> {
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空！")
    private String userId;
    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空！")
    private String roleId;

}
