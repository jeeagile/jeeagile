package com.jeeagile.system.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.enums.AgileStatusEnum;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.mapper.AgileUserDetailsMapper;
import com.jeeagile.system.service.IAgileSysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileUserDetailsServiceImpl implements IAgileUserDetailsService {

    @Lazy
    @Autowired
    private IAgileSysUserService agileSysUserService;

    @Autowired
    private AgileUserDetailsMapper agileUserDetailsMapper;

    @Override
    public AgileBaseUser userLogin(String loginName, String userPassword) {
        try {
            AgileSysUser agileSysUser = this.getAgileSysUser(loginName);
            if (agileSysUser == null) {
                throw new AgileAuthException("用户《" + loginName + "》不存在，请核实！");
            }
            this.checkAgileSysUser(agileSysUser);
            String md5Password = AgileSecurityUtil.encryptPassword(userPassword);
            if (!md5Password.equals(agileSysUser.getUserPwd())) {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_PWD, "用户密码错误！");
            }
            return getAgileUserData(agileSysUser);
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("用户登录异常！");
        }
    }

    @Override
    public AgileUserData getUserDataByLoginName(String loginName) {
        try {
            AgileSysUser agileSysUser = this.getAgileSysUser(loginName);
            if (agileSysUser == null) {
                throw new AgileAuthException("用户《" + loginName + "》不存在，请核实！");
            }
            this.checkAgileSysUser(agileSysUser);
            return getAgileUserData(agileSysUser);
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("加载用户信息异常！");
        }
    }

    @Override
    public AgileBaseUser getUserDataByUserId(String userId) {
        try {
            AgileSysUser agileSysUser = agileSysUserService.getById(userId);
            this.checkAgileSysUser(agileSysUser);
            return getAgileUserData(agileSysUserService.getById(userId));
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("加载用户信息异常！");
        }
    }

    private AgileSysUser getAgileSysUser(String userName) {
        QueryWrapper<AgileSysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysUser::getUserName, userName);
        return agileSysUserService.getOne(queryWrapper);
    }

    private void checkAgileSysUser(AgileSysUser agileSysUser) {
        if (agileSysUser == null) {
            throw new AgileAuthException(AgileResultCode.FAIL_USER_DISABLE, "用户不存在，请核实！");
        }
        if (!AgileStatusEnum.NORMAL.getCode().equals(agileSysUser.getUserStatus())) {
            throw new AgileAuthException(AgileResultCode.FAIL_USER_DISABLE, "用户已停用！");
        }
    }

    private AgileUserData getAgileUserData(AgileSysUser agileSysUser) {
        AgileUserData agileUserData = new AgileUserData();
        BeanUtils.copyProperties(agileSysUser, agileUserData);
        agileUserData.setPassword(agileSysUser.getUserPwd());
        agileUserData.setUserId(agileSysUser.getId());
        return agileUserData;
    }

    @Override
    public List<String> getUserPerm(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                if (agileBaseUser.isSuperAdmin()) {
                    List<String> userPermList = new ArrayList<>();
                    userPermList.add("*:*:*");
                    return userPermList;
                } else {
                    return agileUserDetailsMapper.getUserPermByUserId(agileBaseUser.getUserId());
                }
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("加载用户权限信息异常！");
        }
    }

    @Override
    public List<String> getUserRole(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                if (agileBaseUser.isSuperAdmin()) {
                    List<String> userRoleList = new ArrayList<>();
                    userRoleList.add("admin");
                    return userRoleList;
                } else {
                    return agileUserDetailsMapper.getUserRoleByUserId(agileBaseUser.getUserId());
                }
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("加载用户角色信息异常！");
        }
    }

    @Override
    public List getUserMenu(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                if (agileBaseUser.isSuperAdmin()) {
                    return agileUserDetailsMapper.getSuperAdminMenu();
                } else {
                    return agileUserDetailsMapper.getUserMenuByUserId(agileBaseUser.getUserId());
                }
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("加载用户菜单信息异常！");
        }
    }
}
