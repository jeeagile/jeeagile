package com.jeeagile.system.service;

import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.system.mapper.AgileSysPersonMapper;
import com.jeeagile.system.vo.AgilePersonInfo;
import com.jeeagile.system.vo.AgileUpdatePerson;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.vo.AgileUpdatePwd;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
    private AgileSysPersonMapper agileSysPersonMapper;

    @Override
    public AgilePersonInfo getPersonInfo() {
        AgilePersonInfo agilePersonInfo = new AgilePersonInfo();
        AgileBaseUser userData = getCurrentUserData();
        AgileSysUser agileSysUser = agileSysUserService.getById(userData.getUserId());
        if (agileSysUser != null && AgileStringUtil.isNotEmpty(agileSysUser.getId())) {
            BeanUtils.copyProperties(agileSysUser, agilePersonInfo);
            agilePersonInfo.setDeptName(userData.getDeptName());
            List<String> roleNameList = agileSysPersonMapper.getRoleNameByUserId(agileSysUser.getId());
            List<String> postNameList = agileSysPersonMapper.getPostNameByUserId(agileSysUser.getId());
            agilePersonInfo.setRoleNameList(roleNameList);
            agilePersonInfo.setPostNameList(postNameList);
        }
        return agilePersonInfo;
    }

    @Override
    public boolean updatePersonInfo(AgileUpdatePerson agileUpdatePerson) {
        AgileBaseUser userData = getCurrentUserData();
        AgileSysUser agileSysUser = new AgileSysUser();
        BeanUtils.copyProperties(agileUpdatePerson, agileSysUser);
        agileSysUser.setId(userData.getUserId());
        return agileSysUserService.updateById(agileSysUser);
    }

    @Override
    public boolean updatePersonPwd(AgileUpdatePwd agileUpdatePwd) {
        AgileBaseUser userData = getCurrentUserData();
        AgileSysUser oldSysUser = agileSysUserService.getById(userData.getUserId());
        String oldUserPwd = AgileSecurityUtil.encryptPassword(agileUpdatePwd.getOldPwd());
        String newUserPwd = AgileSecurityUtil.encryptPassword(agileUpdatePwd.getNewPwd());
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
    public boolean updateUserAvatar(String userAvatar) {
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setId(AgileSecurityContext.getCurrentUserId());
        agileSysUser.setUserAvatar(userAvatar);
        return agileSysUserService.updateById(agileSysUser);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    private AgileBaseUser getCurrentUserData() {
        AgileBaseUser userData = AgileSecurityContext.getCurrentUser();
        if (userData == null) {
            throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
        }
        return userData;
    }

}
