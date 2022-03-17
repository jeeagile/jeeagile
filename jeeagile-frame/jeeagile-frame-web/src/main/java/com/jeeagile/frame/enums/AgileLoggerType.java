package com.jeeagile.frame.enums;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 日志类型
 */
public enum AgileLoggerType {
    /**
     * 查询数据
     */
    SELECT,

    /**
     * 查看明细
     */
    DETAIL,

    /**
     * 新增数据
     */
    ADD,

    /**
     * 修改数据
     */
    UPDATE,

    /**
     * 删除数据
     */
    DELETE,

    /**
     * 清空数据
     */
    CLEAR,
    /**
     * 用户授权
     */
    GRANT,
    /**
     * 导出数据
     */
    EXPORT,
    /**
     * 导入数据
     */
    IMPORT,
    /**
     * 用户登录
     */
    LOGIN,
    /**
     * 用户强退
     */
    FORCE,
    /**
     * 生成代码
     */
    GENERATOR,
    /**
     * 清空数据
     */
    CLEAN,
    /**
     * 其它操作
     */
    OTHER,
}
