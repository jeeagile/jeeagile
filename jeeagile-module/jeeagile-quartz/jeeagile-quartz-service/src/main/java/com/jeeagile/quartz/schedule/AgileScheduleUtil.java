package com.jeeagile.quartz.schedule;

import com.jeeagile.core.enums.AgileStatusEnum;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.schedule.job.AgileConcurrentJob;
import com.jeeagile.quartz.schedule.job.AgileDisallowConcurrentJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
@Component
public class AgileScheduleUtil {

    private static Scheduler agileScheduler;

    private AgileScheduleUtil(Scheduler agileScheduler) {
        this.agileScheduler = agileScheduler;
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(AgileQuartzJob agileQuartzJob) {
        try {
            // 如果任务存在进行移除
            if (AgileScheduleUtil.checkExists(agileQuartzJob)) {
                AgileScheduleUtil.deleteJob(agileQuartzJob);
            }
            Class<? extends Job> jobClass = getAgileJobClass(agileQuartzJob);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(agileQuartzJob)).build();
            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(agileQuartzJob.getJobCron());
            cronScheduleBuilder = initCronScheduleMisfirePolicy(agileQuartzJob, cronScheduleBuilder);
            // 构建trigger
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(agileQuartzJob)).withSchedule(cronScheduleBuilder).build();
            // 放入运行参数
            jobDetail.getJobDataMap().put(AgileScheduleConstants.TASK_JOB_PROPERTIES, agileQuartzJob);
            agileScheduler.scheduleJob(jobDetail, cronTrigger);
            // 暂停任务
            if (agileQuartzJob.getJobStatus().equals(AgileStatusEnum.DISABLE.getCode())) {
                AgileScheduleUtil.pauseJob(agileQuartzJob);
            }
        } catch (SchedulerException ex) {
            throw new AgileFrameException("创建任务失败：" + ex.getMessage());
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileFrameException("创建任务发生未知异常：" + ex.getMessage());
        }

    }

    /**
     * 得到quartz任务类
     */
    private static Class<? extends Job> getAgileJobClass(AgileQuartzJob agileQuartzJob) {
        boolean isConcurrent = "0".equals(agileQuartzJob.getConcurrent());
        return isConcurrent ? AgileConcurrentJob.class : AgileDisallowConcurrentJob.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(AgileQuartzJob agileQuartzJob) {
        return TriggerKey.triggerKey(agileQuartzJob.getId(), agileQuartzJob.getJobGroup());
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(AgileQuartzJob agileQuartzJob) {
        return JobKey.jobKey(agileQuartzJob.getId(), agileQuartzJob.getJobGroup());
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder initCronScheduleMisfirePolicy(AgileQuartzJob agileQuartzJob, CronScheduleBuilder cronScheduleBuilder) {
        switch (agileQuartzJob.getInitMisfire()) {
            case AgileScheduleConstants.TASK_MISFIRE_DEFAULT:
                return cronScheduleBuilder;
            case AgileScheduleConstants.TASK_MISFIRE_IGNORE_MISFIRES:
                return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            case AgileScheduleConstants.TASK_MISFIRE_FIRE_AND_PROCEED:
                return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            case AgileScheduleConstants.TASK_MISFIRE_DO_NOTHING:
                return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            default:
                throw new AgileFrameException("任务初始化策略《" + agileQuartzJob.getInitMisfire() + "》不支持！");
        }
    }

    /**
     * 检测任务是否存在
     */
    public static boolean checkExists(AgileQuartzJob agileQuartzJob) {
        try {
            return agileScheduler.checkExists(getJobKey(agileQuartzJob));
        } catch (SchedulerException ex) {
            log.warn("检测任务是否存在发生异常！", ex);
        }
        return false;
    }

    /**
     * 删除任务
     */
    public static boolean deleteJob(AgileQuartzJob agileQuartzJob) {
        try {
            return agileScheduler.deleteJob(getJobKey(agileQuartzJob));
        } catch (SchedulerException ex) {
            log.warn("删除任务发生异常！", ex);
        }
        return false;
    }

    /**
     * 运行任务
     */
    public static void triggerJob(AgileQuartzJob agileQuartzJob) {
        try {
            agileScheduler.triggerJob(getJobKey(agileQuartzJob));
        } catch (SchedulerException ex) {
            throw new AgileFrameException("任务调度发生异常！", ex);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(AgileQuartzJob agileQuartzJob) {
        try {
            agileScheduler.pauseJob(getJobKey(agileQuartzJob));
        } catch (SchedulerException ex) {
            throw new AgileFrameException("暂停任务发生异常！", ex);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(AgileQuartzJob agileQuartzJob) {
        try {
            agileScheduler.resumeJob(getJobKey(agileQuartzJob));
        } catch (SchedulerException ex) {
            throw new AgileFrameException("恢复任务发生异常！", ex);
        }
    }

    /**
     * 清空任务
     */
    public static void clear() {
        try {
            agileScheduler.clear();
        } catch (SchedulerException ex) {
            throw new AgileFrameException("清空所有任务异常！", ex);
        }
    }

}
