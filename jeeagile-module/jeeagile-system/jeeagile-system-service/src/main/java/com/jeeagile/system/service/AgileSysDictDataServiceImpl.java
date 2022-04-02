package com.jeeagile.system.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileTreeServiceImpl;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.mapper.AgileSysDictDataMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDictDataServiceImpl extends AgileTreeServiceImpl<AgileSysDictDataMapper, AgileSysDictData> implements IAgileSysDictDataService {

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
    public List<AgileSysDictData> getSysDictDataList(String dictType) {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysDictData::getDictType, dictType);
        lambdaQueryWrapper.orderByAsc(AgileSysDictData::getDictSort);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public AgileSysDictData getSysDictData(String dictType, String dictValue) {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysDictData::getDictType, dictType);
        lambdaQueryWrapper.eq(AgileSysDictData::getDictValue, dictValue);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    @Cacheable(value = "sys_dict")
    public String getSysDictLabel(String dictType, String dictValue) {
        AgileSysDictData agileSysDictData = this.getSysDictData(dictType, dictValue);
        if (agileSysDictData != null && agileSysDictData.isNotEmptyPk()) {
            return agileSysDictData.getDictLabel();
        }
        return "";
    }

    @Override
    @Cacheable(value = "sys_dict")
    public String getSysDictValue(String dictType, String dictLabel) {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysDictData::getDictType, dictType);
        lambdaQueryWrapper.eq(AgileSysDictData::getDictValue, dictLabel);
        AgileSysDictData agileSysDictData = this.getOne(lambdaQueryWrapper);
        if (agileSysDictData != null && agileSysDictData.isNotEmptyPk()) {
            return agileSysDictData.getDictValue();
        }
        return dictLabel;
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
