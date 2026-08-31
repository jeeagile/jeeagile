package com.jeeagile.drools.entity;

import java.util.Date;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.jeeagile.frame.entity.AgileBaseModel;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:35
 * @description 规则引擎 场景规则执行日志 实体对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileDroolsRuleLogger extends AgileBaseModel<AgileDroolsRuleLogger> {
    /**
     * 执行日志ID
     */
    @NotNull(message = "执行日志ID不能为空！")
    private String loggerId;

    /**
     * 场景名称
     */
    @NotNull(message = "场景名称不能为空！")
    private String ruleName;

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

}
