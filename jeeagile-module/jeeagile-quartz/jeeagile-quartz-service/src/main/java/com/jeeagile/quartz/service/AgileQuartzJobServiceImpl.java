package com.jeeagile.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.mapper.AgileQuartzJobMapper;
import com.jeeagile.quartz.vo.AgileUpdateStatus;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileQuartzJobServiceImpl extends AgileBaseServiceImpl<AgileQuartzJobMapper, AgileQuartzJob> implements IAgileQuartzJobService {

    @Override
    public AgilePage<AgileQuartzJob> selectQuartzJobPage(AgilePageable<AgileQuartzJob> agilePageable) {
        return this.page(agilePageable, getQuartzJobQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public AgileQuartzJob selectQuartzJobById(String quartzJobId) {
        return this.getById(quartzJobId);
    }

    @Override
    public AgileQuartzJob saveQuartzJob(AgileQuartzJob agileQuartzJob) {
        //校验业务数据
        agileQuartzJob.validate();
        this.save(agileQuartzJob);
        return agileQuartzJob;
    }

    @Override
    public boolean updateQuartzJobById(AgileQuartzJob agileQuartzJob) {
        //校验业务数据
        agileQuartzJob.validate();
        return this.updateById(agileQuartzJob);
    }

    @Override
    public boolean deleteQuartzJobById(String quartzJobId) {
        return this.removeById(quartzJobId);
    }

    @Override
    public boolean changeQuartzJobStatus(AgileUpdateStatus agileUpdateStatus) {
        AgileQuartzJob agileQuartzJob = new AgileQuartzJob();
        agileQuartzJob.setId(agileUpdateStatus.getId());
        agileQuartzJob.setJobCode(agileUpdateStatus.getStatus());
        return this.updateById(agileQuartzJob);
    }

    @Override
    public boolean executeQuartzJobById(String quartzJobId) {
        return false;
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileQuartzJob> getQuartzJobQueryWrapper(AgileQuartzJob agileQuartzJob) {
        QueryWrapper<AgileQuartzJob> queryWrapper = new QueryWrapper<>();
        if (agileQuartzJob != null) {
            if (StringUtil.isNotEmpty(agileQuartzJob.getJobCode())) {
                queryWrapper.lambda().eq(AgileQuartzJob::getJobCode, agileQuartzJob.getJobCode());
            }
            if (StringUtil.isNotEmpty(agileQuartzJob.getJobName())) {
                queryWrapper.lambda().like(AgileQuartzJob::getJobName, agileQuartzJob.getJobName());
            }
            if (StringUtil.isNotEmpty(agileQuartzJob.getJobStatus())) {
                queryWrapper.lambda().eq(AgileQuartzJob::getJobStatus, agileQuartzJob.getJobStatus());
            }
        }
        return queryWrapper;
    }
}
