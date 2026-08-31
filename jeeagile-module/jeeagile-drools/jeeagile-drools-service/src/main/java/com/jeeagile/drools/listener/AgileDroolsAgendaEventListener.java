package com.jeeagile.drools.listener;

import com.jeeagile.drools.entity.AgileDroolsRuleLogger;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建人 wangcy
 * @创建日期 2024-01-05
 * @描述 规则引擎 议程事件监听器
 */
public class AgileDroolsAgendaEventListener implements AgendaEventListener {
    private static Logger logger = LoggerFactory.getLogger(AgileDroolsAgendaEventListener.class);
    private Map<String, AgileDroolsRuleLogger> loggerRuleMap = new HashMap<>();
    private String loggerId;

    public AgileDroolsAgendaEventListener(String loggerId) {
        this.loggerId = loggerId;
    }

    public Map<String, AgileDroolsRuleLogger> getLoggerRuleMap() {
        return this.loggerRuleMap;
    }

    /**
     * 成功匹配规则
     */
    @Override
    public void matchCreated(MatchCreatedEvent matchCreatedEvent) {
        logger.info("=====>>成功匹配规则：{}", matchCreatedEvent.getMatch().getRule().getName());
    }

    /**
     * 未匹配规则
     */
    @Override
    public void matchCancelled(MatchCancelledEvent matchCancelledEvent) {
        logger.info("=====>>未匹配规则：{}", matchCancelledEvent.getMatch().getRule().getName());
    }

    @Override
    public void beforeMatchFired(BeforeMatchFiredEvent beforeMatchFiredEvent) {
        Rule rule = beforeMatchFiredEvent.getMatch().getRule();
        AgileDroolsRuleLogger agileDroolsRuleLogger = new AgileDroolsRuleLogger();
        agileDroolsRuleLogger.setLoggerId(loggerId);
        agileDroolsRuleLogger.setRuleName(rule.getName());
        agileDroolsRuleLogger.setStartTime(new Date());
        loggerRuleMap.put(agileDroolsRuleLogger.getRuleName(), agileDroolsRuleLogger);
        logger.info("=====>>开始执行匹配规则，执行规则：{}", rule.getName());
    }

    @Override
    public void afterMatchFired(AfterMatchFiredEvent afterMatchFiredEvent) {
        Rule rule = afterMatchFiredEvent.getMatch().getRule();
        AgileDroolsRuleLogger agileDroolsRuleLogger = loggerRuleMap.get(rule.getName());
        agileDroolsRuleLogger.setEndTime(new Date());
        agileDroolsRuleLogger.setExecuteTime(agileDroolsRuleLogger.getEndTime().getTime() - agileDroolsRuleLogger.getStartTime().getTime());
        logger.info("=====>>结束执行匹配规则，执行规则：{}", rule.getName());
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
