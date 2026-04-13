package com.jeeagile.process.mapper;

import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.process.entity.AgileProcessOrderScope;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JeeAgile
 * @description 工单权限关联表 Mapper
 */
@AgileMapper
public interface AgileProcessOrderScopeMapper extends AgileBaseMapper<AgileProcessOrderScope> {
    /**
     * 批量插入权限
     *
     * @param list 权限列表
     */
    void batchInsert(@Param("list") List<AgileProcessOrderScope> list);

    /**
     * 根据工单ID删除权限
     *
     * @param orderId 工单ID
     */
    void deleteByOrderId(@Param("orderId") String orderId);

    /**
     * 根据工单ID查询权限列表
     *
     * @param orderId 工单ID
     * @return 权限列表
     */
    List<AgileProcessOrderScope> selectByOrderId(@Param("orderId") String orderId);
}
