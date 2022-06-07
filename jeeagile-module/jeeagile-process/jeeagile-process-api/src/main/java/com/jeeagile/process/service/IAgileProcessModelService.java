package com.jeeagile.process.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessModel;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
public interface IAgileProcessModelService extends IAgileBaseService<AgileProcessModel> {
    /**
     * 保存流程模型设计
     *
     * @param modelId
     * @param modelXml
     * @return
     */
    AgileProcessModel saveProcessDesigner(String modelId, String modelXml);

    /**
     * 流程发布
     * @param modelId
     * @return
     */
    boolean processDeployment(String modelId);
}
