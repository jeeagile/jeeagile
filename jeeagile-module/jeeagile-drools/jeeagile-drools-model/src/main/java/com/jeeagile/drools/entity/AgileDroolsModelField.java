package com.jeeagile.drools.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

/**
 * @author JeeAgile
 * @date 2026-03-13 15:26:20
 * @description 规则引擎 数据对象字段 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsModelField extends AgileBaseModel<AgileDroolsModelField> {
    /**
     * 对象主键id
     */
    @NotNull(message = "对象主键id不能为空！")
    private String modelId;

    /**
     * 字段名称
     */
    @NotNull(message = "字段名称不能为空！")
    private String fieldName;

    /**
     * 字段标签
     */
    @NotNull(message = "字段标签不能为空！")
    private String fieldLabel;

    /**
     * 字段类型
     */
    @NotNull(message = "字段类型不能为空！")
    private String fieldType;

    /**
     * 对象主键id
     */
    private String objectId;

    /**
     * 字段描述
     */
    private String fieldDesc;

    /**
     * 字段排序
     */
    private Long fieldSort;

    /**
     * 是否列表标识（1：是 0：否）
     */
    private String listFlag;

    /**
     * 入参标识（1：是 0：否）
     */
    private String inputFlag;

}
