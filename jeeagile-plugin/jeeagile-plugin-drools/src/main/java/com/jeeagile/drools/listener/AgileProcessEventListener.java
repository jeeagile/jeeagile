package com.jeeagile.drools.listener;

import org.kie.api.event.process.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述 执行流程事件监听器
 */
public class AgileProcessEventListener implements ProcessEventListener {
    private static Logger logger = LoggerFactory.getLogger(AgileAgendaEventListener.class);

    /**
     * 流程启动之前
     */
    @Override
    public void beforeProcessStarted(ProcessStartedEvent processStartedEvent) {

    }

    /**
     * 流程启动之后
     */
    @Override
    public void afterProcessStarted(ProcessStartedEvent processStartedEvent) {

    }

    /**
     * 流程完成之前
     */
    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent processCompletedEvent) {

    }

    /**
     * 流程完成之后
     */
    @Override
    public void afterProcessCompleted(ProcessCompletedEvent processCompletedEvent) {

    }

    /**
     * 触发流程节点之前
     */
    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent processNodeTriggeredEvent) {

    }

    /**
     * 触发流程节点之后
     */
    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent processNodeTriggeredEvent) {

    }

    /**
     * 进入流程节点之前
     */
    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent processNodeLeftEvent) {

    }

    /**
     * 进入流程节点之后
     */
    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent processNodeLeftEvent) {

    }

    /**
     * 流程属性改变之前
     */
    @Override
    public void beforeVariableChanged(ProcessVariableChangedEvent processVariableChangedEvent) {

    }

    /**
     * 流程属性改变之后
     */
    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent processVariableChangedEvent) {

    }
}
