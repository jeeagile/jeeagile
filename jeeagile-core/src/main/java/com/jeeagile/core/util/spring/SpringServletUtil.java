package com.jeeagile.core.util.spring;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.util.StringUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
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

    /**
     * 根据上下文获取ServletRequestAttributes
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        try {
            return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取用户TOKEN
     */
    public static String getUserToken() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            return getUserToken(httpServletRequest);
        } else {
            return null;
        }
    }

    /**
     * 获取用户TOKEN
     */
    public static String getUserToken(HttpServletRequest httpServletRequest) {
        String userToken = httpServletRequest.getHeader(AgileConstants.AGILE_TOKEN);
        if (StringUtil.isEmpty(userToken)) {
            userToken = httpServletRequest.getParameter(AgileConstants.AGILE_TOKEN);
        }
        if (StringUtil.isEmpty(userToken)) {
            userToken = getCookieValue(httpServletRequest, AgileConstants.AGILE_TOKEN);
        }
        return userToken;
    }

    /**
     * 根据COOKIE名称获取对应得COOKIE值
     */
    public static String getSpringCookieValue(String cookieName) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            return getCookieValue(httpServletRequest, cookieName);
        } else {
            return null;
        }
    }

    /**
     * 根据COOKIE名称获取对应得COOKIE值
     */
    public static String getCookieValue(HttpServletRequest httpServletRequest, String cookieName) {
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
