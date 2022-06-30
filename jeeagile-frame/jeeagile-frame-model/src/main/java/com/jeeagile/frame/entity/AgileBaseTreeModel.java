package com.jeeagile.frame.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
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
public abstract class AgileBaseTreeModel<T extends AgileBaseTreeModel<T>> extends AgileBaseModel<T> {
    /**
     * 父级ID
     */
    @ExcelIgnore
    private String parentId;

}
