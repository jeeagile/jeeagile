package com.jeeagile.drools.service;

import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.drools.entity.AgileDroolsModel;
import com.jeeagile.drools.vo.AgileDroolsModelFieldInfo;
import com.jeeagile.frame.service.IAgileBaseService;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-12 10:58:02
 * @description 规则引擎 数据对象 Service接口
 */
public interface IAgileDroolsModelService extends IAgileBaseService<AgileDroolsModel> {
    /**
     * 修改数据对象状态
     */
    boolean changeStatus(Serializable modelId, String modelStatus);

    /**
     * 获取模型字段信息
     *
     * @param modelId
     * @return
     */
    List<AgileDroolsModelFieldInfo> selectModelFieldInfoList(String modelId);

    /**
     * 验证数据对象（名称+包名唯一性）
     */
    void validateModel(AgileDroolsModel agileDroolsModel) throws AgileValidateException;
}
