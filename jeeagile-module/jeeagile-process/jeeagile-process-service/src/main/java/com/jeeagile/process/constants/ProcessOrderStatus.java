package com.jeeagile.process.constants;

import java.util.HashMap;
import java.util.Map;

public class ProcessOrderStatus {
    /**
     * 已提交
     */
    public static final String SUBMITTED = "01";

    /**
     * 审批中
     */
    public static final String APPROVAL = "02";

    /**
     * 已拒绝
     */
    public static final String REJECTED = "03";

    /**
     * 已完成
     */
    public static final String FINISHED = "04";

    /**
     * 已终止
     */
    public static final String STOPPED = "05";

    /**
     * 已撤销
     */
    public static final String CANCEL = "06";


    /**
     *
     */
    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(SUBMITTED, "已提交");
        DICT_MAP.put(APPROVAL, "审批中");
        DICT_MAP.put(REJECTED, "已拒绝");
        DICT_MAP.put(FINISHED, "已完成");
        DICT_MAP.put(STOPPED, "已终止");
        DICT_MAP.put(CANCEL, "已撤销");
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
    private ProcessOrderStatus() {
    }
}
