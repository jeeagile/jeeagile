package com.jeeagile.generator.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
public interface IAgileGeneratorTableColumnService extends IAgileBaseService<AgileGeneratorTableColumn> {
    /**
     * 根据生成表ID获取字段信息
     */
    List<AgileGeneratorTableColumn> selectListByTableId(String tableId);

    /**
     * 根据生成表ID删除字段信息
     * @param tableId
     * @return
     */
    boolean deleteByTableId(String tableId);
}
