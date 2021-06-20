package com.jeeagile.system.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.enums.AgileStatusEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.validate.ValidateUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.mapper.AgileSysDictDataMapper;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDictDataServiceImpl extends AgileBaseTreeServiceImpl<AgileSysDictDataMapper, AgileSysDictData> implements IAgileSysDictDataService {

    @Override
    public AgilePage<AgileSysDictData> selectDictDataPage(AgilePageable<AgileSysDictData> agilePageable) {
        return this.page(agilePageable, getSysDictDataQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public List<AgileSysDictData> selectDictDataList(AgileSysDictData agileSysDictData) {
        if (agileSysDictData == null || StringUtil.isEmpty(agileSysDictData.getDictType())) {
            throw new AgileValidateException("字典类型不能为空！");
        }
        return this.list(getSysDictDataQueryWrapper(agileSysDictData));
    }

    @Override
    public List<AgileSysDictData> selectDictDataByDictType(String dictType) {
        QueryWrapper<AgileSysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysDictData::getDictType, dictType);
        queryWrapper.lambda().eq(AgileSysDictData::getDictStatus, AgileStatusEnum.NORMAL.getCode());
        queryWrapper.lambda().orderByAsc(AgileSysDictData::getDictSort);
        return this.list(queryWrapper);
    }

    @Override
    public AgileSysDictData selectDictDataById(String dictDataId) {
        return this.getById(dictDataId);
    }

    @Override
    public AgileSysDictData saveDictData(AgileSysDictData agileSysDictData) {
        ValidateUtil.validateObject(agileSysDictData);
        validateSysDictData(agileSysDictData);
        this.save(agileSysDictData);
        return agileSysDictData;
    }

    @Override
    public boolean updateDictDataById(AgileSysDictData agileSysDictData) {
        ValidateUtil.validateObject(agileSysDictData);
        validateSysDictData(agileSysDictData);
        return this.updateById(agileSysDictData);
    }

    @Override
    public boolean deleteDictDataById(String dictDataId) {
        AgileSysDictData agileSysDictData = this.getById(dictDataId);
        if (agileSysDictData.getSystemFlag().equals(AgileFlagEnum.YES.getCode())) {
            throw new AgileValidateException("系统内置，不能删除！");
        }
        return this.removeById(dictDataId);
    }

    @Override
    public boolean deleteDictDataByDictType(String dictType) {
        if (StringUtil.isEmpty(dictType)) {
            throw new AgileValidateException("字典类型不能为空！");
        }
        QueryWrapper<AgileSysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysDictData::getDictType, dictType);
        return this.remove(queryWrapper);
    }

    /**
     * 校验字典标签与字典键值不能与已存在的数据重复
     */
    private void validateSysDictData(AgileSysDictData agileSysDictData) {
        LambdaQueryWrapper<AgileSysDictData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgileSysDictData::getDictType, agileSysDictData.getDictType());
        if (agileSysDictData.getId() != null) {
            queryWrapper.ne(AgileSysDictData::getId, agileSysDictData.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileSysDictData::getDictLabel, agileSysDictData.getDictLabel()).or().eq(AgileSysDictData::getDictValue, agileSysDictData.getDictValue())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("字典标签或字典键值已存在，请核实！");
        }
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysDictData> getSysDictDataQueryWrapper(AgileSysDictData agileSysDictData) {
        QueryWrapper<AgileSysDictData> queryWrapper = new QueryWrapper<>();
        if (agileSysDictData != null) {
            //添加字典类型查询条件
            if (StringUtil.isNotEmpty(agileSysDictData.getDictType())) {
                queryWrapper.lambda().eq(AgileSysDictData::getDictType, agileSysDictData.getDictType());
            }
            //添加字典标签查询条件
            if (StringUtil.isNotEmpty(agileSysDictData.getDictLabel())) {
                queryWrapper.lambda().like(AgileSysDictData::getDictLabel, agileSysDictData.getDictLabel());
            }
            //添加字典键值查询条件
            if (StringUtil.isNotEmpty(agileSysDictData.getDictValue())) {
                queryWrapper.lambda().like(AgileSysDictData::getDictValue, agileSysDictData.getDictValue());
            }
            //添加状态查询条件
            if (StringUtil.isNotEmpty(agileSysDictData.getDictStatus())) {
                queryWrapper.lambda().like(AgileSysDictData::getDictStatus, agileSysDictData.getDictStatus());
            }
        }
        queryWrapper.lambda().orderByAsc(AgileSysDictData::getDictSort);
        return queryWrapper;
    }

}
