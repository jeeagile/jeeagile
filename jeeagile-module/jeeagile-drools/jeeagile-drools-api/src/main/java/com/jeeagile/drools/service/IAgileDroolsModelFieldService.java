package com.jeeagile.drools.service;

import com.jeeagile.drools.entity.AgileDroolsModelField;
import com.jeeagile.frame.service.IAgileBaseService;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2026-03-13 15:26:20
 * @description 规则引擎 数据对象字段 Service接口
 */
public interface IAgileDroolsModelFieldService extends IAgileBaseService<AgileDroolsModelField>  {
    /**
     * 查询数据对象数据
     *
     * @param modelId
     * @return
     */
    List<AgileDroolsModelField> selectModelFieldList(String modelId);
}
