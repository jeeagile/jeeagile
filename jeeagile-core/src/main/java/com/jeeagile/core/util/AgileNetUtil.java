package com.jeeagile.core.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Component
public class AgileNetUtil {
    private static final Logger log = LoggerFactory.getLogger(AgileNetUtil.class);
    //http://ip.aliyun.com/outGetIpInfo?&accessKey=alibaba-inc
    //http://whois.pconline.com.cn/ipJson.jsp
    public static final String IP_URL = "http://ip.aliyun.com/outGetIpInfo?&accessKey=alibaba-inc";
    public static final String UNKNOWN_IP = "XX XX";

    /**
     * 获取IP地址归属地区
     *
     * @param ip
     * @return
     */
    public static String getAddressByIp(String ip) {
        String address = UNKNOWN_IP;
        try {
            if (isInnerIp(ip)) {
                return "内网IP";
            }
            if (AgileUtil.isAddressEnabled()) {
                RestTemplate restTemplate = new RestTemplate();
                JSONObject rtnObj = restTemplate.getForObject(IP_URL + "&ip=" + ip, JSONObject.class);
                if (rtnObj.getString("code").equals("0")) {
                    JSONObject dataObj = rtnObj.getJSONObject("data");
                    String country = dataObj.getString("country");
                    String region = dataObj.getString("region");
                    String city = dataObj.getString("city");
                    region = region.replace("XX", "");
                    city = city.replace("XX", "");
                    if (country.equals("中国")) {
                        return String.format("%s %s", region, city);
                    } else {
                        return String.format("%s %s", country, region);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return address;
    }

    /**
     * 判断是否为内网IP
     *
     * @param ip
     * @return
     */
    public static boolean isInnerIp(String ip) {
        String regex = "^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }
}
