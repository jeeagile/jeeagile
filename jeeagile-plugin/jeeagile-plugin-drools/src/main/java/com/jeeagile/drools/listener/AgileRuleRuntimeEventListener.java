package com.jeeagile.drools.listener;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述 规则运行时事件监听器
 */
public class AgileRuleRuntimeEventListener implements RuleRuntimeEventListener {
    private static Logger logger = LoggerFactory.getLogger(AgileRuleRuntimeEventListener.class);
    /**
     * 对象插入时触发
     */
    @Override
    public void objectInserted(ObjectInsertedEvent objectInsertedEvent) {
        logger.info("=====>>插入对象：{}",objectInsertedEvent.getFactHandle());
    }
    /**
     * 对象更新时触发
     */
    @Override
    public void objectUpdated(ObjectUpdatedEvent objectUpdatedEvent) {
        logger.info("=====>>更新对象：{}",objectUpdatedEvent.getFactHandle());
    }
    /**
     * 对象删除时触发
     */
    @Override
    public void objectDeleted(ObjectDeletedEvent objectDeletedEvent) {
        logger.info("=====>>删除对象：{}",objectDeletedEvent.getFactHandle());
    }
}
