package com.jeeagile.frame.constants.online;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-17
 * @description 在线表单状态字典常量
 */
public class OnlineFormStatus {
    /**
     * 编辑基础信息
     */
    public static final String FORM_BASIC = "01";
    /**
     * 编辑数据模型
     */
    public static final String DATA_MODEL = "02";
    /**
     * 表单页面设计
     */
    public static final String PAGE_DESIGN = "03";

    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(FORM_BASIC, "编辑基础信息");
        DICT_MAP.put(DATA_MODEL, "编辑数据模型");
        DICT_MAP.put(PAGE_DESIGN, "表单页面设计");
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
    private OnlineFormStatus() {
    }
}
