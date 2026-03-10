package com.jeeagile.drools.kie;

import com.jeeagile.core.util.AgileStringUtil;
import lombok.Data;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;

import java.io.Serializable;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-08
 * @描述
 */
@Data
public class AgileKieSession implements Serializable {
    /**
     * 默认构造
     *
     */
    public AgileKieSession(String baseName) {
        this.baseName = baseName;
    }

    /**
     * kieBase名称
     */
    private String baseName;
    /**
     * sessionName
     */
    private String sessionName;
    /**
     * 是否开启监听
     */
    private boolean listener = true;
    /**
     * 是否为默认session
     */
    private boolean isDefault = false;
    /**
     * 会话状态 stateful 有状态 stateles 无状态
     */
    private KieSessionModel.KieSessionType type = KieSessionModel.KieSessionType.STATEFUL;

    /**
     * 议程事件监听器
     */
    private Class<? extends AgendaEventListener> agendaEventListener;
    /**
     * 执行流程事件监听器
     */
    private Class<? extends ProcessEventListener> processEventListener;
    /**
     * 规则运行时事件监听器
     */
    private Class<? extends RuleRuntimeEventListener> ruleRuntimeEventListener;

    public String getSessionName() {
        if (AgileStringUtil.isEmpty(this.sessionName)) {
            return this.baseName + "-session";
        } else {
            return this.sessionName;
        }
    }
}
