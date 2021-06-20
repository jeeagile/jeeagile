package com.jeeagile.core.util;

import com.alibaba.fastjson.JSONObject;
import com.jeeagile.core.properties.AgileProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Component
public class AddressUtil {
    private static final Logger log = LoggerFactory.getLogger(AddressUtil.class);
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    public static final String UNKNOWN_IP = "XX XX";

    public static String getRealAddressByIp(String ip) {
        String address = UNKNOWN_IP;
        try {
            if (internalIp(ip)) {
                return "内网IP";
            }
            if (AgileUtil.isAddressEnabled()) {
                RestTemplate restTemplate = new RestTemplate();
                String rtnStr = restTemplate.getForObject(IP_URL + "?ip=" + ip + "&json=true", String.class);
                if (StringUtil.isEmpty(rtnStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN_IP;
                }
                JSONObject obj = JSONObject.parseObject(rtnStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public static boolean internalIp(String ip) {
        byte[] addr = formatIpV4(ip);
        return internalIp(addr) || "127.0.0.1".equals(ip);
    }

    private static boolean internalIp(byte[] ipByte) {
        if (ipByte == null || ipByte.length < 2) {
            return true;
        }
        final byte byte0 = ipByte[0];
        final byte byte1 = ipByte[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (byte0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (byte1 >= SECTION_3 && byte1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (byte1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    public static byte[] formatIpV4(String text) {
        if (text.length() == 0) {
            return null;
        }
        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }
}
