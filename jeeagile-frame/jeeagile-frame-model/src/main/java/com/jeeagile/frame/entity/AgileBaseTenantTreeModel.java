package com.jeeagile.frame.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
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
public abstract class AgileBaseTenantTreeModel<T extends AgileBaseTenantTreeModel<T>> extends AgileBaseTreeModel<T> {
    /**
     * 租户ID
     */
    @ExcelIgnore
    @TableField(exist = false)
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String tenantId;
}
