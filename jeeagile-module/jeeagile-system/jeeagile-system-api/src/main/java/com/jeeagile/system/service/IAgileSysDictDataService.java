package com.jeeagile.system.service;

import com.jeeagile.frame.service.IAgileBaseTreeService;
import com.jeeagile.system.entity.AgileSysDictData;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysDictDataService extends IAgileBaseTreeService<AgileSysDictData> {

    /**
     * 根据字典类型查询字典数据列表
     */
    List<AgileSysDictData> selectDictDataByDictType(String dictType);
}
