package com.jeeagile.drools.autoconfigure;

import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.drools.kie.AgileKieBase;
import com.jeeagile.drools.kie.AgileKieRule;
import com.jeeagile.drools.kie.AgileKieSession;
import com.jeeagile.drools.kie.AgileKieTemplate;
import com.jeeagile.drools.properties.AgileDroolsProperties;
import com.jeeagile.drools.properties.AgileKieBaseProperties;
import com.jeeagile.drools.util.AgileDroolsUtil;
import org.kie.api.io.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-05
 * @描述 规则引擎
 */
@Configuration
@EnableConfigurationProperties(AgileDroolsProperties.class)
public class AgileDroolsAutoConfigure {
    private static Logger logger = LoggerFactory.getLogger(AgileKieTemplate.class);

    @Bean
    @ConditionalOnMissingBean
    public AgileKieTemplate agileKieTemplate(AgileDroolsProperties agileDroolsProperties) {
        AgileKieTemplate agileKieTemplate = new AgileKieTemplate();
        agileKieTemplate.setAgileKieBaseList(loadKieBaseList(agileDroolsProperties));
        return agileKieTemplate;
    }

    /**
     * 校验配置是否正确
     */
    private List<AgileKieBase> loadKieBaseList(AgileDroolsProperties agileDroolsProperties) {
        Map<String, AgileKieBaseProperties> kieBaseMap = agileDroolsProperties.getKieBase();
        List<AgileKieBase> agileKieBaseList = new ArrayList<>();
        boolean defaultFlag = false;
        for (String key : kieBaseMap.keySet()) {
            AgileKieBaseProperties agileKieBaseProperties = kieBaseMap.get(key);
            if (AgileStringUtil.isEmpty(agileKieBaseProperties.getPaths())) {
                throw new AgileFrameException("KieBaseName:" + key + "请配置规则文件路径！");
            }
            List<AgileKieRule> kieRuleList = this.loadKieRule(agileKieBaseProperties);
            AgileKieBase agileKieBase = new AgileKieBase();
            agileKieBase.setName(key);
            agileKieBase.setPackages(agileKieBaseProperties.getPackages());
            agileKieBase.setKieRuleList(kieRuleList);
            if (agileDroolsProperties.getDefaultKieBaseName().equals(key)) {
                defaultFlag = true;
                agileKieBase.setDefault(true);
            }
            agileKieBase.getKieSessionList().add(createKieSession(agileKieBase, agileKieBaseProperties));
            agileKieBaseList.add(agileKieBase);
        }
        // 如果未设置默认kieBase 则设置第一个为默认
        if (!defaultFlag && !agileKieBaseList.isEmpty()) {
            agileKieBaseList.get(0).setDefault(true);
            agileKieBaseList.get(0).getKieSessionList().get(0).setDefault(true);
        }
        return agileKieBaseList;
    }


    /**
     * 创建session
     */
    private AgileKieSession createKieSession(AgileKieBase agileKieBase, AgileKieBaseProperties agileKieBaseProperties) {
        AgileKieSession agileKieSession = new AgileKieSession(agileKieBase.getName());
        agileKieSession.setSessionName(agileKieBase.getName() + "-session");
        agileKieSession.setDefault(agileKieBase.isDefault());
        return agileKieSession;
    }

    /**
     * 读取规则文件内容
     */
    private List<AgileKieRule> loadKieRule(AgileKieBaseProperties agileKieBaseProperties) {
        List<AgileKieRule> kieRuleList = new ArrayList<>();
        List<File> ruleFileList = AgileDroolsUtil.loadRuleFile(agileKieBaseProperties.getPaths());
        for (File file : ruleFileList) {
            AgileKieRule agileKieRule = new AgileKieRule();
            agileKieRule.setContent(AgileDroolsUtil.loadFileContent(file, agileKieBaseProperties.getCharset()));
            agileKieRule.setType(ResourceType.determineResourceType(file.getName()));
            agileKieRule.setName(file.getName());
            agileKieRule.setPath(file.getPath());
            kieRuleList.add(agileKieRule);
        }
        return kieRuleList;
    }
}
