package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.vo.system.AgilePersonInfo;
import com.jeeagile.frame.vo.system.AgileSysPerson;
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
public class AgileSysPersonServiceImpl implements IAgileSysPersonService {
    @Autowired
    private IAgileSysUserService agileSysUserService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysPostService agileSysPostService;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;


    @Override
    public AgileSysPerson getAgileSysPerson() {
        AgileSysPerson agileSysPerson = new AgileSysPerson();
        AgileBaseUser userData = AgileSecurityContext.getUserData();
        AgileSysUser agileSysUser = agileSysUserService.getById(userData.getUserId());
        if (agileSysUser != null && AgileStringUtil.isNotEmpty(agileSysUser.getId())) {
            BeanUtils.copyProperties(agileSysUser, agileSysPerson);
            agileSysPerson.setDeptName(userData.getDeptName());
            agileSysPerson.setRoleNameList(this.getRoleNameByUserId(agileSysUser.getId()));
            agileSysPerson.setPostNameList(this.getPostNameByUserId(agileSysUser.getId()));
        }
        return agileSysPerson;
    }

    @Override
    public boolean updatePersonInfo(AgilePersonInfo agilePersonInfo) {
        AgileSysUser agileSysUser = new AgileSysUser();
        BeanUtils.copyProperties(agilePersonInfo, agileSysUser);
        agileSysUser.setId(AgileSecurityContext.getUserId());
        return agileSysUserService.updateById(agileSysUser);
    }

    @Override
    public boolean updatePersonPassword(String oldPwd, String newPwd) {
        AgileSysUser oldSysUser = agileSysUserService.getById(AgileSecurityContext.getUserId());
        String oldUserPwd = AgileSecurityUtil.encryptPassword(oldPwd);
        String newUserPwd = AgileSecurityUtil.encryptPassword(newPwd);
        if (oldSysUser.getUserPwd().equals(oldUserPwd)) {
            AgileSysUser newSysUser = new AgileSysUser();
            newSysUser.setId(oldSysUser.getId());
            newSysUser.setUserPwd(newUserPwd);
            return agileSysUserService.updateById(newSysUser);
        } else {
            throw new AgileValidateException("输入的老密码有误，请确认！");
        }
    }

    @Override
    public boolean updatePersonAvatar(String userAvatar) {
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setId(AgileSecurityContext.getUserId());
        agileSysUser.setUserAvatar(userAvatar);
        return agileSysUserService.updateById(agileSysUser);
    }

    /**
     * 获取用户分配角色名称
     *
     * @param userId
     * @return
     */
    private List<String> getRoleNameByUserId(String userId) {
        List<String> userRoleNameList = new ArrayList<>();
        List<String> userRoleIdList = agileSysUserRoleService.getUserRoleIdList(userId);
        if (AgileCollectionUtil.isNotEmpty(userRoleIdList)) {
            LambdaQueryWrapper<AgileSysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysRole::getRoleStatus, "0");
            lambdaQueryWrapper.in(AgileSysRole::getId, userRoleIdList);
            List<AgileSysRole> agileSysRoleList = agileSysRoleService.list(lambdaQueryWrapper);
            agileSysRoleList.forEach(item -> userRoleNameList.add(item.getRoleName()));
        }
        return userRoleNameList;
    }


    /**
     * 获取用户分配岗位名称
     *
     * @param userId
     * @return
     */
    private List<String> getPostNameByUserId(String userId) {
        List<String> userPostNameList = new ArrayList<>();
        List<String> userPostIdList = agileSysUserPostService.getUserPostIdList(userId);
        if (AgileCollectionUtil.isNotEmpty(userPostIdList)) {
            LambdaQueryWrapper<AgileSysPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysPost::getPostStatus, "0");
            lambdaQueryWrapper.in(AgileSysPost::getId, userPostIdList);
            List<AgileSysPost> agileSysRoleList = agileSysPostService.list(lambdaQueryWrapper);
            agileSysRoleList.forEach(item -> userPostNameList.add(item.getPostName()));
        }
        return userPostNameList;
    }


}
