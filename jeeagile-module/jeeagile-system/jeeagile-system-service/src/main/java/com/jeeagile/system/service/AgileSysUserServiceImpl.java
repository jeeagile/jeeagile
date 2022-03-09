package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.AgileUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.entity.AgileSysUserPost;
import com.jeeagile.system.entity.AgileSysUserRole;
import com.jeeagile.system.mapper.AgileSysUserMapper;
import com.jeeagile.system.vo.AgileUpdatePwd;
import com.jeeagile.system.vo.AgileUpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserServiceImpl extends AgileBaseServiceImpl<AgileSysUserMapper, AgileSysUser> implements IAgileSysUserService {

    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;
    @Autowired
    private IAgileSysConfigService agileSysConfigService;

    @Override
    public LambdaQueryWrapper<AgileSysUser> queryWrapper(AgileSysUser agileSysUser) {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysUser != null) {
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserName())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getNickName())) {
                lambdaQueryWrapper.like(AgileSysUser::getNickName, agileSysUser.getNickName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserEmail())) {
                lambdaQueryWrapper.like(AgileSysUser::getUserEmail, agileSysUser.getUserEmail());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserPhone())) {
                lambdaQueryWrapper.like(AgileSysUser::getUserPhone, agileSysUser.getUserPhone());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getDeptId())) {
                lambdaQueryWrapper.eq(AgileSysUser::getDeptId, agileSysUser.getDeptId());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserStatus())) {
                lambdaQueryWrapper.eq(AgileSysUser::getUserStatus, agileSysUser.getUserStatus());
            }
        }
        lambdaQueryWrapper.ne(AgileSysUser::getUserName, AgileUtil.getSuperAdmin());
        lambdaQueryWrapper.orderByAsc(AgileSysUser::getUserSort);
        return lambdaQueryWrapper;
    }

    @Override
    public AgileSysUser selectModel(Serializable userId) {
        AgileSysUser agileSysUser = this.getById(userId);
        List<AgileSysUserRole> sysUserRoleList = agileSysUserRoleService.selectListByUserId((String) userId);
        sysUserRoleList.forEach(sysUserRole ->
                agileSysUser.getRoleIdList().add(sysUserRole.getRoleId())
        );
        List<AgileSysUserPost> sysUserPostList = agileSysUserPostService.selectListByUserId((String) userId);
        sysUserPostList.forEach(sysUserPost ->
                agileSysUser.getPostIdList().add(sysUserPost.getPostId())
        );
        return agileSysUser;
    }

    @Override
    public AgileSysUser saveModel(AgileSysUser agileSysUser) {
        String userPwd = agileSysUser.getUserPwd();
        if (AgileStringUtil.isEmpty(userPwd)) {
            userPwd = agileSysConfigService.getDefaultPwd();
        }
        agileSysUser.setUserPwd(AgileSecurityUtil.encryptPassword(userPwd));
        this.validateData(agileSysUser);
        this.save(agileSysUser);
        this.saveUserRole(agileSysUser.getId(), agileSysUser.getRoleIdList());
        this.saveUserPost(agileSysUser.getId(), agileSysUser.getPostIdList());
        return agileSysUser;
    }

    @Override
    public boolean updateModel(AgileSysUser agileSysUser) {
        String userPwd = agileSysUser.getUserPwd();
        if (AgileStringUtil.isNotEmpty(userPwd)) {
            agileSysUser.setUserPwd(AgileSecurityUtil.encryptPassword(userPwd));
        }
        this.validateData(agileSysUser);
        this.updateById(agileSysUser);
        agileSysUserPostService.deleteByUserId(agileSysUser.getId());
        agileSysUserRoleService.deleteByUserId(agileSysUser.getId());
        this.saveUserRole(agileSysUser.getId(), agileSysUser.getRoleIdList());
        return this.saveUserPost(agileSysUser.getId(), agileSysUser.getPostIdList());
    }

    @Override
    public boolean deleteModel(Serializable userId) {
        agileSysUserPostService.deleteByUserId((String) userId);
        agileSysUserRoleService.deleteByUserId((String) userId);
        return this.removeById(userId);
    }

    @Override
    public boolean resetUserPwd(AgileUpdatePwd agileUpdatePwd) {
        String userPwd = agileUpdatePwd.getNewPwd();
        if (AgileStringUtil.isEmpty(userPwd)) {
            userPwd = agileSysConfigService.getDefaultPwd();
        }
        userPwd = AgileSecurityUtil.encryptPassword(userPwd);
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setId(agileUpdatePwd.getUserId());
        agileSysUser.setUserPwd(userPwd);
        return this.updateById(agileSysUser);
    }

    @Override
    public boolean changeUserStatus(AgileUpdateStatus agileUpdateStatus) {
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setId(agileUpdateStatus.getId());
        agileSysUser.setUserStatus(agileUpdateStatus.getStatus());
        return this.updateById(agileSysUser);
    }

    /**
     * 用户名不能与已存在的数据重复
     */
    private void validateData(AgileSysUser agileSysUser) {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysUser.getId() != null) {
            lambdaQueryWrapper.ne(AgileSysUser::getId, agileSysUser.getId());
        }
        lambdaQueryWrapper.and(queryWrapper ->
                queryWrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("用户名不能与已存在的数据重复！");
        }
    }


    /**
     * 保存用户岗位
     *
     * @param userId
     * @param postIdList
     * @return
     */
    private boolean saveUserPost(String userId, List<String> postIdList) {
        if (postIdList != null && !postIdList.isEmpty()) {
            List<AgileSysUserPost> agileSysUserPostList = new ArrayList<>();
            for (String postId : postIdList) {
                AgileSysUserPost agileSysUserPost = new AgileSysUserPost();
                agileSysUserPost.setUserId(userId);
                agileSysUserPost.setPostId(postId);
                agileSysUserPostList.add(agileSysUserPost);
            }
            return agileSysUserPostService.saveBatch(agileSysUserPostList);
        } else {
            return true;
        }
    }


    /**
     * 保存用户已分配的角色
     */
    private boolean saveUserRole(String userId, List<String> roleIdList) {
        if (roleIdList != null && !roleIdList.isEmpty()) {
            List<AgileSysUserRole> agileSysUserRoleList = new ArrayList<>();
            for (String roleId : roleIdList) {
                AgileSysUserRole agileSysUserRole = new AgileSysUserRole();
                agileSysUserRole.setUserId(userId);
                agileSysUserRole.setRoleId(roleId);
                agileSysUserRoleList.add(agileSysUserRole);
            }
            return agileSysUserRoleService.saveBatch(agileSysUserRoleList);
        } else {
            return true;
        }
    }
}
