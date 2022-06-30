package com.jeeagile.frame.aspect;

import com.alibaba.fastjson.JSON;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.util.AgileAgentUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.system.util.AgileSystemUtil;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.AgileSysLogger;
import com.jeeagile.frame.entity.system.AgileSysLogin;
import com.jeeagile.frame.enums.AgileLoggerType;
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
import java.util.Arrays;
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

    /**
     * 应用服务端口
     */
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
            this.saveAgileSysLogger(joinPoint, throwable, rtnObject, endTime - beginTime);
        }
    }


    /**
     * 保存日志
     *
     * @param joinPoint
     * @param throwable
     * @param executeTime
     */
    private void saveAgileSysLogger(ProceedingJoinPoint joinPoint, Throwable throwable, Object rtnObject, long executeTime) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            if (joinPoint.getTarget() instanceof AgileCrudController) {
                AgileLogger agileLogger = joinPoint.getTarget().getClass().getAnnotation(AgileLogger.class);
                if (agileLogger == null || !agileLogger.recordFlag()) {
                    return;
                }
                if (agileLogger.methodName() != null && agileLogger.methodName().length > 0
                        && !Arrays.toString(agileLogger.methodName()).contains(method.getName())) {
                    return;
                }
            }
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
                        log.warn("用户登录接口参数不是《AgileSysLogin》，暂无法记录登录信息！");
                        return;
                    }
                    AgileSysLogin agileSysLogin = getAgileSysLogin(httpServletRequest, throwable);
                    agileSysLogin.setLoginName(agileLoginUser.getUserName());
                    agileSysLogin.setLoginModule(getModuleName(joinPoint, agileLogger));
                    //登录失败情况下无法记录租户ID 此处需进行存放
                    String tenantId = agileLoginUser.getTenantId();
                    if (AgileStringUtil.isEmpty(tenantId)) {
                        tenantId = "非法租户";
                    }
                    AgileSecurityContext.putTenantId(tenantId);
                    agileLoggerAsyncTask.saveAgileSysLogin(agileSysLogin);
                    AgileSecurityContext.removeTenant();
                } else {
                    AgileSysLogger agileSysLogger = getAgileSysLogger(httpServletRequest, throwable);
                    agileSysLogger.setOperateModule(getModuleName(joinPoint, agileLogger));
                    agileSysLogger.setOperateNotes(agileLogger.notes());
                    agileSysLogger.setOperateType(agileLogger.type().name());
                    agileSysLogger.setExecuteTime(executeTime);
                    String className = joinPoint.getTarget().getClass().getName();
                    String methodName = joinPoint.getSignature().getName();
                    agileSysLogger.setExecuteMethod(className + ":" + methodName);
                    if (this.isSaveParam(joinPoint, agileLogger)) {
                        this.putParam(joinPoint, rtnObject, agileSysLogger);
                    }
//                    //todo 此处需要考虑异步线程下是否存在问题
//                    AgileSecurityContext.putTenantId(AgileSecurityContext.getUserData().getTenantId());
                    agileLoggerAsyncTask.saveAgileSysLogger(agileSysLogger);
//                    AgileSecurityContext.removeTenant();
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
     * @param agileSysLogger
     */
    private void putParam(ProceedingJoinPoint joinPoint, Object rtnObject, AgileSysLogger agileSysLogger) {
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
        agileSysLogger.setRequestParam(requestParam);
        agileSysLogger.setResponseParam(responseParam);
    }

    private AgileSysLogin getAgileSysLogin(HttpServletRequest httpServletRequest, Throwable throwable) {
        AgileSysLogin agileSysLogin = new AgileSysLogin();
        agileSysLogin.setStatus(AgileConstants.SUCCESS);
        agileSysLogin.setLoginTime(new Date());
        agileSysLogin.setLoginIp(AgileAgentUtil.getUserClientIp(httpServletRequest));
        String serverAddress = AgileSystemUtil.getHostInfo().getAddress() + ":" + serverPort;
        agileSysLogin.setServerAddress(serverAddress);

        UserAgent userAgent = AgileAgentUtil.getUserAgent(httpServletRequest);
        agileSysLogin.setLoginOs(userAgent.getOperatingSystem().getName());
        agileSysLogin.setLoginDevice(userAgent.getOperatingSystem().getDeviceType().getName());
        agileSysLogin.setLoginBrowser(userAgent.getBrowser().getName());
        if (throwable != null) {
            agileSysLogin.setStatus(AgileConstants.FAIL);
            String excMessage = throwable.getMessage();
            if (excMessage.length() > 100) {
                agileSysLogin.setMessage(excMessage.substring(100));
            } else {
                agileSysLogin.setMessage(throwable.getMessage());
            }
        } else {
            agileSysLogin.setStatus(AgileConstants.SUCCESS);
            agileSysLogin.setMessage("登录成功");
        }
        return agileSysLogin;
    }

    /**
     * 封装操作日志对象参数
     *
     * @param httpServletRequest
     * @param throwable
     * @return
     */
    private AgileSysLogger getAgileSysLogger(HttpServletRequest httpServletRequest, Throwable throwable) {
        AgileSysLogger agileSysLogger = new AgileSysLogger();
        AgileBaseUser userData = AgileSecurityContext.getUserData();
        if (userData != null) {
            agileSysLogger.setOperateUser(userData.getUserName());
            agileSysLogger.setCreateUser(userData.getUserId());
            agileSysLogger.setUpdateUser(userData.getUserId());
        }
        agileSysLogger.setRequestUri(httpServletRequest.getRequestURI());
        agileSysLogger.setRequestMethod(httpServletRequest.getMethod());
        agileSysLogger.setOperateIp(AgileAgentUtil.getUserClientIp(httpServletRequest));
        String serverAddress = AgileSystemUtil.getHostInfo().getAddress() + ":" + this.serverPort;
        agileSysLogger.setServerAddress(serverAddress);

        UserAgent userAgent = AgileAgentUtil.getUserAgent(httpServletRequest);
        agileSysLogger.setOperateOs(userAgent.getOperatingSystem().getName());
        agileSysLogger.setOperateDevice(userAgent.getOperatingSystem().getDeviceType().getName());
        agileSysLogger.setOperateBrowser(userAgent.getBrowser().getName());
        if (throwable != null) {
            agileSysLogger.setStatus(AgileConstants.FAIL);
            String excMessage = throwable.getMessage();
            if (excMessage.length() > 200) {
                agileSysLogger.setMessage(excMessage.substring(200));
            } else {
                agileSysLogger.setMessage(excMessage);
            }
        } else {
            agileSysLogger.setStatus(AgileConstants.SUCCESS);
        }
        return agileSysLogger;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        this.serverPort = webServerInitializedEvent.getWebServer().getPort();
    }
}




