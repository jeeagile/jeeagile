package com.jeeagile.drools.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 规则场景 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("agile_drools_scene")
public class AgileDroolsScene extends AgileBaseModel<AgileDroolsScene> {
    /**
     * 场景编码
     */
    @NotNull(message = "场景编码不能为空！")
    private String sceneCode;

    /**
     * 场景名称
     */
    @NotNull(message = "场景名称不能为空！")
    private String sceneName;

    /**
     * 场景状态
     */
    private String sceneStatus;

    /**
     * 场景描述
     */
    private String sceneDesc;

    /**
     * 分配岗位列表
     */
    @TableField(exist = false)
    private List<String> ruleIdList = new ArrayList<>();
}
