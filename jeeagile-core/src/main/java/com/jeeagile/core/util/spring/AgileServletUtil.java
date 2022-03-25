package com.jeeagile.core.util.spring;

import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.util.AgileArrayUtil;
import com.jeeagile.core.util.AgileStringUtil;
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
public class AgileServletUtil {
    private AgileServletUtil() {
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
        if (AgileStringUtil.isEmpty(userToken)) {
            userToken = httpServletRequest.getParameter(AgileConstants.AGILE_TOKEN);
        }
        if (AgileStringUtil.isEmpty(userToken)) {
            userToken = getCookieValue(httpServletRequest, AgileConstants.AGILE_TOKEN);
        }
        return userToken;
    }

    /**
     * 获取请求头信息
     * @param headerName
     * @return
     */
    public static String getHeaderValue(String headerName) {
        return getHeaderValue(getHttpServletRequest(), headerName);
    }

    /**
     * 获取请求头信息
     * @param headerName
     * @return
     */
    public static String getHeaderValue(HttpServletRequest httpServletRequest, String headerName) {
        if (httpServletRequest != null) {
            return httpServletRequest.getHeader(headerName);
        }
        return null;
    }

    /**
     * 根据COOKIE名称获取对应得COOKIE值
     */
    public static String getCookieValue(String cookieName) {
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
        if (httpServletRequest != null && AgileArrayUtil.isNotEmpty(httpServletRequest.getCookies())) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
