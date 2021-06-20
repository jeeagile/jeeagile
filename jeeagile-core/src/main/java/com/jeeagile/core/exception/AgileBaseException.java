package com.jeeagile.core.exception;

import com.jeeagile.core.result.IAgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 异常处理基类
 */
public abstract class AgileBaseException extends RuntimeException {
    private final IAgileResultCode agileResultCode;

    /**
     * 构造函数
     *
     * @param agileResultCode
     */
    protected AgileBaseException(IAgileResultCode agileResultCode) {
        super(agileResultCode.getMessage());
        this.agileResultCode = agileResultCode;
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param throwable
     */
    protected AgileBaseException(IAgileResultCode agileResultCode, Throwable throwable) {
        super(throwable);
        this.agileResultCode = agileResultCode;
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param message
     */
    protected AgileBaseException(IAgileResultCode agileResultCode, String message) {
        super(message);
        this.agileResultCode = agileResultCode;
    }

    /**
     * 构造函数
     *
     * @param agileResultCode
     * @param message
     * @param throwable
     */
    protected AgileBaseException(IAgileResultCode agileResultCode, String message, Throwable throwable) {
        super(message, throwable);
        this.agileResultCode = agileResultCode;
    }

    /**
     * 构造函数
     *
     * @return
     */
    public IAgileResultCode getAgileResultCode() {
        return this.agileResultCode;
    }
}
