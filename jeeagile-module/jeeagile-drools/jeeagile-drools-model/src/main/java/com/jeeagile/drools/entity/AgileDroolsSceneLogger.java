package com.jeeagile.drools.entity;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 场景执行日志 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsSceneLogger extends AgileBaseModel<AgileDroolsSceneLogger> {
    /**
     * 场景ID
     */
    @NotNull(message = "场景ID不能为空！")
    private String sceneId;

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
     * 执行规则个数
     */
    private int ruleCount;

    /**
     * 执行参数
     */
    private String executeParam;

    /**
     * 执行结果
     */
    private String executeResult;

    /**
     * 执行状态（0：失败 1：成功）
     */
    @NotNull(message = "执行状态不能为空！")
    private String executeStatus;

    /**
     * 开发时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 执行时间(毫秒)
     */
    @NotNull(message = "执行时间(毫秒)不能为空！")
    private Long executeTime;

    /**
     * 执行错误异常信息
     */
    private String errorMsg;

}
