package com.jeeagile.process.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessDefinition;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程定义
 */
public interface IAgileProcessDefinitionService extends IAgileBaseService<AgileProcessDefinition> {
    /**
     * 流程定义激活
     *
     * @param id
     * @return
     */
    boolean processDefinitionActive(String id);

    /**
     * 流程定义挂起
     *
     * @param id
     * @return
     */
    boolean processDefinitionSuspend(String id);
}
