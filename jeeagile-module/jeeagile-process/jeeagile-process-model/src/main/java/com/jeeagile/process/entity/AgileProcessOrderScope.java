package com.jeeagile.process.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @description 工单权限关联表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileProcessOrderScope extends AgileBaseModel<AgileProcessOrderScope> {
    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID")
    private String orderId;

    /**
     * 权限类型(dept/role/post/user)
     */
    @ApiModelProperty(value = "权限类型(dept/role/post/user)")
    private String scopeType;

    /**
     * 权限ID(部门ID/角色ID/岗位ID/用户ID)
     */
    @ApiModelProperty(value = "权限ID")
    private String scopeId;
}
