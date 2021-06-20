package com.jeeagile.system.service;

import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.system.entity.AgileSysDictType;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public interface IAgileSysDictTypeService extends IAgileBaseService<AgileSysDictType> {
    /**
     * 分页查询字典类型列表
     */
    AgilePage<AgileSysDictType> selectDictTypePage(AgilePageable<AgileSysDictType> agilePageable);

    /**
     * 查询字典类型列表
     */
    List<AgileSysDictType> selectDictTypeList(AgileSysDictType agileSysDictType);

    /**
     * 查看字典类型信息
     */
    AgileSysDictType selectDictTypeById(String dictTypeId);

    /**
     * 新增字典类型
     */
    AgileSysDictType saveDictType(AgileSysDictType agileSysDictType);

    /**
     * 更新字典类型
     */
    boolean updateDictTypeById(AgileSysDictType agileSysDictType);

    /**
     * 删除字典类型
     */
    boolean deleteDictTypeById(String dictTypeId);
}
