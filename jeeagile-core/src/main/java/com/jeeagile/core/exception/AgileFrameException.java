package com.jeeagile.core.exception;

import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.result.IAgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 系统异常类
 */
public class AgileFrameException extends AgileBaseException {
    /**
     * 构造函数 自定义异常类
     *
     * @param throwable
     */
    public AgileFrameException(Throwable throwable) {
        super(AgileResultCode.FAIL_SERVER_EXCEPTION, throwable);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param message
     */
    public AgileFrameException(String message) {
        super(AgileResultCode.FAIL_SERVER_EXCEPTION, message);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param message
     * @param throwable
     */
    public AgileFrameException(String message, Throwable throwable) {
        super(AgileResultCode.FAIL_SERVER_EXCEPTION, message, throwable);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param agileResultCode
     */
    public AgileFrameException(IAgileResultCode agileResultCode) {
        super(agileResultCode);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param agileResultCode
     * @param throwable
     */
    public AgileFrameException(IAgileResultCode agileResultCode, Throwable throwable) {
        super(agileResultCode, throwable);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param agileResultCode
     * @param message
     */
    public AgileFrameException(IAgileResultCode agileResultCode, String message) {
        super(agileResultCode, message);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param agileResultCode
     * @param message
     * @param throwable
     */
    public AgileFrameException(IAgileResultCode agileResultCode, String message, Throwable throwable) {
        super(agileResultCode, message, throwable);
    }
}
