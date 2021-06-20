package com.jeeagile.quartz.entity;

import com.jeeagile.frame.entity.AgileBaseModel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AgileQuartzJob extends AgileBaseModel<AgileQuartzJob> {
    /**
     * 任务名称
     */
    @NotNull(message = "任务名称不能为空！")
    @Size(max = 50, message = "任务名称最大值为50！")
    private String jobName;

    /**
     * 任务编码
     */
    @NotNull(message = "任务编码不能为空！")
    @Size(max = 30, message = "任务编码最大值为30！")
    private String jobCode;

    /**
     * Bean名称
     */
    @NotNull(message = "Bean名称不能为空！")
    private String beanName;

    /**
     * 执行方法
     */
    @NotNull(message = "执行方法不能为空！")
    private String methodName;

    /**
     * 执行参数
     */
    private String methodParam;

    /**
     * cron执行表达式
     */
    @NotNull(message = "Cron执行表达式不能为空!")
    private String jobCron;

    /**
     * 菜单状态（0正常 1停用）
     */
    @NotNull(message = "任务状态不能为空!")
    private String jobStatus;

    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注信息长度最大值为150！")
    private String remark;
}
