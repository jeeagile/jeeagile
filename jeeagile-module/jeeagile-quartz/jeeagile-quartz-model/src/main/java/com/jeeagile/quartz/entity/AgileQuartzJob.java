package com.jeeagile.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeeagile.frame.entity.AgileBaseTenantModel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AgileQuartzJob extends AgileBaseTenantModel<AgileQuartzJob> {

    /**
     * 任务名称
     */
    @NotEmpty(message = "任务名称不能为空！")
    @Size(max = 50, message = "任务名称最大长度为50！")
    private String jobName;

    /**
     * 任务编码
     */
    @NotEmpty(message = "任务编码不能为空！")
    @Size(max = 30, message = "任务编码最大长度为30！")
    private String jobCode;

    /**
     * 任务分组
     */
    @NotEmpty(message = "任务分组不能为空！")
    @Size(max = 30, message = "任务分组最大长度为30！")
    private String jobGroup;

    /**
     * Bean名称
     */
    @NotEmpty(message = "Bean名称不能为空！")
    private String beanName;

    /**
     * 执行方法
     */
    @NotEmpty(message = "执行方法不能为空！")
    private String methodName;

    /**
     * 执行参数
     */
    private String methodParam;

    /**
     * cron执行表达式
     */
    @NotEmpty(message = "Cron执行表达式不能为空!")
    private String jobCron;

    /**
     * 菜单状态（0正常 1停用）
     */
    @NotEmpty(message = "任务状态不能为空!")
    private String jobStatus;

    /**
     * 初始策略（0:默认 1:立即触发执行 2:触发一次执行 3:不触发立即执行）
     */
    private String initMisfire;

    /**
     * 并发策略（0:允许 1:禁止）
     */
    private String concurrent;

    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注信息长度最大长度为150！")
    private String remark;


    /**
     * 下次执行时间
     */
    @TableField(exist = false)
    private Date nextTime;
}
