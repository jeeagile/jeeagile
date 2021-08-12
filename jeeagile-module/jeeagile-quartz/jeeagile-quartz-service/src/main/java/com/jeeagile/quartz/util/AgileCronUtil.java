package com.jeeagile.quartz.util;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileCronUtil {
    private AgileCronUtil() {
    }

    /**
     * 验证CRON表达式是否有效
     */
    public static boolean isValidCron(String cron) {
        return CronExpression.isValidExpression(cron);
    }

    /**
     * 通过给定的Cron表达式获取下一个执行时间
     *
     * @param cron Cron表达式
     * @return Date 下次Cron表达式执行时间
     */
    public static Date getNextTime(String cron) {
        try {
            CronExpression cronExpression = new CronExpression(cron);
            return cronExpression.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
