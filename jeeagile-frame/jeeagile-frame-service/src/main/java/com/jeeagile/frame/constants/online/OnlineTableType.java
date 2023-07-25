package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-25
 * @description 在线表单 数据表类型字典常量
 */
public class OnlineTableType {
    /**
     * 数据主表
     */
    public static final String MASTER = "01";
    /**
     * 一对一从表
     */
    public static final String ONE_TO_ONE = "02";

    /**
     * 一对多从表
     */
    public static final String ONE_TO_MANY = "03";


    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(MASTER, "数据主表");
        DICT_MAP.put(ONE_TO_ONE, "一对一从表");
        DICT_MAP.put(ONE_TO_MANY, "一对多从表");
    }

    /**
     * 判断值是否合法。
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
    private OnlineTableType() {
    }
}
