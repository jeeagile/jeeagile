package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.entity.AgileSysUserPost;
import com.jeeagile.system.entity.AgileSysUserRole;
import com.jeeagile.system.mapper.AgileSysUserMapper;
import com.jeeagile.system.vo.AgileSysUserInfo;
import com.jeeagile.system.vo.AgileUpdatePwd;
import com.jeeagile.system.vo.AgileUpdateStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
    public AgilePage<AgileSysUser> selectUserPage(AgilePageable<AgileSysUser> agilePageable) {
        return this.page(agilePageable, getSysUserQueryWrapper(agilePageable.getQueryCond()));
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysUser> getSysUserQueryWrapper(AgileSysUser agileSysUser) {
        QueryWrapper<AgileSysUser> queryWrapper = new QueryWrapper<>();
        if (agileSysUser != null) {
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserName())) {
                queryWrapper.lambda().eq(AgileSysUser::getUserName, agileSysUser.getUserName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getNickName())) {
                queryWrapper.lambda().like(AgileSysUser::getNickName, agileSysUser.getNickName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserEmail())) {
                queryWrapper.lambda().like(AgileSysUser::getUserEmail, agileSysUser.getUserEmail());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserPhone())) {
                queryWrapper.lambda().like(AgileSysUser::getUserPhone, agileSysUser.getUserPhone());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getDeptId())) {
                queryWrapper.lambda().eq(AgileSysUser::getDeptId, agileSysUser.getDeptId());
            }
            if (AgileStringUtil.isNotEmpty(agileSysUser.getUserStatus())) {
                queryWrapper.lambda().eq(AgileSysUser::getUserStatus, agileSysUser.getUserStatus());
            }
        }
        queryWrapper.lambda().ne(AgileSysUser::getUserName, "admin");
        queryWrapper.lambda().orderByAsc(AgileSysUser::getUserSort);
        return queryWrapper;
    }

    @Override
    public AgileSysUserInfo selectUserById(String userId) {
        AgileSysUserInfo agileSysUserInfo = new AgileSysUserInfo();
        AgileSysUser agileSysUser = this.getById(userId);
        BeanUtils.copyProperties(agileSysUser, agileSysUserInfo);
        List<AgileSysUserRole> sysUserRoleList = agileSysUserRoleService.selectListByUserId(userId);
        sysUserRoleList.forEach(sysUserRole ->
                agileSysUserInfo.getRoleIdList().add(sysUserRole.getRoleId())
        );
        List<AgileSysUserPost> sysUserPostList = agileSysUserPostService.selectListByUserId(userId);
        sysUserPostList.forEach(sysUserPost ->
                agileSysUserInfo.getPostIdList().add(sysUserPost.getPostId())
        );
        return agileSysUserInfo;
    }

    @Override
    public AgileSysUserInfo saveUser(AgileSysUserInfo agileSysUserInfo) {
        String userPwd = agileSysUserInfo.getUserPwd();
        if (AgileStringUtil.isEmpty(userPwd)) {
            userPwd = agileSysConfigService.getDefaultPwd();
        }
        agileSysUserInfo.setUserPwd(AgileSecurityUtil.encryptPassword(userPwd));
        //校验业务数据
        AgileValidateUtil.validateObject(agileSysUserInfo);
        this.validateSysUser(agileSysUserInfo);
        this.save(agileSysUserInfo);
        this.saveUserRole(agileSysUserInfo.getId(), agileSysUserInfo.getRoleIdList());
        this.saveUserPost(agileSysUserInfo.getId(), agileSysUserInfo.getPostIdList());
        return agileSysUserInfo;
    }

    @Override
    public boolean updateUserById(AgileSysUserInfo agileSysUserInfo) {
        String userPwd = agileSysUserInfo.getUserPwd();
        if (AgileStringUtil.isNotEmpty(userPwd)) {
            agileSysUserInfo.setUserPwd(AgileSecurityUtil.encryptPassword(userPwd));
        }
        //校验业务数据
        AgileValidateUtil.validateObject(agileSysUserInfo);
        this.validateSysUser(agileSysUserInfo);
        this.updateById(agileSysUserInfo);
        agileSysUserPostService.deleteByUserId(agileSysUserInfo.getId());
        agileSysUserRoleService.deleteByUserId(agileSysUserInfo.getId());
        this.saveUserRole(agileSysUserInfo.getId(), agileSysUserInfo.getRoleIdList());
        return this.saveUserPost(agileSysUserInfo.getId(), agileSysUserInfo.getPostIdList());
    }

    @Override
    public boolean deleteUserById(String userId) {
        agileSysUserPostService.deleteByUserId(userId);
        agileSysUserRoleService.deleteByUserId(userId);
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
    private void validateSysUser(AgileSysUser agileSysUser) {
        LambdaQueryWrapper<AgileSysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (agileSysUser.getId() != null) {
            queryWrapper.ne(AgileSysUser::getId, agileSysUser.getId());
        }
        queryWrapper.and(wrapper ->
                wrapper.eq(AgileSysUser::getUserName, agileSysUser.getUserName())
        );
        if (this.count(queryWrapper) > 0) {
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
