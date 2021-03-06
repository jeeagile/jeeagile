package com.jeeagile.process.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessDefinition;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程定义
 */
public interface IAgileProcessDefinitionService extends IAgileBaseService<AgileProcessDefinition> {
    /**
     * 查询可发起流程列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessDefinition> selectMainVersionPage(AgilePageable<AgileProcessDefinition> agilePageable);

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

    /**
     * 修改流程定义为主版本
     *
     * @param id
     * @return
     */
    boolean updateMainVersion(String id);

}
