package com.jeeagile.core.util.system.info;

import lombok.Data;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
public class UserInfo {
    /**
     * 登录用户名字
     */
    private String name;
    /**
     * 登录用户的home目录
     */
    private String homeDir;
    /**
     * 登录用户工作目录
     */
    private String userDir;
    /**
     * 语言
     */
    private String language;
}
