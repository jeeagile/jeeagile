package com.jeeagile.core.util.spring;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description Spring工具类
 */
public class SpringServletUtil {
    private SpringServletUtil() {
    }

    /**
     * 获取当前HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest httpServletRequest = null;
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes != null) {
            httpServletRequest = servletRequestAttributes.getRequest();
        }
        return httpServletRequest;
    }

    /**
     * 获取当前HttpServletRequest
     *
     * @return
     */
    public static HttpServletResponse getHttpServletResponse() {
        HttpServletResponse httpServletResponse = null;
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes != null) {
            httpServletResponse = servletRequestAttributes.getResponse();
        }
        return httpServletResponse;
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        try {
            return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        } catch (Exception ex) {
            return null;
        }
    }
}
