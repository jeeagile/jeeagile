package com.jeeagile.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jeeagile.frame.entity.AgileModel;
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
public class AgileQuartzJobLogger extends AgileModel<AgileQuartzJobLogger> {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务编码
     */
    private String jobCode;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * Bean名称
     */
    private String beanName;

    /**
     * 执行方法
     */
    private String methodName;

    /**
     * 执行参数
     */
    private String methodParam;

    /**
     * cron执行表达式
     */
    private String jobCron;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date stopTime;

    /**
     * 执行时间 (毫秒)
     */
    private Long executeTime;

    /**
     * 执行状态（0：成功 1：失败）
     */
    private String status;

    /**
     * 异常信息
     */
    private String message;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
