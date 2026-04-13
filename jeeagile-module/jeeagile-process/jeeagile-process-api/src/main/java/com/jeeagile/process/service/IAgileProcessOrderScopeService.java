package com.jeeagile.process.service;

import com.jeeagile.frame.service.IAgileBaseService;
import com.jeeagile.process.entity.AgileProcessOrderScope;

import java.util.List;
import java.util.Set;

/**
 * @author JeeAgile
 * @description 工单权限关联服务接口
 */
public interface IAgileProcessOrderScopeService extends IAgileBaseService<AgileProcessOrderScope> {
    /**
     * 保存工单权限（流程启动时调用）
     *
     * @param orderId          工单ID
     * @param candidateUsers   候选用户集合
     * @param candidateGroups  候选组集合(dept:xxx, role:xxx, post:xxx)
     */
    void saveOrderScope(String orderId, Set<String> candidateUsers, Set<String> candidateGroups);

    /**
     * 获取当前用户可查看的工单ID列表
     *
     * @return 工单ID列表
     */
    List<String> getUserVisibleOrderIds();

    /**
     * 根据工单ID查询权限列表
     *
     * @param orderId 工单ID
     * @return 权限列表
     */
    List<AgileProcessOrderScope> selectByOrderId(String orderId);

    /**
     * 根据工单ID删除权限
     *
     * @param orderId 工单ID
     */
    void deleteByOrderId(String orderId);
}
