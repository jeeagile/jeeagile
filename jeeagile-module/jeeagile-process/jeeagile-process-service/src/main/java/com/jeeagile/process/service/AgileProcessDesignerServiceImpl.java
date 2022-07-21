package com.jeeagile.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.system.IAgileSysDeptService;
import com.jeeagile.frame.service.system.IAgileSysPostService;
import com.jeeagile.frame.service.system.IAgileSysRoleService;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import com.jeeagile.process.entity.AgileProcessScript;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-06-07
 * @description 流程设计
 */
@AgileService
public class AgileProcessDesignerServiceImpl implements IAgileProcessDesignerService {

    @Autowired
    private IAgileSysUserService agileSysUserService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysDeptService agileSysDeptService;
    @Autowired
    private IAgileSysPostService agileSysPostService;

    @Override
    public AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable) {
        AgilePage<AgileSysUser> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysUser::getId, AgileSysUser::getUserName, AgileSysUser::getNickName);
        AgileSysUser agileSysUser = agilePageable.getQueryCond();
        if (agileSysUser != null) {
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserName())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getNickName())) {
                lambdaQueryWrapper.like(AgileSysUser::getNickName, agileSysUser.getNickName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysUser::getUserStatus, "0");
        lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileUtil.getSuperAdmin());
        lambdaQueryWrapper.orderByAsc(AgileSysUser::getUserSort);
        return agileSysUserService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysRole> selectRolePage(AgilePageable<AgileSysRole> agilePageable) {
        AgilePage<AgileSysRole> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysRole::getId, AgileSysRole::getRoleCode, AgileSysRole::getRoleName);
        AgileSysRole agileSysRole = agilePageable.getQueryCond();
        if (agileSysRole != null) {
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleCode())) {
                lambdaQueryWrapper.eq(AgileSysRole::getRoleCode, agileSysRole.getRoleCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleName())) {
                lambdaQueryWrapper.like(AgileSysRole::getRoleName, agileSysRole.getRoleName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysRole::getRoleStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileSysRole::getRoleSort);
        return agileSysRoleService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysDept> selectDeptPage(AgilePageable<AgileSysDept> agilePageable) {
        AgilePage<AgileSysDept> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysDept::getId, AgileSysDept::getDeptCode, AgileSysDept::getDeptName);
        AgileSysDept agileSysDept = agilePageable.getQueryCond();
        if (agileSysDept != null) {
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptCode())) {
                lambdaQueryWrapper.eq(AgileSysDept::getDeptCode, agileSysDept.getDeptCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysDept.getDeptName())) {
                lambdaQueryWrapper.like(AgileSysDept::getDeptName, agileSysDept.getDeptName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysDept::getDeptStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileSysDept::getParentId, AgileSysDept::getDeptSort);
        return agileSysDeptService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysPost> selectPostPage(AgilePageable<AgileSysPost> agilePageable) {
        AgilePage<AgileSysPost> agilePage = new AgilePage<>(agilePageable.getCurrentPage(), agilePageable.getPageSize());
        LambdaQueryWrapper<AgileSysPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(AgileSysPost::getId, AgileSysPost::getPostCode, AgileSysPost::getPostName);
        AgileSysPost agileSysPost = agilePageable.getQueryCond();
        if (agileSysPost != null) {
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostCode())) {
                lambdaQueryWrapper.eq(AgileSysPost::getPostCode, agileSysPost.getPostCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysPost.getPostName())) {
                lambdaQueryWrapper.like(AgileSysPost::getPostName, agileSysPost.getPostName());
            }
        }
        lambdaQueryWrapper.eq(AgileSysPost::getPostStatus, "0");
        lambdaQueryWrapper.orderByAsc(AgileSysPost::getPostSort);
        return agileSysPostService.page(agilePage, lambdaQueryWrapper);
    }

    @Override
    public AgilePage<AgileSysGroup> selectGroupPage(AgilePageable<AgileSysGroup> agilePageable) {
        return null;
    }

    @Override
    public AgilePage<AgileProcessScript> selectScriptPage(AgilePageable<AgileProcessScript> agilePageable) {
        return null;
    }

    @Override
    public List<String> detailUserNickName(List<String> userIds) {
        List<String> nickNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(userIds)) {
            userIds.forEach(id -> {
                AgileSysUser agileSysUser = agileSysUserService.getById(id);
                if (agileSysUser != null && agileSysUser.isNotEmptyPk()) {
                    nickNameList.add(agileSysUser.getNickName());
                }
            });
        }
        return nickNameList;
    }

    @Override
    public List<String> detailRoleName(List<String> roleIds) {
        List<String> roleNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(roleIds)) {
            roleIds.forEach(id -> {
                AgileSysRole agileSysRole = agileSysRoleService.getById(id);
                if (agileSysRole != null && agileSysRole.isNotEmptyPk()) {
                    roleNameList.add(agileSysRole.getRoleName());
                }
            });
        }
        return roleNameList;
    }

    @Override
    public List<String> detailDeptName(List<String> deptIds) {
        List<String> deptNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(deptIds)) {
            deptIds.forEach(id -> {
                AgileSysDept agileSysDept = agileSysDeptService.getById(id);
                if (agileSysDept != null && agileSysDept.isNotEmptyPk()) {
                    deptNameList.add(agileSysDept.getDeptName());
                }
            });
        }
        return deptNameList;
    }

    @Override
    public List<String> detailPostName(List<String> postIds) {
        List<String> postNameList = new ArrayList<>();
        if (AgileCollectionUtil.isNotEmpty(postIds)) {
            postIds.forEach(id -> {
                AgileSysPost agileSysPost = agileSysPostService.getById(id);
                if (agileSysPost != null && agileSysPost.isNotEmptyPk()) {
                    postNameList.add(agileSysPost.getPostName());
                }
            });
        }
        return postNameList;
    }

    @Override
    public List<String> detailGroupName(List<String> groupCodes) {
        return null;
    }

    @Override
    public List<String> detailScriptName(List<String> scriptCodes) {
        return null;
    }
}
