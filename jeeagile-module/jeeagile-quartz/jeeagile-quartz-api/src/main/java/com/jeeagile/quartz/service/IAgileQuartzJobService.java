package com.jeeagile.quartz.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.vo.AgileUpdateStatus;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileQuartzJobService extends IAgileBaseService<AgileQuartzJob> {
    /**
     * 分页查询任务列表
     */
    AgilePage<AgileQuartzJob> selectQuartzJobPage(AgilePageable<AgileQuartzJob> agilePageable);

    /**
     * 查看任务详细信息
     */
    AgileQuartzJob selectQuartzJobById(String quartzJobId);

    /**
     * 新增任务
     */
    AgileQuartzJob saveQuartzJob(AgileQuartzJob agileQuartzJob);

    /**
     * 更新任务
     */
    boolean updateQuartzJobById(AgileQuartzJob agileQuartzJob);

    /**
     * 删除任务
     */
    boolean deleteQuartzJobById(String quartzJobId);

    /**
     * 更新状态
     */
    boolean changeQuartzJobStatus(AgileUpdateStatus agileUpdateStatus);

    /**
     * 执行任务
     */
    boolean executeQuartzJobById(String quartzJobId);

}
