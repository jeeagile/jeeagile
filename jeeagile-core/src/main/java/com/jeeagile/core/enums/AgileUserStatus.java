package com.jeeagile.core.enums;

public enum AgileUserStatus {
    NORMAL("0", "正常"),
    DISABLE("1", "停用");
    /**
     * 码值
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    AgileUserStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileUserStatus agileUserStatus : AgileUserStatus.values()) {
            if (agileUserStatus.code.equals(code)) {
                return agileUserStatus.desc;
            }
        }
        return "";
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
