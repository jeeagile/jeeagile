package com.jeeagile.system.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.enums.AgileStatusEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.mapper.AgileSysDictDataMapper;

import java.io.Serializable;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDictDataServiceImpl extends AgileBaseTreeServiceImpl<AgileSysDictDataMapper, AgileSysDictData> implements IAgileSysDictDataService {

    @Override
    public LambdaQueryWrapper<AgileSysDictData> queryWrapper(AgileSysDictData agileSysDictData) {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysDictData != null) {
            lambdaQueryWrapper.eq(AgileSysDictData::getDictType, agileSysDictData.getDictType());
            //添加字典标签查询条件
            if (AgileStringUtil.isNotEmpty(agileSysDictData.getDictLabel())) {
                lambdaQueryWrapper.like(AgileSysDictData::getDictLabel, agileSysDictData.getDictLabel());
            }
            //添加字典键值查询条件
            if (AgileStringUtil.isNotEmpty(agileSysDictData.getDictValue())) {
                lambdaQueryWrapper.like(AgileSysDictData::getDictValue, agileSysDictData.getDictValue());
            }
            //添加状态查询条件
            if (AgileStringUtil.isNotEmpty(agileSysDictData.getDictStatus())) {
                lambdaQueryWrapper.like(AgileSysDictData::getDictStatus, agileSysDictData.getDictStatus());
            }
        } else {
            lambdaQueryWrapper.eq(AgileSysDictData::getDictType, null);
        }
        lambdaQueryWrapper.orderByAsc(AgileSysDictData::getDictSort);
        return lambdaQueryWrapper;
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
    public void saveModelValidate(AgileSysDictData agileSysDictData) {
        this.validateData(agileSysDictData);
    }

    @Override
    public void updateModelValidate(AgileSysDictData agileSysDictData) {
        this.validateData(agileSysDictData);
    }

    @Override
    public boolean deleteModel(Serializable id) {
        AgileSysDictData agileSysDictData = this.getById(id);
        if (agileSysDictData.getSystemFlag().equals(AgileFlagEnum.YES.getCode())) {
            throw new AgileValidateException("系统内置，不能删除！");
        }
        return this.removeById(id);
    }

    @Override
    public boolean deleteDictDataByDictType(String dictType) {
        if (AgileStringUtil.isEmpty(dictType)) {
            throw new AgileValidateException("字典类型不能为空！");
        }
        QueryWrapper<AgileSysDictData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysDictData::getDictType, dictType);
        return this.remove(queryWrapper);
    }

    /**
     * 校验字典标签与字典键值是否存在
     */
    private void validateData(AgileSysDictData agileSysDictData) {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysDictData::getDictType, agileSysDictData.getDictType());
        if (agileSysDictData.getId() != null) {
            lambdaQueryWrapper.ne(AgileSysDictData::getId, agileSysDictData.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileSysDictData::getDictLabel, agileSysDictData.getDictLabel()).or().eq(AgileSysDictData::getDictValue, agileSysDictData.getDictValue())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("字典标签或字典键值已存在，请核实！");
        }
    }
}
