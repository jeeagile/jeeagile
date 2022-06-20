package com.jeeagile.process.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.vo.AgileProcessHistory;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
public interface IAgileProcessInstanceService extends IAgileBaseService<AgileProcessInstance> {
    /**
     * 启动流程定义
     *
     * @param processDefinitionId 流程定义ID
     * @param formData            表单数据
     * @return
     */
    boolean startProcessInstance(String processDefinitionId, Map<String, Object> formData);

    /**
     * 查询当前用户发起的流程
     *
     * @param agilePageable
     * @return
     */
    AgilePage<AgileProcessInstance> selectProcessInstancePage(AgilePageable<AgileProcessInstance> agilePageable);

    /**
     * 查询流程实例信息
     *
     * @param processInstanceId
     * @return
     */
    AgileProcessInstance selectProcessInstanceInfo(String processInstanceId);

    /**
     * 查询流程实例审批历史
     *
     * @param processInstanceId
     * @return
     */
    List<AgileProcessHistory> selectProcessInstanceHistory(String processInstanceId);


    /**
     * 撤销流程实例
     *
     * @param processInstanceId
     * @return
     */
    boolean cancelProcessInstance(String processInstanceId);
}
