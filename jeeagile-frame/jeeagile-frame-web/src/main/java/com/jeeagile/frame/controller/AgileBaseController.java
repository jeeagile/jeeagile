package com.jeeagile.frame.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.IAgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public abstract class AgileBaseController {
    /**
     * 返回成功结果集
     *
     * @return
     */
    protected <T> AgileResult<T> rtnSuccess() {
        return AgileResult.success();
    }

    /**
     * 返回成功结果集
     *
     * @param message 指定返回消息
     * @return
     */
    protected <T> AgileResult<T> rtnSuccess(String message) {
        return AgileResult.success(message);
    }

    /**
     * 返回成功结果集
     *
     * @param data 返回的数据结果集
     * @return
     */
    protected <T> AgileResult<T> rtnSuccess(Object data) {
        return AgileResult.success(data);
    }

    /**
     * 返回成功结果集
     *
     * @param message 指定返回消息
     * @param data    返回的数据结果集
     * @return
     */
    protected <T> AgileResult<T> rtnSuccess(Object data, String message) {
        return AgileResult.success(data, message);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @return
     */
    protected <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode) {
        return AgileResult.agileResult(agileResultCode);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @param data            返回的数据结果集
     * @return
     */
    protected <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, Object data) {
        return AgileResult.agileResult(agileResultCode, data);
    }

    /**
     * 返回成功结果集
     *
     * @param agileResultCode 返回枚举对象
     * @param data            返回的数据结果集
     * @param message         返回描述
     * @return
     */
    protected <T> AgileResult<T> agileResult(IAgileResultCode agileResultCode, Object data, String message) {
        return AgileResult.agileResult(agileResultCode, data, message);
    }

    /**
     * 指定类型返回结果
     *
     * @param agileResultCode
     * @return
     */
    protected <T> AgileResult<T> rtnError(IAgileResultCode agileResultCode) {
        return AgileResult.error(agileResultCode);
    }

    /**
     * 指定类型返回结果
     *
     * @param agileResultCode
     * @param message
     * @return
     */
    protected <T> AgileResult<T> rtnError(IAgileResultCode agileResultCode, String message) {
        return AgileResult.error(agileResultCode, message);
    }

    /**
     * 异常处理
     *
     * @param agileResultCode
     * @param ex
     * @return
     */
    protected <T> AgileResult<T> rtnError(IAgileResultCode agileResultCode, Exception ex) {
        return AgileResult.error(agileResultCode, ex);
    }

    /**
     * 异常处理 如果异常信息获取不到异常信息则使用默认msg
     *
     * @param agileResultCode
     * @param ex              异常信息
     * @param defaultMessage  默认信息
     * @return
     */
    protected <T> AgileResult<T> rtnError(IAgileResultCode agileResultCode, Exception ex, String defaultMessage) {
        return AgileResult.error(agileResultCode, ex, defaultMessage);
    }

    /**
     * 异常处理
     *
     * @param ex             异常信息
     * @param defaultMessage 默认信息
     * @return
     */
    protected <T> AgileResult<T> rtnError(Exception ex, String defaultMessage) {
        return AgileResult.error(ex, defaultMessage);
    }
}
