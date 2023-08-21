package com.jeeagile.online.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * @author JeeAgile
 * @date 2027-07-25
 * @description 在线表单 过滤类型
 */
public class OnlineFilterType {
     
    /**
     * 无过滤
     */
    public static final String NONE = "01";
    /**
     * 等于过滤
     */
    public static final String EQUAL = "02";
    /**
     * 范围过滤
     */
    public static final String RANGE = "03";
    /**
     * 模糊过滤
     */
    public static final String LIKE = "04";
    /**
     * IN LIST列表过滤
     */
    public static final String IN = "05";

    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(NONE, "无过滤");
        DICT_MAP.put(EQUAL, "等于过滤");
        DICT_MAP.put(RANGE, "范围过滤");
        DICT_MAP.put(LIKE, "模糊过滤");
        DICT_MAP.put(IN, "IN LIST列表过滤");
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
    private OnlineFilterType() {
    }
}
