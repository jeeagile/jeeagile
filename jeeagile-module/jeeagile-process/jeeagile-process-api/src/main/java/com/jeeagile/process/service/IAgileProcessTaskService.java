package com.jeeagile.process.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.process.entity.AgileProcessDefinition;


/**
 * @author JeeAgile
 * @date 2022-06-10
 * @description 我的事务
 */
public interface IAgileProcessTaskService {
    /**
     * 查询可发起流程列表
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessDefinition> selectProcessDefinitionPage(AgilePageable<AgileProcessDefinition> agilePageable);

    /**
     * 获取流程定义详细信息
     *
     * @param processDefinitionId
     * @return
     */
    AgileProcessDefinition getProcessDefinitionInfo(String processDefinitionId);
}
