package com.jeeagile.logger.aspect;

import com.alibaba.fastjson.JSON;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileAgentUtil;
import com.jeeagile.core.util.system.util.AgileSystemUtil;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.core.enums.AgileCommonStatus;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.logger.entity.AgileLoggerLogin;
import com.jeeagile.logger.entity.AgileLoggerOperate;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
@Aspect
@Component
@Order
public class AgileLoggerAspect implements ApplicationListener<WebServerInitializedEvent> {
    @Autowired
    private AgileLoggerAsyncTask agileLoggerAsyncTask;

    private int serverPort;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.jeeagile.frame.annotation.AgileLogger)")
    public void agileAroundMethod() {
        // default implementation ignored
    }

    /**
     * 环绕基类为BaseController进行业务处理
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("agileAroundMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();//执行开始时间
        Throwable throwable = null;
        Object rtnObject = null;
        try {
            rtnObject = joinPoint.proceed();
            return rtnObject;
        } catch (Throwable ex) {
            throwable = ex;
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();//执行结束时间
            this.saveAgileLoggerOperate(joinPoint, throwable, rtnObject, endTime - beginTime);
        }
    }


    /**
     * 保存日志
     *
     * @param joinPoint
     * @param throwable
     * @param executeTime
     */
    private void saveAgileLoggerOperate(ProceedingJoinPoint joinPoint, Throwable throwable, Object rtnObject, long executeTime) {
        try {
//            AgileLogger agileLogger = joinPoint.getTarget().getClass().getAnnotation(AgileLogger.class);
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            AgileLogger agileLogger = method.getAnnotation(AgileLogger.class);
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (agileLogger != null && servletRequestAttributes != null) {
                HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
                if (agileLogger.type() == AgileLoggerType.LOGIN) {
                    AgileLoginUser agileLoginUser = null;
                    for (Object object : joinPoint.getArgs()) {
                        if (object instanceof AgileLoginUser) {
                            agileLoginUser = (AgileLoginUser) object;
                            break;
                        }
                    }
                    if (agileLoginUser == null) {
                        log.warn("用户登录接口参数不是《AgileLoggerLogin》，暂无法记录登录信息！");
                        return;
                    }
                    AgileLoggerLogin agileLoggerLogin = getAgileLoggerLogin(httpServletRequest, throwable);
                    agileLoggerLogin.setLoginName(agileLoginUser.getUserName());
                    agileLoggerLogin.setLoginModule(getModuleName(joinPoint, agileLogger));
                    agileLoggerAsyncTask.saveAgileLoggerLogin(agileLoggerLogin);
                } else {
                    AgileLoggerOperate agileLoggerOperate = getAgileLoggerOperate(httpServletRequest, throwable);
                    agileLoggerOperate.setOperateModule(getModuleName(joinPoint, agileLogger));
                    agileLoggerOperate.setOperateNotes(agileLogger.notes());
                    agileLoggerOperate.setOperateType(agileLogger.type().name());
                    agileLoggerOperate.setExecuteTime(executeTime);
                    String className = joinPoint.getTarget().getClass().getName();
                    String methodName = joinPoint.getSignature().getName();
                    agileLoggerOperate.setExecuteMethod(className + ":" + methodName);
                    if (this.isSaveParam(joinPoint, agileLogger)) {
                        this.putParam(joinPoint, rtnObject, agileLoggerOperate);
                    }
                    agileLoggerAsyncTask.saveAgileLoggerOperate(agileLoggerOperate);
                }
            }
        } catch (Exception ex) {
            log.warn("日志记录出现异常，异常信息：{}", ex.getMessage());
        }
    }

    /**
     * 获取模块名称
     *
     * @param joinPoint
     * @param agileLogger
     * @return
     */
    private String getModuleName(ProceedingJoinPoint joinPoint, AgileLogger agileLogger) {
        String moduleName = agileLogger.module();
        if (AgileStringUtil.isEmpty(moduleName)) {
            moduleName = agileLogger.value();
        }
        if (AgileStringUtil.isEmpty(moduleName)) {
            agileLogger = joinPoint.getTarget().getClass().getAnnotation(AgileLogger.class);
            moduleName = agileLogger.module();
            if (AgileStringUtil.isEmpty(moduleName)) {
                moduleName = agileLogger.value();
            }
        }
        if (AgileStringUtil.isEmpty(moduleName)) {
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            log.warn("{}:{}未配置操作日志模块！！！", className, methodName);
            moduleName = "未知操作模块";
        }
        return moduleName;
    }

    /**
     * 判断是否保存参数
     *
     * @return
     */
    private boolean isSaveParam(ProceedingJoinPoint joinPoint, AgileLogger agileLogger) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        AgileLogger agileLoggerClass = targetClass.getAnnotation(AgileLogger.class);
        if (agileLoggerClass != null) {
            return agileLoggerClass.param();
        } else {
            return agileLogger.param();
        }
    }

    /**
     * 将请求数据和返回数据转换为json串
     *
     * @param joinPoint
     * @param rtnObject
     * @param agileLoggerOperate
     */
    private void putParam(ProceedingJoinPoint joinPoint, Object rtnObject, AgileLoggerOperate agileLoggerOperate) {
        String requestParam = "";
        String responseParam = "";
        try {
            List<Object> paramObj = new ArrayList<>();
            for (Object object : joinPoint.getArgs()) {
                if (object instanceof HttpServletRequest || object instanceof MultipartFile) {
                    continue;
                }
                paramObj.add(object);
            }
            requestParam = JSON.toJSONString(paramObj.toArray(new Object[paramObj.size()]));
            if (rtnObject != null) {
                responseParam = JSON.toJSONString(rtnObject);
            }
        } catch (Exception ex) {
            log.warn("请求参数转换JSON出现错误：{}", ex.getMessage());
        }
        agileLoggerOperate.setRequestParam(requestParam);
        agileLoggerOperate.setResponseParam(responseParam);
    }

    private AgileLoggerLogin getAgileLoggerLogin(HttpServletRequest httpServletRequest, Throwable throwable) {
        AgileLoggerLogin agileLoggerLogin = new AgileLoggerLogin();
        agileLoggerLogin.setStatus(AgileCommonStatus.SUCCESS.getCode());
        agileLoggerLogin.setLoginTime(new Date());
        agileLoggerLogin.setLoginIp(AgileAgentUtil.getUserClientIp(httpServletRequest));
        String serverAddress = AgileSystemUtil.getHostInfo().getAddress() + ":" + serverPort;
        agileLoggerLogin.setServerAddress(serverAddress);

        UserAgent userAgent = AgileAgentUtil.getUserAgent(httpServletRequest);
        agileLoggerLogin.setLoginOs(userAgent.getOperatingSystem().getName());
        agileLoggerLogin.setLoginDevice(userAgent.getOperatingSystem().getDeviceType().getName());
        agileLoggerLogin.setLoginBrowser(userAgent.getBrowser().getName());
        if (throwable != null) {
            agileLoggerLogin.setStatus(AgileCommonStatus.FAIL.getCode());
            String excMessage = throwable.getMessage();
            if (excMessage.length() > 100) {
                agileLoggerLogin.setMessage(excMessage.substring(100));
            } else {
                agileLoggerLogin.setMessage(throwable.getMessage());
            }
        } else {
            agileLoggerLogin.setStatus(AgileCommonStatus.SUCCESS.getCode());
            agileLoggerLogin.setMessage("登录成功");
        }
        return agileLoggerLogin;
    }

    /**
     * 封装操作日志对象参数
     *
     * @param httpServletRequest
     * @param throwable
     * @return
     */
    private AgileLoggerOperate getAgileLoggerOperate(HttpServletRequest httpServletRequest, Throwable throwable) {
        AgileLoggerOperate agileLoggerOperate = new AgileLoggerOperate();
        AgileBaseUser userData = AgileSecurityContext.getCurrentUser();
        if (userData != null) {
            agileLoggerOperate.setOperateUser(userData.getUserName());
            agileLoggerOperate.setCreateUser(userData.getUserId());
            agileLoggerOperate.setUpdateUser(userData.getUserId());
        }
        agileLoggerOperate.setRequestUri(httpServletRequest.getRequestURI());
        agileLoggerOperate.setRequestMethod(httpServletRequest.getMethod());
        agileLoggerOperate.setOperateIp(AgileAgentUtil.getUserClientIp(httpServletRequest));
        String serverAddress = AgileSystemUtil.getHostInfo().getAddress() + ":" + this.serverPort;
        agileLoggerOperate.setServerAddress(serverAddress);

        UserAgent userAgent = AgileAgentUtil.getUserAgent(httpServletRequest);
        agileLoggerOperate.setOperateOs(userAgent.getOperatingSystem().getName());
        agileLoggerOperate.setOperateDevice(userAgent.getOperatingSystem().getDeviceType().getName());
        agileLoggerOperate.setOperateBrowser(userAgent.getBrowser().getName());
        if (throwable != null) {
            agileLoggerOperate.setStatus(AgileCommonStatus.FAIL.getCode());
            String excMessage = throwable.getMessage();
            if (excMessage.length() > 200) {
                agileLoggerOperate.setMessage(excMessage.substring(200));
            } else {
                agileLoggerOperate.setMessage(excMessage);
            }
        } else {
            agileLoggerOperate.setStatus(AgileCommonStatus.SUCCESS.getCode());
        }
        return agileLoggerOperate;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        this.serverPort = webServerInitializedEvent.getWebServer().getPort();
    }
}




