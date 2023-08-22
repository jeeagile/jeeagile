package com.jeeagile.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.online.constants.OnlineDictType;
import com.jeeagile.frame.constants.SysYesNo;
import com.jeeagile.online.entity.AgileOnlineDict;
import com.jeeagile.online.mapper.AgileOnlineDictMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;

/**
 * @author JeeAgile
 * @date 2023-07-17
 * @description 在线表单 字典管理 接口实现
 */
@AgileService
public class AgileOnlineDictServiceImpl extends AgileBaseServiceImpl<AgileOnlineDictMapper, AgileOnlineDict> implements IAgileOnlineDictService {
    @Override
    public LambdaQueryWrapper<AgileOnlineDict> queryWrapper(AgileOnlineDict agileOnlineDict) {
        LambdaQueryWrapper<AgileOnlineDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlineDict != null) {
            if (AgileStringUtil.isNotEmpty(agileOnlineDict.getDictName())) {
                lambdaQueryWrapper.like(AgileOnlineDict::getDictName, agileOnlineDict.getDictName());
            }
            if (AgileStringUtil.isNotEmpty(agileOnlineDict.getDictType())) {
                lambdaQueryWrapper.eq(AgileOnlineDict::getDictType, agileOnlineDict.getDictType());
            }
        }
        lambdaQueryWrapper.orderByAsc(AgileOnlineDict::getDictType);
        return lambdaQueryWrapper;
    }

    @Override
    public void validateModel(AgileOnlineDict agileOnlineDict) {
        if (OnlineDictType.TABLE.equals(agileOnlineDict.getDictType())) {
            if (AgileStringUtil.isEmpty(agileOnlineDict.getKeyColumnName())) {
                throw new AgileValidateException("字典表键字段名称不能为空！");
            }
            if (AgileStringUtil.isEmpty(agileOnlineDict.getValueColumnName())) {
                throw new AgileValidateException("字典表值字段名称不能为空！");
            }
            if (AgileStringUtil.isEmpty(agileOnlineDict.getLabelColumnName())) {
                throw new AgileValidateException("字典表标签字段名称不能为空！");
            }
            if (SysYesNo.YES.equals(agileOnlineDict.getTreeFlag())) {
                if (AgileStringUtil.isEmpty(agileOnlineDict.getParentKeyColumnName())) {
                    throw new AgileValidateException("字典表父键字段名称不能为空！");
                }
            }
            agileOnlineDict.setSystemDictType(null);
            agileOnlineDict.setDictDataJson(null);
            return;
        }
        if (OnlineDictType.SYSTEM.equals(agileOnlineDict.getDictType())) {
            if (AgileStringUtil.isEmpty(agileOnlineDict.getSystemDictType())) {
                throw new AgileValidateException("系统字典类型不能为空！");
            }
            agileOnlineDict.setTableName(null);
            agileOnlineDict.setParentKeyColumnName(null);
            agileOnlineDict.setKeyColumnName(null);
            agileOnlineDict.setValueColumnName(null);
            agileOnlineDict.setLabelColumnName(null);
            agileOnlineDict.setDictParamJson(null);
            agileOnlineDict.setDictFilterJson(null);
            return;
        }
        if (OnlineDictType.CUSTOM.equals(agileOnlineDict.getDictType())) {
            if (AgileStringUtil.isEmpty(agileOnlineDict.getDictDataJson())) {
                throw new AgileValidateException("字典数据不能为空！");
            }
            agileOnlineDict.setTableName(null);
            agileOnlineDict.setSystemDictType(null);
            agileOnlineDict.setParentKeyColumnName(null);
            agileOnlineDict.setKeyColumnName(null);
            agileOnlineDict.setValueColumnName(null);
            agileOnlineDict.setLabelColumnName(null);
            agileOnlineDict.setDictParamJson(null);
            agileOnlineDict.setDictFilterJson(null);
            return;
        }
    }
}
