package com.jeeagile.drools.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

/**
 * @author JeeAgile
 * @date 2026-03-12 10:50:27
 * @description 规则引擎 数据对象 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsModel extends AgileBaseModel<AgileDroolsModel> {
    /**
     * 对象名称
     */
    @NotNull(message = "对象名称不能为空！")
    private String modelName;

    /**
     * 对象标签
     */
    @NotNull(message = "对象标签不能为空！")
    private String modelLabel;

    /**
     * 对象类型（java、declare）
     */
    @NotNull(message = "对象类型不能为空！")
    private String modelType;

    /**
     * 对象包名
     */
    @NotNull(message = "对象包名不能为空！")
    private String modelPackage;

    /**
     * 父级对象
     */
    private String superModel;

    /**
     * 对象状态
     */
    private String modelStatus;

    /**
     * 入参标识（1：是 0：否）
     */
    private String inputFlag;

    /**
     * 出参标识（1：是 0：否）
     */
    private String outputFlag;

    /**
     * 对象描述
     */
    private String modelDesc;

}
