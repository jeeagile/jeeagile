package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysUserServiceImpl extends AgileBaseServiceImpl<AgileSysUserMapper, AgileSysUser> implements IAgileSysUserService {

    @Autowired
    private IAgileSysDeptService agileSysDeptService;
    @Autowired
    private IAgileSysPostService agileSysPostService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;
    @Autowired
    private IAgileSysConfigService agileSysConfigService;

    @Override
    public Object initData() {
        Map initData = new HashMap();
        initData.put("deptList", agileSysDeptService.selectList());
        initData.put("postList", agileSysPostService.selectList());
        initData.put("roleList", agileSysRoleService.selectList());
        return initData;
    }

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
        agileSysUser.setRoleIdList(this.selectUserRoleIdList(userId));
        agileSysUser.setPostIdList(this.selectUserPostIdList(userId));
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
        this.saveUserRole(agileSysUser);
        this.saveUserPost(agileSysUser);
        return agileSysUser;
    }

    @Override
    public boolean updateModel(AgileSysUser agileSysUser) {
        //防止修改用户信息将用户密码进行修改
        agileSysUser.setUserPwd(null);
        this.validateData(agileSysUser);
        this.updateById(agileSysUser);
        this.deleteUserPost(agileSysUser.getId());
        this.deleteUserRole(agileSysUser.getId());
        this.saveUserRole(agileSysUser);
        return this.saveUserPost(agileSysUser);
    }

    @Override
    public boolean deleteModel(Serializable userId) {
        this.deleteUserPost(userId);
        this.deleteUserRole(userId);
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
     * 查询用户已分配角色ID
     *
     * @param userId
     * @return
     */
    private List<String> selectUserRoleIdList(Serializable userId) {
        LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserRole::getUserId, userId);
        lambdaQueryWrapper.select(AgileSysUserRole::getRoleId);
        List<AgileSysUserRole> userRoleList = agileSysUserRoleService.list(lambdaQueryWrapper);
        List<String> userRoleIdList = new ArrayList<>();
        userRoleList.forEach(sysUserRole -> userRoleIdList.add(sysUserRole.getRoleId()));
        return userRoleIdList;
    }

    /**
     * 查询用户已分配岗位ID
     *
     * @param userId
     * @return
     */
    private List<String> selectUserPostIdList(Serializable userId) {
        LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysUserPost::getUserId, userId);
        lambdaQueryWrapper.select(AgileSysUserPost::getPostId);
        List<AgileSysUserPost> userPostList = agileSysUserPostService.list(lambdaQueryWrapper);
        List<String> userPostIdList = new ArrayList<>();
        userPostList.forEach(sysUserRole -> userPostIdList.add(sysUserRole.getPostId()));
        return userPostIdList;
    }

    /**
     * 删除用户已分配岗位
     *
     * @param userId
     * @return
     */
    private boolean deleteUserPost(Serializable userId) {
        if (AgileStringUtil.isNotEmpty(userId)) {
            LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysUserPost::getUserId, userId);
            return agileSysUserPostService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 删除用户已分配角色
     *
     * @param userId
     * @return
     */
    private boolean deleteUserRole(Serializable userId) {
        if (AgileStringUtil.isNotEmpty(userId)) {
            LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysUserRole::getUserId, userId);
            return agileSysUserRoleService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
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
     * @param agileSysUser
     * @return
     */
    private boolean saveUserPost(AgileSysUser agileSysUser) {
        if (AgileCollectionUtil.isNotEmpty(agileSysUser.getPostIdList())) {
            List<AgileSysUserPost> agileSysUserPostList = new ArrayList<>();
            for (String postId : agileSysUser.getPostIdList()) {
                AgileSysUserPost agileSysUserPost = new AgileSysUserPost();
                agileSysUserPost.setUserId(agileSysUser.getId());
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
    private boolean saveUserRole(AgileSysUser agileSysUser) {
        if (AgileCollectionUtil.isNotEmpty(agileSysUser.getRoleIdList())) {
            List<AgileSysUserRole> agileSysUserRoleList = new ArrayList<>();
            for (String roleId : agileSysUser.getRoleIdList()) {
                AgileSysUserRole agileSysUserRole = new AgileSysUserRole();
                agileSysUserRole.setUserId(agileSysUser.getId());
                agileSysUserRole.setRoleId(roleId);
                agileSysUserRoleList.add(agileSysUserRole);
            }
            return agileSysUserRoleService.saveBatch(agileSysUserRoleList);
        } else {
            return true;
        }
    }
}
