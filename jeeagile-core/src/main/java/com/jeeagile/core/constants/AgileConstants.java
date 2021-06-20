package com.jeeagile.core.constants;

import com.jeeagile.core.util.CharsetUtil;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description JeeAgile全局常量类
 */
public class AgileConstants {
    private AgileConstants() {
    }

    public static final String UTF8 = CharsetUtil.UTF_8;

    public static final String AGILE_SECURITY = "agile.security";
    public static final String AGILE_PROTOCOL = "agile.protocol";
    public static final String AGILE_PROTOCOL_TYPE = "type";

    // 用户信息KEY 用于上下文用户信息传递
    public static final String AGILE_USER_DATA = "AGILE_USER_DATA";

    // 数据范围 01 全部数据权限
    public static final String AGILE_DATA_SCOPE_01 = "01";
    // 数据范围 02 本部门数据权限
    public static final String AGILE_DATA_SCOPE_02 = "02";
    // 数据范围 03 本部门及以下数据权限
    public static final String AGILE_DATA_SCOPE_03 = "03";
    // 数据范围 04 仅本人数据权限
    public static final String AGILE_DATA_SCOPE_04 = "04";
    // 数据范围 05 自定义部门数据权限
    public static final String AGILE_DATA_SCOPE_05 = "05";


    //资源映射路径 前缀
    public static final String AGILE_RESOURCE_PREFIX = "/resource";

}
