package com.jeeagile.process.service;

import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.frame.service.system.IAgileSysUserPostService;
import com.jeeagile.frame.service.system.IAgileSysUserRoleService;
import com.jeeagile.process.entity.AgileProcessOrderScope;
import com.jeeagile.process.mapper.AgileProcessOrderMapper;
import com.jeeagile.process.mapper.AgileProcessOrderScopeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author JeeAgile
 * @description 工单权限关联服务实现
 */
@AgileService
public class AgileProcessOrderScopeServiceImpl extends AgileBaseServiceImpl<AgileProcessOrderScopeMapper, AgileProcessOrderScope> implements IAgileProcessOrderScopeService {

    @Autowired
    private AgileProcessOrderMapper agileProcessOrderMapper;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;

    @Override
    public void saveOrderScope(String orderId, Set<String> candidateUsers, Set<String> candidateGroups) {
        if (orderId == null) {
            return;
        }
        List<AgileProcessOrderScope> scopeList = new ArrayList<>();
        Date createTime = new Date();

        // 解析候选用户
        if (AgileCollectionUtil.isNotEmpty(candidateUsers)) {
            candidateUsers.forEach(userId -> {
                AgileProcessOrderScope scope = new AgileProcessOrderScope();
                scope.setOrderId(orderId);
                scope.setScopeType("user");
                scope.setScopeId(userId);
                scope.setCreateTime(createTime);
                scopeList.add(scope);
            });
        }

        // 解析候选组(dept:xxx, role:xxx, post:xxx)
        if (AgileCollectionUtil.isNotEmpty(candidateGroups)) {
            candidateGroups.forEach(group -> {
                String[] parts = group.split(":");
                if (parts.length == 2) {
                    AgileProcessOrderScope scope = new AgileProcessOrderScope();
                    scope.setOrderId(orderId);
                    scope.setScopeType(parts[0]);  // dept/role/post
                    scope.setScopeId(parts[1]);
                    scope.setCreateTime(createTime);
                    scopeList.add(scope);
                }
            });
        }

        if (AgileCollectionUtil.isNotEmpty(scopeList)) {
            baseMapper.batchInsert(scopeList);
        }
    }

    @Override
    public List<String> getUserVisibleOrderIds() {
        AgileBaseUser agileBaseUser = AgileSecurityContext.getUserData();
        if (agileBaseUser == null) {
            return new ArrayList<>();
        }

        String userId = agileBaseUser.getUserId();
        String deptId = agileBaseUser.getDeptId();
        List<String> roleIds = agileSysUserRoleService.getUserRoleIdList(userId);
        List<String> postIds = agileSysUserPostService.getUserPostIdList(userId);

        return agileProcessOrderMapper.selectOrderIdsByScope(userId, deptId, roleIds, postIds);
    }

    @Override
    public List<AgileProcessOrderScope> selectByOrderId(String orderId) {
        return baseMapper.selectByOrderId(orderId);
    }

    @Override
    public void deleteByOrderId(String orderId) {
        baseMapper.deleteByOrderId(orderId);
    }
}
