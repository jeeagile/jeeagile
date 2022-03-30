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
     * 获取字典数据列表
     */
    List<AgileSysDictData> getSysDictDataList(String dictType);

    /**
     * 获取字典数据
     */
    AgileSysDictData getSysDictData(String dictType, String dictValue);
}
