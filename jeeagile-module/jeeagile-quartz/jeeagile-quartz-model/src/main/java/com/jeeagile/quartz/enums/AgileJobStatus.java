package com.jeeagile.quartz.enums;

public enum AgileJobStatus {
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

    AgileJobStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileJobStatus agileJobStatus : AgileJobStatus.values()) {
            if (agileJobStatus.code.equals(code)) {
                return agileJobStatus.desc;
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
