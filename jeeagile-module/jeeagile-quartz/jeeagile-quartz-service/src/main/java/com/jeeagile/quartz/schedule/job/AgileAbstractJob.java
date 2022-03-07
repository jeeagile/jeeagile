package com.jeeagile.quartz.schedule.job;

import com.alibaba.fastjson.JSONObject;
import com.jeeagile.core.enums.AgileCommonStatus;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.spring.AgileSpringUtil;
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.entity.AgileQuartzJobLogger;
import com.jeeagile.quartz.schedule.AgileScheduleConstants;
import com.jeeagile.quartz.schedule.AgileScheduleUtil;
import com.jeeagile.quartz.service.IAgileQuartzJobLoggerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
public abstract class AgileAbstractJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        AgileQuartzJob agileQuartzJob = (AgileQuartzJob) jobDataMap.get(AgileScheduleConstants.TASK_JOB_PROPERTIES);
        Throwable throwable = null;
        Date startTime = new Date();
        try {
            if (agileQuartzJob != null && agileQuartzJob.isNotEmptyPk()) {
                doExecute(jobExecutionContext, agileQuartzJob);
            } else {
                throw new AgileFrameException("任务调度参数为空！");
            }
        } catch (Exception ex) {
            log.error("任务执行异常：", ex);
            throwable = ex;
        } finally {
            saveJobLog(agileQuartzJob, startTime, throwable);
        }
    }

    /**
     * 记录任务执行日志
     */
    protected void saveJobLog(AgileQuartzJob agileQuartzJob, Date startTime, Throwable throwable) {
        final AgileQuartzJobLogger agileQuartzJobLogger = new AgileQuartzJobLogger();
        BeanUtils.copyProperties(agileQuartzJob, agileQuartzJobLogger);
        agileQuartzJobLogger.setId(null);
        agileQuartzJobLogger.setStartTime(startTime);
        agileQuartzJobLogger.setStopTime(new Date());
        long executeTime = agileQuartzJobLogger.getStopTime().getTime() - agileQuartzJobLogger.getStartTime().getTime();
        agileQuartzJobLogger.setExecuteTime(executeTime);
        if (throwable != null) {
            agileQuartzJobLogger.setStatus(AgileCommonStatus.FAIL.getCode());
            String errorMsg = StringUtils.substring(throwable.getMessage(), 0, 2000);
            agileQuartzJobLogger.setMessage(errorMsg);
        } else {
            agileQuartzJobLogger.setStatus(AgileCommonStatus.SUCCESS.getCode());
        }
        AgileSpringUtil.getBean(IAgileQuartzJobLoggerService.class).save(agileQuartzJobLogger);
    }

    protected void doExecute(JobExecutionContext context, AgileQuartzJob agileQuartzJob) throws Exception {
        this.invokeMethod(agileQuartzJob);
    }

    /**
     * 执行方法
     */
    protected void invokeMethod(AgileQuartzJob agileQuartzJob) throws Exception {
        Object bean = null;
        if (!agileQuartzJob.getBeanName().contains(".")) {
            bean = AgileSpringUtil.getBean(agileQuartzJob.getBeanName());
        } else {
            bean = Class.forName(agileQuartzJob.getBeanName()).newInstance();
        }
        if (bean == null) {
            //方法不存在暂停执行任务
            AgileScheduleUtil.pauseJob(agileQuartzJob);
            throw new AgileFrameException("任务代码《" + agileQuartzJob.getJobCode() + "》执行Bean不存在，请核实！");
        }
        invokeMethod(bean, agileQuartzJob);
    }

    /**
     * 调用任务方法
     *
     * @param bean           目标对象
     * @param agileQuartzJob 任务调度对象
     */
    private void invokeMethod(Object bean, AgileQuartzJob agileQuartzJob) throws SecurityException, IllegalArgumentException {
        List<Method> methodList = filterMethod(bean, agileQuartzJob.getMethodName());
        //判断方法是否存在，如果不存在则暂停执行任务
        if (methodList == null || methodList.isEmpty()) {
            AgileScheduleUtil.pauseJob(agileQuartzJob);
            throw new AgileFrameException("任务代码《" + agileQuartzJob.getJobCode() + "》执行方法不存在，请核实！");
        }
        //获取方法个数，用于对比执行的是那个方法
        int methodParamCount = 0;
        if (AgileStringUtil.isNotEmpty(agileQuartzJob.getMethodParam())) {
            methodParamCount = agileQuartzJob.getMethodParam().split("&").length;
        }
        //对比参数个数确定要执行的方法
        Method invokeMethod = null;
        for (Method method : methodList) {
            if (method.getParameterCount() == methodParamCount) {
                invokeMethod = method;
            }
        }
        if (invokeMethod == null) {
            log.warn("设定参数与要执行方法的参数个数不符，将随机获取一个方法进行执行！");
            invokeMethod = methodList.get(0);
        }
        if (invokeMethod.getParameterCount() == 0) {
            if (AgileStringUtil.isNotEmpty(agileQuartzJob.getMethodParam())) {
                log.warn("任务要执行方法属于无参方法，但任务设定了参数，请核实！");
            }
            ReflectionUtils.invokeMethod(invokeMethod, bean);
        } else {
            Object[] methodParams = getMethodParams(invokeMethod, agileQuartzJob.getMethodParam());
            ReflectionUtils.invokeMethod(invokeMethod, bean, methodParams);
        }
    }

    /**
     * 获取执行方法参数值
     */
    public Object[] getMethodParams(Method invokeMethod, String methodParamStr) {
        String[] methodParams = methodParamStr.split("&");
        Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
        if (methodParams.length != parameterTypes.length) {
            log.warn("任务要执行方法参数个数与任务参数设置个数不一致！");
        }
        Object[] methodParameterObject = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > methodParams.length) {
                methodParameterObject[i] = null;
            }
            Class<?> parameterType = parameterTypes[i];
            //如果是基础类型直接转换，否则按照json转换
            if (isBasicDataTypes(parameterType)) {
                methodParameterObject[i] = parseBasicTypeWrapper(methodParams[i], parameterType);
            } else {
                methodParameterObject[i] = JSONObject.parseObject(methodParams[i], parameterType);
            }
        }
        return methodParameterObject;
    }

    /**
     * 过滤执行方法，主要解决存在同方法名问题
     *
     * @param bean
     * @param methodName
     * @return
     */
    private List<Method> filterMethod(Object bean, String methodName) {
        List<Method> methodList = new ArrayList<>();
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                methodList.add(method);
            }
        }
        return methodList;
    }

    /**
     * 判断是否为基本数据类型包装类
     */
    private boolean isBasicDataTypes(Class<?> clazz) {
        Set<Class<?>> classSet = new HashSet<>();
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }

    /**
     * 基本类型包装类解析
     */
    private Object parseBasicTypeWrapper(String value, Class<?> parameterType) {
        try {
            if (Number.class.isAssignableFrom(parameterType)) {
                if (parameterType == Integer.class) {
                    return Integer.parseInt(value);
                } else if (parameterType == Short.class) {
                    return Short.parseShort(value);
                } else if (parameterType == Long.class) {
                    return Long.parseLong(value);
                } else if (parameterType == Float.class) {
                    return Float.parseFloat(value);
                } else if (parameterType == Double.class) {
                    return Double.parseDouble(value);
                } else if (parameterType == Byte.class) {
                    return value.getBytes();
                }
            } else if (parameterType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (parameterType == Character.class) {
                return value.charAt(0);
            }
        } catch (Exception ex) {
            log.error("基础数据类型转换失败", ex);
            throw new AgileFrameException("基础数据类型转换失败，失败信息：" + ex.getMessage());
        }
        return null;
    }
}
