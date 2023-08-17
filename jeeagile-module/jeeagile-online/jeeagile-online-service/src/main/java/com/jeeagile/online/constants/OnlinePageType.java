package com.jeeagile.online.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2027-07-25
 * @description 在线表单 表单页面类型字典常量
 */
public class OnlinePageType {
    /**
     * 查询页面
     */
    public static final String QUERY = "01";
    /**
     * 编辑页面
     */
    public static final String EDIT = "02";

    /**
     * 流程页面
     */
    public static final String FLOW = "03";

    /**
     * 工单列表
     */
    public static final String ORDER = "04";
    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(4);

    static {
        DICT_MAP.put(QUERY, "查询页面");
        DICT_MAP.put(EDIT, "编辑页面");
        DICT_MAP.put(FLOW, "流程页面");
        DICT_MAP.put(ORDER, "工单列表");
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
    private OnlinePageType() {
    }
}
