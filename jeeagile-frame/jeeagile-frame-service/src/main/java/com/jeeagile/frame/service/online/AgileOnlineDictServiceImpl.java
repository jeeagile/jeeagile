package com.jeeagile.frame.service.online;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.entity.online.AgileOnlineDict;
import com.jeeagile.frame.mapper.online.AgileOnlineDictMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.system.IAgileSysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileOnlineDictServiceImpl extends AgileBaseServiceImpl<AgileOnlineDictMapper, AgileOnlineDict> implements IAgileOnlineDictService {
    @Autowired
    private IAgileSysDictDataService agileSysDictDataService;

    @Override
    public LambdaQueryWrapper<AgileOnlineDict> queryWrapper(AgileOnlineDict agileOnlineDict) {
        LambdaQueryWrapper<AgileOnlineDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlineDict != null) {
//            if (AgileStringUtil.isNotEmpty(agileOnlineDict.getConfigName())) {
//                lambdaQueryWrapper.like(AgileOnlineDict::getConfigName, agileOnlineDict.getConfigName());
//            }
//            if (AgileStringUtil.isNotEmpty(agileOnlineDict.getConfigKey())) {
//                lambdaQueryWrapper.eq(AgileOnlineDict::getConfigKey, agileOnlineDict.getConfigKey());
//            }
//            if (AgileStringUtil.isNotEmpty(agileOnlineDict.getSystemFlag())) {
//                lambdaQueryWrapper.eq(AgileOnlineDict::getSystemFlag, agileOnlineDict.getSystemFlag());
//            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public void saveModelValidate(AgileOnlineDict AgileOnlineDict) {
        this.validateData(AgileOnlineDict);
    }

    @Override
    public void updateModelValidate(AgileOnlineDict AgileOnlineDict) {
        this.validateData(AgileOnlineDict);
    }

    /**
     * 校验参数名称或参数键名不能与已存在的数据重复
     */
    private void validateData(AgileOnlineDict agileOnlineDict) {
        LambdaQueryWrapper<AgileOnlineDict> queryWrapper = new LambdaQueryWrapper<>();
        if (agileOnlineDict.getId() != null) {
            queryWrapper.ne(AgileOnlineDict::getId, agileOnlineDict.getId());
        }
//        queryWrapper.and(wrapper ->
//                wrapper.eq(AgileOnlineDict::getConfigKey, AgileOnlineDict.getConfigKey()).or().eq(AgileOnlineDict::getConfigName, AgileOnlineDict.getConfigName())
//        );
        if (this.count(queryWrapper) > 0) {
            throw new AgileValidateException("参数名称或参数键名已存在！");
        }
    }
}
