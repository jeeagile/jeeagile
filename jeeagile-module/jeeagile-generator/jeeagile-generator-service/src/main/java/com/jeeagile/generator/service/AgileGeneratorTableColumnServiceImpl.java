package com.jeeagile.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;
import com.jeeagile.generator.mapper.AgileGeneratorTableColumnMapper;

import java.util.List;


/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@AgileService
public class AgileGeneratorTableColumnServiceImpl extends AgileBaseServiceImpl<AgileGeneratorTableColumnMapper, AgileGeneratorTableColumn> implements IAgileGeneratorTableColumnService {

    @Override
    public List<AgileGeneratorTableColumn> selectListByTableId(String tableId) {
        QueryWrapper<AgileGeneratorTableColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileGeneratorTableColumn::getTableId, tableId);
        queryWrapper.lambda().orderByAsc(AgileGeneratorTableColumn::getColumnSort);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteByTableId(String tableId) {
        QueryWrapper<AgileGeneratorTableColumn> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileGeneratorTableColumn::getTableId, tableId);
        return this.remove(queryWrapper);
    }
}
