package com.jeeagile.process.constants;

import java.util.HashMap;
import java.util.Map;

public class ProcessFormType {

    /**
     * 流程表单
     */
    public static final String PROCESS_FORM = "01";

    /**
     * 业务表单
     */
    public static final String BUSINESS_FORM = "02";
    /**
     * 在线表单
     */
    public static final String ONLINE_FORM = "03";
    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(PROCESS_FORM, "流程表单");
        DICT_MAP.put(BUSINESS_FORM, "业务表单");
        DICT_MAP.put(ONLINE_FORM, "在线表单");
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
    private ProcessFormType() {
    }
}
