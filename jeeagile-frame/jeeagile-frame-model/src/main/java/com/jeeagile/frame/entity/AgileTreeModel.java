package com.jeeagile.frame.entity;

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
public abstract class AgileTreeModel<T extends AgileTreeModel<T>> extends AgileBaseModel<T> {
    /**
     * 父级ID
     */
    private String parentId;

}
