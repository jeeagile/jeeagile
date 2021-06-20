package com.jeeagile.core.result;

import java.io.Serializable;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileResultCode extends Serializable {
    /**
     * 编码
     *
     * @return
     */
    String getCode();

    /**
     * 消息
     *
     * @return
     */
    String getMessage();
}
