package com.jeeagile.frame.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-22
 * @description 用户性别
 */
public class SysUserSex {
    /**
     * 男
     */
    public static final String MAN = "0";
    /**
     * 女
     */
    public static final String WOMAN = "1";
    /**
     * 未知
     */
    public static final String UNKNOWN = "2";
    /**
     * MAP
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(MAN, "男");
        DICT_MAP.put(WOMAN, "女");
        DICT_MAP.put(UNKNOWN, "未知");
    }

    /**
     * 判断是否当前常量字典的合法值。
     */
    public static boolean isValid(String value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    public static String getDesc(String value) {
        return DICT_MAP.get(value);
    }

    /**
     * 私有构造函数
     */
    private SysUserSex() {
    }
}
