package com.jeeagile.system.service;

import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.mapper.AgileSysPersonMapper;
import com.jeeagile.system.vo.AgilePersonInfo;
import com.jeeagile.system.vo.AgileSysPerson;
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
    public AgileSysPerson getAgileSysPerson() {
        AgileSysPerson agileSysPerson = new AgileSysPerson();
        AgileBaseUser userData = AgileSecurityContext.getUserData();
        AgileSysUser agileSysUser = agileSysUserService.getById(userData.getUserId());
        if (agileSysUser != null && AgileStringUtil.isNotEmpty(agileSysUser.getId())) {
            BeanUtils.copyProperties(agileSysUser, agileSysPerson);
            agileSysPerson.setDeptName(userData.getDeptName());
            List<String> roleNameList = agileSysPersonMapper.getRoleNameByUserId(agileSysUser.getId());
            List<String> postNameList = agileSysPersonMapper.getPostNameByUserId(agileSysUser.getId());
            agileSysPerson.setRoleNameList(roleNameList);
            agileSysPerson.setPostNameList(postNameList);
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

}
