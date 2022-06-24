package com.jeeagile.frame.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author JeeAgile
 * @date 2022-03-22
 * @description 租户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class AgileTenantTreeModel<T extends AgileTenantTreeModel<T>> extends AgileTenantModel<T> {
    /**
     * 父级ID
     */
    private String parentId;
}
