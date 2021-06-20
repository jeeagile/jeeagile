package com.jeeagile.core.enums;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 状态枚举
 */
public enum AgileStatusEnum {
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

    AgileStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        for (AgileStatusEnum agileStatusEnum : AgileStatusEnum.values()) {
            if (agileStatusEnum.code.equals(code)) {
                return agileStatusEnum.desc;
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
