package com.jeeagile.quartz.schedule;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileScheduleConstants {
    /**
     * 执行目标key
     */
    public static final String TASK_JOB_PROPERTIES = "TASK_JOB_PROPERTIES";

    /**
     * 默认
     */
    public static final String TASK_MISFIRE_DEFAULT = "0";

    /**
     * 立即触发执行
     */
    public static final String TASK_MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * 触发一次执行
     */
    public static final String TASK_MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * 不触发立即执行
     */
    public static final String TASK_MISFIRE_DO_NOTHING = "3";
}
