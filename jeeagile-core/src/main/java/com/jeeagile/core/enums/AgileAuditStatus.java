package com.jeeagile.core.enums;

/**
 * @author JeeAgile
 * @date 2022-06-24
 * @description 启用状态枚举
 */
public enum AgileAuditStatus {
    AUDIT("0", "审核中"),
    PASS("1", "审核通过"),
    REJECT("2", "审核拒绝");
    /**
     * 码值
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    AgileAuditStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileAuditStatus agileAuditStatus : AgileAuditStatus.values()) {
            if (agileAuditStatus.code.equals(code)) {
                return agileAuditStatus.desc;
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
