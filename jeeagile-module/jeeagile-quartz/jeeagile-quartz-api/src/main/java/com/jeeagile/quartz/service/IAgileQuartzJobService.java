package com.jeeagile.quartz.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.quartz.entity.AgileQuartzJob;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileQuartzJobService extends IAgileBaseService<AgileQuartzJob> {

    /**
     * 更新状态
     */
    boolean changeQuartzJobStatus(AgileQuartzJob agileQuartzJob);

    /**
     * 执行任务
     */
    boolean executeQuartzJob(String quartzJobId);

}
