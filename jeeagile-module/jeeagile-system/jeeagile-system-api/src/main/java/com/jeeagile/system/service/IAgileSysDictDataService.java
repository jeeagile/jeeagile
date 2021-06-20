package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
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
     * 分页查询字典数列表
     */
    AgilePage<AgileSysDictData> selectDictDataPage(AgilePageable<AgileSysDictData> agilePageable);

    /**
     * 查询字典数据列表
     */
    List<AgileSysDictData> selectDictDataList(AgileSysDictData agileSysDictData);

    /**
     * 根据字典类型查询字典数据列表
     */
    List<AgileSysDictData> selectDictDataByDictType(String dictType);

    /**
     * 查看字典数据信息
     */
    AgileSysDictData selectDictDataById(String dictDataId);

    /**
     * 新增字典数据
     */
    AgileSysDictData saveDictData(AgileSysDictData agileSysDictData);

    /**
     * 更新字典数据
     */
    boolean updateDictDataById(AgileSysDictData agileSysDictData);

    /**
     * 删除字典数据
     */
    boolean deleteDictDataById(String dictDataId);

    /**
     * 删除字典数据
     */
    boolean deleteDictDataByDictType(String dictType);
}
