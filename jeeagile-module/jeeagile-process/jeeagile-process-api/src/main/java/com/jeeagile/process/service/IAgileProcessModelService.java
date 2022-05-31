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
     * @param modelId
     * @param modelXml
     * @return
     */
    AgileProcessModel saveProcessModelDesigner(String modelId, String modelXml);
}
