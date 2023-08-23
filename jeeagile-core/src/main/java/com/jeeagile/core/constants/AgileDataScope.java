package com.jeeagile.core.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-22
 * @description 数据权限
 */
public class AgileDataScope {
    /**
     * 数据范围 01 全部数据权限
     */
    public static final String ALL = "01";
    /**
     * 数据范围 02 本部门数据权限
     */
    public static final String DEPT = "02";
    /**
     * 数据范围 03 本部门及以下数据权限
     */
    public static final String DEPT_AND_CHILD = "03";
    /**
     * 数据范围 04 仅本人数据权限
     */
    public static final String SELF = "04";
    /**
     * 数据范围 05 自定义部门数据权限
     */
    public static final String CUSTOM = "05";
    /**
     * MAP
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(5);

    static {
        DICT_MAP.put(ALL, "全部数据权限");
        DICT_MAP.put(DEPT, "本部门数据权限");
        DICT_MAP.put(DEPT_AND_CHILD, "本部门及以下数据权限");
        DICT_MAP.put(SELF, "仅本人数据权限");
        DICT_MAP.put(CUSTOM, "自定义部门数据权限");
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
    private AgileDataScope() {
    }
}
