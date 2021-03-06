package com.jeeagile.process.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessForm;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程表单
 */
public interface IAgileProcessFormService extends IAgileBaseService<AgileProcessForm> {
    /**
     * 保存流程设计
     * @return
     */
    AgileProcessForm saveProcessFormDesigner(String formId, String formConf, String formFields);
}
