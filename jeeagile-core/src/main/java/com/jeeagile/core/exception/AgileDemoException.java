package com.jeeagile.core.exception;


import com.jeeagile.core.result.AgileResultCode;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 异常处理类
 */
public class AgileDemoException extends AgileBaseException {

    public AgileDemoException(String message) {
        super(AgileResultCode.FAIL_DEMO_EXCEPTION, message);
    }

    public AgileDemoException() {
        super(AgileResultCode.FAIL_DEMO_EXCEPTION);
    }
}
