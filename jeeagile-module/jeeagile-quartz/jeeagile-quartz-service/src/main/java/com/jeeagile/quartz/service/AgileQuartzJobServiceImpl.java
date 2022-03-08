package com.jeeagile.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.enums.AgileStatusEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.mapper.AgileQuartzJobMapper;
import com.jeeagile.quartz.schedule.AgileScheduleUtil;
import com.jeeagile.quartz.util.AgileCronUtil;
import com.jeeagile.quartz.vo.AgileQuartzJobInfo;
import com.jeeagile.quartz.vo.AgileUpdateStatus;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileQuartzJobServiceImpl extends AgileBaseServiceImpl<AgileQuartzJobMapper, AgileQuartzJob> implements IAgileQuartzJobService {

    @Override
    public LambdaQueryWrapper<AgileQuartzJob> queryWrapper(AgileQuartzJob agileQuartzJob) {
        LambdaQueryWrapper<AgileQuartzJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileQuartzJob != null) {
            if (AgileStringUtil.isNotEmpty(agileQuartzJob.getJobCode())) {
                lambdaQueryWrapper.eq(AgileQuartzJob::getJobCode, agileQuartzJob.getJobCode());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzJob.getJobName())) {
                lambdaQueryWrapper.like(AgileQuartzJob::getJobName, agileQuartzJob.getJobName());
            }
            if (AgileStringUtil.isNotEmpty(agileQuartzJob.getJobStatus())) {
                lambdaQueryWrapper.eq(AgileQuartzJob::getJobStatus, agileQuartzJob.getJobStatus());
            }
        }
        lambdaQueryWrapper.orderByAsc(AgileQuartzJob::getJobCode);
        return lambdaQueryWrapper;
    }

    @Override
    public AgileQuartzJob selectModel(Serializable id) {
        AgileQuartzJob agileQuartzJob = this.getById(id);
        AgileQuartzJobInfo agileQuartzJobInfo = new AgileQuartzJobInfo();
        if (agileQuartzJob != null && agileQuartzJob.isNotEmptyPk()) {
            BeanUtils.copyProperties(agileQuartzJob, agileQuartzJobInfo);
            agileQuartzJobInfo.setNextTime(AgileCronUtil.getNextTime(agileQuartzJob.getJobCron()));
        }
        return agileQuartzJobInfo;
    }

    @Override
    public AgileQuartzJob saveModel(AgileQuartzJob agileQuartzJob) {
        this.validateData(agileQuartzJob);
        this.save(agileQuartzJob);
        AgileScheduleUtil.createScheduleJob(agileQuartzJob);
        return agileQuartzJob;
    }

    @Override
    public boolean updateModel(AgileQuartzJob agileQuartzJob) {
        this.validateData(agileQuartzJob);
        if (this.updateById(agileQuartzJob)) {
            AgileScheduleUtil.createScheduleJob(agileQuartzJob);
            return true;
        }
        return false;
    }

    /**
     * 校验业务数据
     *
     * @param agileQuartzJob
     */
    private void validateData(AgileQuartzJob agileQuartzJob) {
        if (!AgileCronUtil.isValidCron(agileQuartzJob.getJobCron())) {
            throw new AgileValidateException("Cron表达式格式错误，请核实！");
        }
        LambdaQueryWrapper<AgileQuartzJob> queryWrapper = new LambdaQueryWrapper<>();
        if (agileQuartzJob.getId() != null) {
            queryWrapper.ne(AgileQuartzJob::getId, agileQuartzJob.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileQuartzJob::getJobCode, agileQuartzJob.getJobCode()).or().eq(AgileQuartzJob::getJobName, agileQuartzJob.getJobName())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("任务编码或任务名称已存在！");
        }
    }

    @Override
    public boolean deleteModel(Serializable id) {
        AgileQuartzJob agileQuartzJob = this.getById(id);
        if (agileQuartzJob != null && this.removeById(agileQuartzJob.getId())) {
            AgileScheduleUtil.deleteJob(agileQuartzJob);
        }
        return true;
    }

    @Override
    public boolean changeQuartzJobStatus(AgileUpdateStatus agileUpdateStatus) {
        AgileQuartzJob agileQuartzJob = this.getById(agileUpdateStatus.getId());
        agileQuartzJob.setJobStatus(agileUpdateStatus.getStatus());
        boolean updateFlag = this.updateById(agileQuartzJob);
        if (updateFlag) {
            if (AgileStatusEnum.NORMAL.getCode().equals(agileUpdateStatus.getStatus())) {
                AgileScheduleUtil.resumeJob(agileQuartzJob);
            } else {
                AgileScheduleUtil.pauseJob(agileQuartzJob);
            }
        }
        return updateFlag;
    }

    @Override
    public boolean executeQuartzJobById(String quartzJobId) {
        AgileQuartzJob agileQuartzJob = this.getById(quartzJobId);
        if (agileQuartzJob != null && agileQuartzJob.isNotEmptyPk()) {
            AgileScheduleUtil.triggerJob(agileQuartzJob);
        }
        return true;
    }
}
