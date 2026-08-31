package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsScene;
import com.jeeagile.drools.vo.AgileDroolsRuleInfo;
import com.jeeagile.drools.vo.AgileDroolsSceneInfo;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 规则场景 Service接口
 */
public interface IAgileDroolsSceneService extends IAgileBaseService<AgileDroolsScene> {
    /**
     * 修改规则场景状态
     *
     * @param sceneId
     * @return
     */
    boolean changeStatus(String sceneId, String sceneStatus);

    /**
     * 获取场景执行信息
     */
    AgileDroolsSceneInfo info(Serializable sceneId);

    /**
     * 执行规则场景
     *
     * @param sceneCode
     * @param paramData
     * @return
     */
    Object execute(String sceneCode, Map paramData);
}
