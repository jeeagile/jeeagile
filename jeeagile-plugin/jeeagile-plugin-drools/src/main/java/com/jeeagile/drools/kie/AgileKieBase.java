package com.jeeagile.drools.kie;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建人 JeeAgile
 * @创建日期 2025-01-08
 * @描述
 */
@Data
public class AgileKieBase implements Serializable {
    /**
     * 映射KieBaseName
     */
    private String name;
    /**
     * 是否默认
     */
    private boolean isDefault = false;
    /**
     * 映射packages
     */
    private List<String> packages;
    /**
     * 规则文件路径
     */
    private List<AgileKieRule> kieRuleList = new ArrayList<>();
    /**
     * session
     */
    private List<AgileKieSession> kieSessionList = new ArrayList<>();
}
