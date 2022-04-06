package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.entity.AgileSysDictType;
import com.jeeagile.system.mapper.AgileSysDictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

import static com.jeeagile.core.constants.AgileConstants.SYS_NORMAL_DISABLE;
import static com.jeeagile.core.constants.AgileConstants.SYS_YES_NO;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysDictTypeServiceImpl extends AgileBaseServiceImpl<AgileSysDictTypeMapper, AgileSysDictType> implements IAgileSysDictTypeService {

    @Autowired
    private IAgileSysDictDataService agileSysDictDataService;

    @Override
    public LambdaQueryWrapper<AgileSysDictType> queryWrapper(AgileSysDictType agileSysDictType) {
        LambdaQueryWrapper<AgileSysDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysDictType != null) {
            //字典名称模糊查询
            if (AgileStringUtil.isNotEmpty(agileSysDictType.getDictName())) {
                lambdaQueryWrapper.like(AgileSysDictType::getDictName, agileSysDictType.getDictName());
            }
            //字典类型模糊查询
            if (AgileStringUtil.isNotEmpty(agileSysDictType.getDictType())) {
                lambdaQueryWrapper.like(AgileSysDictType::getDictType, agileSysDictType.getDictType());
            }
            //状态
            if (AgileStringUtil.isNotEmpty(agileSysDictType.getDictStatus())) {
                lambdaQueryWrapper.eq(AgileSysDictType::getDictStatus, agileSysDictType.getDictStatus());
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public List<AgileSysDictType> selectExportData(AgileSysDictType agileSysDictType) {
        List<AgileSysDictType> agileSysDictTypeList = this.selectList(agileSysDictType);
        agileSysDictTypeList.forEach(item -> {
            item.setDictStatus(agileSysDictDataService.getSysDictLabel(SYS_NORMAL_DISABLE, item.getDictStatus()));
            item.setSystemFlag(agileSysDictDataService.getSysDictLabel(SYS_YES_NO,item.getSystemFlag()));
        });
        return agileSysDictTypeList;
    }

    @Override
    public void saveModelValidate(AgileSysDictType agileSysDictType) {
        this.validateData(agileSysDictType);
    }

    @Override
    public boolean updateModel(AgileSysDictType agileSysDictType) {
        this.validateData(agileSysDictType);
        AgileSysDictType oldAgileSysDictType = this.getById(agileSysDictType.getId());
        if (oldAgileSysDictType.getDictType() != agileSysDictType.getDictType()) {
            LambdaUpdateWrapper<AgileSysDictData> lambdaUpdateWrapper = new UpdateWrapper().lambda();
            lambdaUpdateWrapper.set(AgileSysDictData::getDictType, agileSysDictType.getDictType());
            lambdaUpdateWrapper.eq(AgileSysDictData::getDictType, oldAgileSysDictType.getDictType());
            agileSysDictDataService.update(lambdaUpdateWrapper);
        }
        return this.updateById(agileSysDictType);
    }

    @Override
    public boolean deleteModel(Serializable id) {
        AgileSysDictType agileSysDictType = this.getById(id);
        if (agileSysDictType.getSystemFlag().equals(AgileFlagEnum.YES.getCode())) {
            throw new AgileValidateException("系统内置，不能删除！");
        }
        this.deleteDictData(agileSysDictType);
        return this.removeById(id);
    }

    /**
     * 删除字段数据
     */
    private boolean deleteDictData(AgileSysDictType agileSysDictType) {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysDictData::getDictType, agileSysDictType.getDictType());
        return agileSysDictDataService.remove(lambdaQueryWrapper);
    }

    /**
     * 校验字典名称与字典类型是否已存在
     */
    private void validateData(AgileSysDictType agileSysDictType) {
        LambdaQueryWrapper<AgileSysDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysDictType.getId() != null) {
            lambdaQueryWrapper.ne(AgileSysDictType::getId, agileSysDictType.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileSysDictType::getDictName, agileSysDictType.getDictName()).or().eq(AgileSysDictType::getDictType, agileSysDictType.getDictType())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("字典名称与字典类型不能与已存在的数据重复！");
        }
    }
}
