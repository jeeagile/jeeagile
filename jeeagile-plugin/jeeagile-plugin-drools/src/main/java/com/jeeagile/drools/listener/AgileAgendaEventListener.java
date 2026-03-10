package com.jeeagile.drools.listener;

import org.kie.api.event.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述 规则引擎 议程事件监听器
 */
public class AgileAgendaEventListener implements AgendaEventListener {
    private static Logger logger = LoggerFactory.getLogger(AgileAgendaEventListener.class);
    /**
     * 成功匹配规则
     */
    @Override
    public void matchCreated(MatchCreatedEvent matchCreatedEvent) {
        logger.info("=====>>成功匹配规则：{}" , matchCreatedEvent.getMatch().getRule().getName());
    }

    /**
     * 未匹配规则
     */
    @Override
    public void matchCancelled(MatchCancelledEvent matchCancelledEvent) {
        logger.info("=====>>未匹配规则：{}" , matchCancelledEvent.getMatch().getRule().getName());
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent beforeMatchFiredEvent) {
        logger.info("=====>>开始执行匹配规则，执行规则：{}" , beforeMatchFiredEvent.getMatch().getRule().getName());
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent afterMatchFiredEvent) {
        logger.info("=====>>结束执行匹配规则，执行规则：{}" , afterMatchFiredEvent.getMatch().getRule().getName());
    }

    @Override
    public void agendaGroupPopped(AgendaGroupPoppedEvent agendaGroupPoppedEvent) {

    }

    @Override
    public void agendaGroupPushed(AgendaGroupPushedEvent agendaGroupPushedEvent) {

    }

    @Override
    public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

    }

    @Override
    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

    }

    @Override
    public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

    }

    @Override
    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

    }
}
