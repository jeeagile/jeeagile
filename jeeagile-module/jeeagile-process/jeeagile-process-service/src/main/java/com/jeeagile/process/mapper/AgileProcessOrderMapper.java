package com.jeeagile.process.mapper;

import com.jeeagile.frame.annotation.AgileMapper;
import com.jeeagile.frame.mapper.AgileBaseMapper;
import com.jeeagile.process.entity.AgileProcessOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-06-14
 * @description 流程实例
 */
@AgileMapper
public interface AgileProcessOrderMapper extends AgileBaseMapper<AgileProcessOrder> {
    /**
     * 查询用户有权限的工单ID列表（基于权限关联表）
     *
     * @param userId  用户ID
     * @param deptId  部门ID
     * @param roleIds 角色ID列表
     * @param postIds 岗位ID列表
     * @return 工单ID列表
     */
    List<String> selectOrderIdsByScope(@Param("userId") String userId,
                                       @Param("deptId") String deptId,
                                       @Param("roleIds") List<String> roleIds,
                                       @Param("postIds") List<String> postIds);
}
