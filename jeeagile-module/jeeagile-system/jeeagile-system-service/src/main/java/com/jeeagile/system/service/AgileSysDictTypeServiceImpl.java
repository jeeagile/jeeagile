package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeeagile.core.enums.AgileFlagEnum;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.validate.ValidateUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysDictData;
import com.jeeagile.system.entity.AgileSysDictType;
import com.jeeagile.system.mapper.AgileSysDictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService()
public class AgileSysDictTypeServiceImpl extends AgileBaseServiceImpl<AgileSysDictTypeMapper, AgileSysDictType> implements IAgileSysDictTypeService {

    @Autowired
    private IAgileSysDictDataService agileSysDictDataService;

    @Override
    public AgilePage<AgileSysDictType> selectDictTypePage(AgilePageable<AgileSysDictType> agilePageable) {
        return this.page(agilePageable, getSysDictTypeQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public List<AgileSysDictType> selectDictTypeList(AgileSysDictType agileSysDictType) {
        return this.list(getSysDictTypeQueryWrapper(agileSysDictType));
    }

    @Override
    public AgileSysDictType selectDictTypeById(String dictTypeId) {
        return this.getById(dictTypeId);
    }

    @Override
    public AgileSysDictType saveDictType(AgileSysDictType agileSysDictType) {
        //校验业务数据
        ValidateUtil.validateObject(agileSysDictType);
        validateSysDictType(agileSysDictType);
        this.save(agileSysDictType);
        return agileSysDictType;
    }

    @Override
    public boolean updateDictTypeById(AgileSysDictType agileSysDictType) {
        //校验业务数据
        ValidateUtil.validateObject(agileSysDictType);
        validateSysDictType(agileSysDictType);
        AgileSysDictType oldAgileSysDictType = this.getById(agileSysDictType.getId());
        if (oldAgileSysDictType.getDictType() != agileSysDictType.getDictType()) {
            LambdaUpdateWrapper<AgileSysDictData> updateWrapper = new UpdateWrapper().lambda();
            updateWrapper.set(AgileSysDictData::getDictType, agileSysDictType.getDictType());
            updateWrapper.eq(AgileSysDictData::getDictType, oldAgileSysDictType.getDictType());
            agileSysDictDataService.update(updateWrapper);
        }
        return this.updateById(agileSysDictType);
    }

    @Override
    public boolean deleteDictTypeById(String dictTypeId) {
        AgileSysDictType agileSysDictType = this.getById(dictTypeId);
        if (agileSysDictType.getSystemFlag().equals(AgileFlagEnum.YES.getCode())) {
            throw new AgileValidateException("系统内置，不能删除！");
        }
        agileSysDictDataService.deleteDictDataByDictType(agileSysDictType.getDictType());
        return this.removeById(dictTypeId);
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysDictType> getSysDictTypeQueryWrapper(AgileSysDictType agileSysDictType) {
        QueryWrapper<AgileSysDictType> queryWrapper = new QueryWrapper<>();
        if (agileSysDictType != null) {
            //字典名称模糊查询
            if (StringUtil.isNotEmpty(agileSysDictType.getDictName())) {
                queryWrapper.lambda().like(AgileSysDictType::getDictName, agileSysDictType.getDictName());
            }
            //字典类型模糊查询
            if (StringUtil.isNotEmpty(agileSysDictType.getDictType())) {
                queryWrapper.lambda().like(AgileSysDictType::getDictType, agileSysDictType.getDictType());
            }
            //状态
            if (StringUtil.isNotEmpty(agileSysDictType.getDictStatus())) {
                queryWrapper.lambda().eq(AgileSysDictType::getDictStatus, agileSysDictType.getDictStatus());
            }
        }
        return queryWrapper;
    }

    /**
     * 校验字典名称与字典类型不能与已存在的数据重复
     */
    private void validateSysDictType(AgileSysDictType agileSysDictType) {
        LambdaQueryWrapper<AgileSysDictType> queryWrapper = new LambdaQueryWrapper<>();
        if (agileSysDictType.getId() != null) {
            queryWrapper.ne(AgileSysDictType::getId, agileSysDictType.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileSysDictType::getDictName, agileSysDictType.getDictName()).or().eq(AgileSysDictType::getDictType, agileSysDictType.getDictType())
        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("字典名称与字典类型不能与已存在的数据重复！");
        }
    }
}
