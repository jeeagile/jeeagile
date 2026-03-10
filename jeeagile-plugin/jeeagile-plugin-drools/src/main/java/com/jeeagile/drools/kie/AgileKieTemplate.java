package com.jeeagile.drools.kie;

import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.util.AgileStringUtil;
import lombok.Setter;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.builder.model.ListenerModel;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.conf.EvaluatorOption;
import org.kie.internal.builder.conf.KnowledgeBuilderOption;
import org.kie.internal.builder.conf.SingleValueKnowledgeBuilderOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述 drools kie 模板
 */
public class AgileKieTemplate implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(AgileKieTemplate.class);
    /**
     * KieServices
     */
    private final KieServices kieServices = KieServices.Factory.get();
    /**
     * kie 文件系统 尽量避免多次实例化
     */
    private final KieFileSystem kieFileSystem = this.kieServices.newKieFileSystem();
    /**
     * 构建KieModule使用
     */
    private final KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
    /**
     * KieContainer
     */
    private KieContainer kieContainer;

    /**
     * -- SETTER --
     *
     * @param agileKieBaseList
     */
    @Setter
    private List<AgileKieBase> agileKieBaseList;

    /**
     * 默认构造函数
     */
    public AgileKieTemplate() {

    }

    /**
     * 默认构造函数
     */
    public AgileKieTemplate(KnowledgeBuilderOption... options) {
        for (KnowledgeBuilderOption option : options) {
            if (option instanceof EvaluatorOption) {
                this.kieModuleModel.setConfigurationProperty(EvaluatorOption.PROPERTY_NAME + option.getPropertyName(), ((EvaluatorOption) option).getEvaluatorDefinition().getClass().getName());
            } else if (option instanceof SingleValueKnowledgeBuilderOption) {
                this.kieModuleModel.setConfigurationProperty(option.getPropertyName(), option.toString());
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ReleaseId releaseId = this.kieServices.newReleaseId("com.jeeagile", "jeeagile-plugin-drools", "1.0.0");
        this.kieFileSystem.generateAndWritePomXML(releaseId);
        this.kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
        this.kieServices.newKieBuilder(kieFileSystem).buildAll();
        this.kieContainer = this.kieServices.newKieContainer(releaseId);
        this.buildKieContainer();
    }

    /**
     * buildKieContainer
     */
    private void buildKieContainer() {
        if (AgileStringUtil.isNotEmpty(this.agileKieBaseList)) {
            agileKieBaseList.forEach(agileKieBase -> {
                this.addKieBaseModel(agileKieBase);
                if (AgileStringUtil.isNotEmpty(agileKieBase.getKieRuleList())) {
                    agileKieBase.getKieRuleList().forEach(agileKieRule -> {
                        this.addContent(agileKieRule.getPath(), agileKieRule.getContent());
                    });
                }
            });
            this.updateKieContainer();
        }
    }

    /**
     * 创建默认KieBaseModel
     *
     * @param agileKieBase
     * @return
     */
    private void createKieBaseModel(AgileKieBase agileKieBase) {
        // 创建KieBaseModel
        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel(agileKieBase.getName());
        // 设置是否默认kieBase
        kieBaseModel.setDefault(agileKieBase.isDefault());
        // 加载包路径
        for (String pkg : agileKieBase.getPackages()) {
            kieBaseModel.addPackage(pkg);
        }
        if (AgileStringUtil.isEmpty(agileKieBase.getKieSessionList())) {
            List<AgileKieSession> kieSessionList = new ArrayList<>();
            AgileKieSession agileKieSession = new AgileKieSession(agileKieBase.getName());
            agileKieSession.setDefault(agileKieBase.isDefault());
            kieSessionList.add(agileKieSession);
            agileKieBase.setKieSessionList(kieSessionList);
        }
        for (AgileKieSession agileKieSession : agileKieBase.getKieSessionList()) {
            this.createKieSessionModel(agileKieSession);
        }
    }

    /**
     * 创建 KieSessionModel
     *
     * @param agileKieSession
     */
    private void createKieSessionModel(AgileKieSession agileKieSession) {
        if (AgileStringUtil.isEmpty(agileKieSession.getBaseName())) {
            throw new AgileFrameException("baseName不能为空！");
        }
        KieBaseModel kieBaseModel = this.kieModuleModel.getKieBaseModels().get(agileKieSession.getBaseName());
        if (kieBaseModel == null) {
            return;
        }
        KieSessionModel kieSessionModel = null;
        if (kieBaseModel.getKieSessionModels().containsKey(agileKieSession.getSessionName())) {
            kieSessionModel = kieBaseModel.getKieSessionModels().get(agileKieSession.getSessionName());
        } else {
            kieSessionModel = kieBaseModel.newKieSessionModel(agileKieSession.getSessionName());
        }
        if (agileKieSession.isListener()) {
            if (agileKieSession.getAgendaEventListener() != null) {
                kieSessionModel.newListenerModel(agileKieSession.getAgendaEventListener().getTypeName(), ListenerModel.Kind.AGENDA_EVENT_LISTENER);
            } else {
                kieSessionModel.newListenerModel("com.jeeagile.drools.listener.AgileAgendaEventListener", ListenerModel.Kind.AGENDA_EVENT_LISTENER);
            }
            if (agileKieSession.getRuleRuntimeEventListener() != null) {
                kieSessionModel.newListenerModel(agileKieSession.getRuleRuntimeEventListener().getTypeName(), ListenerModel.Kind.RULE_RUNTIME_EVENT_LISTENER);
            } else {
                kieSessionModel.newListenerModel("com.jeeagile.drools.listener.AgileRuleRuntimeEventListener", ListenerModel.Kind.RULE_RUNTIME_EVENT_LISTENER);
            }
            if (agileKieSession.getProcessEventListener() != null) {
                kieSessionModel.newListenerModel(agileKieSession.getProcessEventListener().getTypeName(), ListenerModel.Kind.PROCESS_EVENT_LISTENER);
            } else {
                kieSessionModel.newListenerModel("com.jeeagile.drools.listener.AgileProcessEventListener", ListenerModel.Kind.PROCESS_EVENT_LISTENER);
            }
        }
        kieSessionModel.setDefault(agileKieSession.isDefault());
        kieSessionModel.setType(agileKieSession.getType());
    }

    /**
     * 判断kiebase是否存在
     *
     * @param kieBaseName
     * @return
     */
    public boolean existsKieBase(String kieBaseName) {
        if (this.kieContainer == null) {
            return false;
        }
        if (kieContainer.getKieBaseNames().contains(kieBaseName)) {
            return true;
        }
        return false;
    }

    /**
     * 新增规则
     *
     * @param agileKieBase
     */
    public void addKieBaseModel(AgileKieBase agileKieBase) {
        this.addOrUpdateKieBaseModel(agileKieBase);
    }

    /**
     * 更新kieBase
     *
     * @param agileKieBase
     */
    public void updateKieBaseModel(AgileKieBase agileKieBase) {
        this.addOrUpdateKieBaseModel(agileKieBase);
    }

    /**
     * 新增或者更新规则文件
     *
     * @param agileKieBase
     */
    public void addOrUpdateKieBaseModel(AgileKieBase agileKieBase) {
        if (!existsKieBase(agileKieBase.getName())) {
            this.createKieBaseModel(agileKieBase);
        } else {
            KieBaseModel kieBaseModel = kieModuleModel.getKieBaseModels().get(agileKieBase.getName());
            List<String> packages = kieBaseModel.getPackages();
            for (String pkg : agileKieBase.getPackages()) {
                if (!packages.contains(pkg)) {
                    kieBaseModel.addPackage(pkg);
                }
            }
            if (AgileStringUtil.isNotEmpty(agileKieBase.getKieSessionList())) {
                for (AgileKieSession agileKieSession : agileKieBase.getKieSessionList()) {
                    this.createKieSessionModel(agileKieSession);
                }
            }
        }
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
    }

    /**
     * 添加规则
     */
    public void addKieRule(AgileKieRule agileKieRule) {
        this.addOrUpdateKieRule(agileKieRule);
    }

    /**
     * 更新规则
     */
    public void updateKieRule(AgileKieRule agileKieRule) {
        this.addOrUpdateKieRule(agileKieRule);
    }

    /**
     * 添加或更新规则
     */
    public void addOrUpdateKieRule(AgileKieRule agileKieRule) {
        this.addContent(agileKieRule);
        this.updateKieContainer();
    }

    /**
     * 添加规则
     */
    public void addKieRule(List<AgileKieRule> agileKieRuleList) {
        this.addOrUpdateKieRule(agileKieRuleList);
    }

    /**
     * 更新规则
     */
    public void updateKieRule(List<AgileKieRule> agileKieRuleList) {
        this.addOrUpdateKieRule(agileKieRuleList);
    }

    /**
     * 更新规则
     */
    public void addOrUpdateKieRule(List<AgileKieRule> agileKieRuleList) {
        agileKieRuleList.forEach(this::addContent);
        this.updateKieContainer();
    }

    /**
     * path必须带文件类型后缀，且同名文件会进行内容覆盖
     */
    public void addContent(AgileKieRule agileKieRule) {
        this.addContent(agileKieRule.getPath(), agileKieRule.getContent());
    }

    /**
     * path必须带文件类型后缀，且同名文件会进行内容覆盖
     */
    private void addContent(String path, String content) {
        if (!path.contains("src/main/resources/")) {
            path = "src/main/resources/" + path.replace("\\", "/");
        }
        kieFileSystem.write(path, content);
    }

    /**
     * 删除文件
     */
    public void removeContent(String path) {
        kieFileSystem.delete(path);
        kieServices.newKieBuilder(kieFileSystem).buildAll();
    }

    /**
     * 验证规则并更新容器
     */
    public void updateKieContainer() {
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Message message : results.getMessages(Message.Level.ERROR)) {
                stringBuilder.append(message.getId()).append("、").append(message.getText()).append("\r\n");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            logger.error("规则文件验证未通过：{}", stringBuilder);
            throw new AgileValidateException(stringBuilder.toString());
        }
        ((KieContainerImpl) this.kieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
    }

    /**
     * 删除默认kieBase规则
     */
    public void removeKieRule(String packageName, String ruleName) {
        KieBase kieBase = this.getKieBase();
        kieBase.removeRule(packageName, ruleName);
    }

    /**
     * 删除规则
     */
    public void removeKieRule(String kieBaseName, String packageName, String ruleName) {
        KieBase kieBase = this.getKieBase(kieBaseName);
        kieBase.removeRule(packageName, ruleName);
    }

    /**
     * 删除规则包
     */
    public void removeKiePackage(String packageName) {
        KieBase kieBase = this.getKieBase();
        kieBase.removeKiePackage(packageName);
    }

    /**
     * 删除规则包
     */
    public void removeKiePackage(String kieBaseName, String packageName) {
        KieBase kieBase = this.getKieBase(kieBaseName);
        kieBase.removeKiePackage(packageName);
    }

    /**
     * 获取KieBase
     */
    public KieBase getKieBase(String kieBaseName) throws AgileBaseException {
        if (this.existsKieBase(kieBaseName)) {
            return kieContainer.getKieBase(kieBaseName);
        } else {
            throw new AgileFrameException("KieBase《" + kieBaseName + "》不存在！");
        }
    }

    /**
     * 获取默认KieBase
     */
    public KieBase getKieBase() throws AgileBaseException {
        if (this.kieContainer != null) {
            KieBase kieBase = kieContainer.getKieBase();
            if (kieBase == null) {
                throw new AgileFrameException("未设置默认KieBase！");
            }
            return kieBase;
        } else {
            throw new AgileFrameException("获取默认KieBase出错！");
        }
    }

    /**
     * KieSession
     */
    public KieSession getKieSession(String kieSessionName) throws AgileBaseException {
        if (this.kieContainer != null) {
            KieSession kieSession = kieContainer.newKieSession(kieSessionName);
            if (kieSession == null) {
                kieSession = kieContainer.newKieSession(kieSessionName + "-session");
            }
            if (kieSession == null) {
                throw new AgileFrameException("KieSession《" + kieSessionName + "》不存在！");
            }
            return kieSession;
        } else {
            throw new AgileFrameException("获取KieSession出错！");
        }
    }

    /**
     * 获取默认KieSession
     */
    public KieSession getKieSession() throws AgileBaseException {
        if (this.kieContainer != null) {
            KieSession kieSession = kieContainer.newKieSession();
            if (kieSession == null) {
                throw new AgileFrameException("未设置默认kieSession！");
            }
            return kieSession;
        } else {
            throw new AgileFrameException("获取默认KieSession出错！");
        }
    }

    /**
     * 仅验证规则文件是否正确 不对kieContainer进行更新
     */
    public Results verify(AgileKieRule agileKieRule) {
        try {
            this.addContent(agileKieRule.getPath(), agileKieRule.getContent());
            Results results = kieServices.newKieBuilder(kieFileSystem).buildAll().getResults();
            return results;
        } finally {
            kieFileSystem.delete(agileKieRule.getPath());
            kieServices.newKieBuilder(kieFileSystem).buildAll();
        }
    }

    /**
     * 仅验证规则文件是否正确 不对kieContainer进行更新
     */
    public Results verify(String content) {
        return this.verify(content, ResourceType.DRL);
    }

    /**
     * 仅验证规则文件是否正确 不对kieContainer进行更新
     */
    public Results verify(String content, ResourceType resourceType) {
        String path = this.generateResourceName(AgileStringUtil.getUuid(), resourceType);
        this.addContent(path, content);
        Results results = kieServices.newKieBuilder(kieFileSystem).buildAll().getResults();
        kieFileSystem.delete(path);
        this.updateKieContainer();
        return results;
    }

    /**
     * 执行规则
     */
    public int fireAllRules(String kieBaseName, Object... objects) {
        KieSession kieSession = null;
        try {
            kieSession = this.getKieSession(kieBaseName);
            for (Object object : objects) {
                kieSession.insert(object);
            }
            return kieSession.fireAllRules();
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }

    /**
     * 根据kieSessionName执行规则
     */
    public int fireAllRules(Object... objects) {
        KieSession kieSession = null;
        try {
            kieSession = this.getKieSession();
            for (Object object : objects) {
                kieSession.insert(object);
            }
            return kieSession.fireAllRules();
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }

    /**
     * 生产资源文件路径
     *
     * @param name
     * @param type
     * @return
     */
    private String generateResourceName(String name, ResourceType type) {
        return "src/main/resources/" + name + "." + type.getDefaultExtension();
    }
}
