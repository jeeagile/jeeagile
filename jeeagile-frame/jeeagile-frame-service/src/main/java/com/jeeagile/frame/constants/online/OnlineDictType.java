package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

public final class OnlineDictType {
    /**
     * 数据表字典。
     */
    public static final String TABLE = "01";
    /**
     * URL字典。
     */
    public static final String SYSTEM = "02";
    /**
     * 自定义字典。
     */
    public static final String CUSTOM = "99";

    private static final Map<String, String> ONLINE_DICT_TYPE_MAP = new HashMap<>();

    static {
        ONLINE_DICT_TYPE_MAP.put(TABLE, "数据表字典");
        ONLINE_DICT_TYPE_MAP.put(SYSTEM, "系统管理字典");
        ONLINE_DICT_TYPE_MAP.put(CUSTOM, "自定义字典");
    }

    /**
     * 判断是否当前常量字典的合法值。
     */
    public static boolean isValid(String value) {
        return value != null && ONLINE_DICT_TYPE_MAP.containsKey(value);
    }

    public static String getDesc(String value) {
        return ONLINE_DICT_TYPE_MAP.get(value);
    }

    /**
     * 私有构造函数
     */
    private OnlineDictType() {
    }
}
