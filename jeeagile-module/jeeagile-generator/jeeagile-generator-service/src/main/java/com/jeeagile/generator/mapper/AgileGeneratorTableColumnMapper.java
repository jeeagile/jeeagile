package com.jeeagile.generator.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.generator.entity.AgileGeneratorTableColumn;

/**
 * @author JeeAgile
 * @date 2021-06-21
 * @description
 */
@AgileMapper
@InterceptorIgnore(tenantLine = "true")
public interface AgileGeneratorTableColumnMapper extends AgileBaseMapper<AgileGeneratorTableColumn> {

}
