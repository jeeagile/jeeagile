package com.jeeagile.frame.entity;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import io.swagger.annotations.ApiModelProperty;
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
public abstract class AgileModel<T extends AgileModel<T>> extends Model<T> {
    @ExcelIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String excelName;

    /**
     * 判断主键是否为空
     *
     * @return
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    public boolean isNotEmptyPk() {
        return AgileStringUtil.isNotEmpty(this.pkVal());
    }

    /**
     * 判断主键是否为空
     *
     * @return
     */
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    public boolean isEmptyPk() {
        return AgileStringUtil.isEmpty(this.pkVal());
    }

    /**
     * 校验数据
     *
     * @param group
     */
    public void validate(Class... group) {
        AgileValidateUtil.validateObject(this, group);
    }
}
