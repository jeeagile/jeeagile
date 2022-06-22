package com.jeeagile.frame.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public abstract class AgileBaseModel<T extends AgileBaseModel<T>> extends AgileModel<T> {
    @ExcelIgnore
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 创建人
     */
    @ExcelIgnore
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JSONField(serialize = false)
    private String createUser;

    /**
     * 创建时间
     */
    @ExcelIgnore
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @ExcelIgnore
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(serialize = false)
    private String updateUser;

    /**
     * 更新时间
     */
    @ExcelIgnore
    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE, whereStrategy = FieldStrategy.NEVER)
    private Date updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    /**
     * 设置当前创建人和创建时间
     */
    public void setCreatValue() {
        this.setCreateUser(AgileSecurityUtil.getUserId());
        this.setCreateTime(new Date());
    }

    /**
     * 设置当前更新人和更新时间
     */
    public void setUpdateValue() {
        this.setUpdateUser(AgileSecurityUtil.getUserId());
        this.setUpdateTime(new Date());
    }

    /**
     * 更新创建人和创建时间为null
     */
    public void setCreatNullValue() {
        this.setCreateUser(null);
        this.setCreateTime(null);
    }

    /**
     * 更新更新人和更新时间为null
     */
    public void setUpdateNullValue() {
        this.setUpdateUser(null);
        this.setUpdateTime(null);
    }
}