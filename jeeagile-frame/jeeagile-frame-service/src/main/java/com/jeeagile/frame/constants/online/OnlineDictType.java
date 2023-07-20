package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-17
 * @description 在线表单 字典类型常量
 */
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

    /**
     *
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(TABLE, "数据表字典");
        DICT_MAP.put(SYSTEM, "系统管理字典");
        DICT_MAP.put(CUSTOM, "自定义字典");
    }

    /**
     * 判断字典值是否合法。
     */
    public static boolean isValid(String value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 获取字典描述
     */
    public static String getDesc(String value) {
        return DICT_MAP.get(value);
    }

    /**
     * 私有构造函数
     */
    private OnlineDictType() {
    }
}
