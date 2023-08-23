package com.jeeagile.core.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-22
 * @description 审核状态
 */
public class AgileAuditStatus {
    /**
     * 审核中
     */
    public static final String AUDIT = "0";
    /**
     * 审核通过
     */
    public static final String PASS = "1";
    /**
     * 审核拒绝
     */
    public static final String REJECT = "2";
    /**
     * MAP
     */
    private static final Map<String, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(AUDIT, "审核中");
        DICT_MAP.put(PASS, "审核通过");
        DICT_MAP.put(REJECT, "审核拒绝");
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
    private AgileAuditStatus() {
    }
}
