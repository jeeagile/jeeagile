package com.jeeagile.core.util;

import com.jeeagile.core.util.system.util.AgileSystemUtil;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileAgentUtil {
    private AgileAgentUtil() {
    }

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP_V6 = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IP_V4 = "127.0.0.1";

    /**
     * 获取用户客户端IP地址
     *
     * @param httpServletRequest
     * @return
     */
    public static String getUserClientIp(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return UNKNOWN;
        }
        String ip = getHttpHeaderIp(httpServletRequest);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
            if (LOCALHOST_IP_V4.equals(ip) || LOCALHOST_IP_V6.equals(ip)) {
                ip = AgileSystemUtil.getHostInfo().getAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.indexOf(AgileCharUtil.COMMA) > -1) {
            ip = ip.substring(0, ip.indexOf(AgileCharUtil.COMMA));
        }
        return LOCALHOST_IP_V6.equals(ip) ? LOCALHOST_IP_V4 : ip;
    }

    /**
     * 从httpheader获取ip
     *
     * @param httpServletRequest
     * @return
     */
    private static String getHttpHeaderIp(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-Forwarded-For");
        if (AgileStringUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (AgileStringUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (AgileStringUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("X-Real-IP");
        }
        if (AgileStringUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (AgileStringUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        return ip;
    }

    /**
     * 获取用户代理对象
     *
     * @param httpServletRequest
     * @return
     */
    public static UserAgent getUserAgent(HttpServletRequest httpServletRequest) {
        return UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
    }
}
