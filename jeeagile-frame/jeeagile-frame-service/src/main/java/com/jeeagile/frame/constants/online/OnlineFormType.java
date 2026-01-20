package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-17
 * @description 在线表单 表单类型字典常量
 */
public class OnlineFormType {
    /**
     * 业务表单
     */
    public static final String BUSINESS = "01";
    /**
     * 流程表单
     */
    public static final String FLOW = "02";


    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(2);

    static {
        DICT_MAP.put(BUSINESS, "业务表单");
        DICT_MAP.put(FLOW, "流程表单");
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
    private OnlineFormType() {
    }
}
