package com.jeeagile.core.enums;

/**
 * @author JeeAgile
 * @date 2022-06-24
 * @description 启用状态枚举
 */
public enum AgileEnableStatus {
    ENABLE("0", "启用"),
    DISABLE("1", "停用");
    /**
     * 码值
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    AgileEnableStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileEnableStatus agileEnableStatus : AgileEnableStatus.values()) {
            if (agileEnableStatus.code.equals(code)) {
                return agileEnableStatus.desc;
            }
        }
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
