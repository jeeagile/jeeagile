package com.jeeagile.frame.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.enums.AgileUserStatus;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.service.system.*;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.frame.util.AgileBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileUserDetailsServiceImpl implements IAgileUserDetailsService {

    @Autowired
    private IAgileSysUserService agileSysUserService;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysRoleMenuService agileSysRoleMenuService;
    @Autowired
    private IAgileSysMenuService agileSysMenuService;
    @Autowired
    private IAgileSysDeptService agileSysDeptService;
    @Autowired
    private IAgileSysRoleDeptService agileSysRoleDeptService;

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
        if (!AgileUserStatus.NORMAL.getCode().equals(agileSysUser.getUserStatus())) {
            throw new AgileAuthException(AgileResultCode.FAIL_USER_DISABLE, "用户已停用！");
        }
    }

    private AgileUserData getAgileUserData(AgileSysUser agileSysUser) {
        AgileUserData agileUserData = new AgileUserData();
        AgileBeanUtils.copyProperties(agileSysUser, agileUserData);
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
                    return this.getUserPermByUserId(agileBaseUser.getUserId());
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
                    return this.getUserRoleIdList(agileBaseUser.getUserId());
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
                    return this.getSuperAdminMenu();
                } else {
                    return this.getUserMenuByUserId(agileBaseUser.getUserId());
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

    @Override
    public List<String> getUserDataScope(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                List<String> userDataScopeList = new ArrayList<>();
                List<AgileSysRole> agileSysRoleList = this.getUserRoleList(agileBaseUser.getUserId());
                agileSysRoleList.forEach(agileSysRole -> {
                    if (agileSysRole.getRoleStatus().equals("0")
                            && !userDataScopeList.contains(agileSysRole.getDataScope())) {
                        userDataScopeList.add(agileSysRole.getDataScope());
                    }
                });
                return userDataScopeList;
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AgileAuthException("获取用户权限类型异常！");
        }
    }

    @Override
    public Set<String> getUserDeptScopeList(AgileBaseUser agileBaseUser, String dataScopeType) {
        try {
            if (agileBaseUser != null) {
                Set<String> userDeptList = new HashSet<>();
                if (AgileConstants.AGILE_DATA_SCOPE_03.equals(dataScopeType)) {
                    List<AgileSysDept> agileSysDeptList = agileSysDeptService.selectAllChild(agileBaseUser.getDeptId());
                    agileSysDeptList.forEach(agileSysDept -> userDeptList.add(agileSysDept.getId()));
                }
                if (AgileConstants.AGILE_DATA_SCOPE_05.equals(dataScopeType)) {
                    userDeptList.addAll(this.getUserDeptScopeList(agileBaseUser.getUserId()));
                }
                return userDeptList;
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AgileAuthException("获取用户部门权限异常！");
        }
    }

    /**
     * 获取用户部门权限列表
     *
     * @param userId
     * @return
     */
    private List<String> getUserDeptScopeList(String userId) {
        List<String> userRoleIdList = this.getUserRoleIdList(userId);
        QueryWrapper<AgileSysRoleDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct dept_id");
        queryWrapper.lambda().in(AgileSysRoleDept::getRoleId, userRoleIdList);
        return agileSysRoleDeptService.listObjs(queryWrapper).stream().map(deptId -> (String) deptId).collect(Collectors.toList());
    }


    /**
     * 获取超级管理员菜单列表
     *
     * @return
     */
    private List<AgileSysMenu> getSuperAdminMenu() {
        LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysMenu::getMenuStatus, "0");
        lambdaQueryWrapper.in(AgileSysMenu::getMenuType, "M", "C");
        lambdaQueryWrapper.orderByAsc(AgileSysMenu::getParentId, AgileSysMenu::getMenuSort);
        return agileSysMenuService.list(lambdaQueryWrapper);
    }

    /**
     * 获取用户分配菜单权限
     *
     * @param userId
     * @return
     */
    private List<String> getUserPermByUserId(String userId) {
        List<String> userPermList = new ArrayList<>();
        List<String> userMenuIdList = getUserMenuIdByUserId(userId);
        if (AgileCollectionUtil.isNotEmpty(userMenuIdList)) {
            LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysMenu::getMenuStatus, "0");
            lambdaQueryWrapper.in(AgileSysMenu::getId, userMenuIdList);
            List<AgileSysMenu> agileSysMenuList = agileSysMenuService.list(lambdaQueryWrapper);
            agileSysMenuList.forEach(agileSysMenu -> {
                if (AgileStringUtil.isNotEmpty(agileSysMenu.getMenuPerm())) {
                    userPermList.add(agileSysMenu.getMenuPerm());
                }
            });
        }
        return userPermList;
    }

    /**
     * 获取用户已分配菜单
     *
     * @param userId
     * @return
     */
    private List<AgileSysMenu> getUserMenuByUserId(String userId) {
        List<String> userMenuIdList = getUserMenuIdByUserId(userId);
        if (AgileCollectionUtil.isNotEmpty(userMenuIdList)) {
            LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysMenu::getMenuStatus, "0");
            lambdaQueryWrapper.in(AgileSysMenu::getId, userMenuIdList);
            lambdaQueryWrapper.eq(AgileSysMenu::getMenuStatus, "0");
            lambdaQueryWrapper.in(AgileSysMenu::getMenuType, "M", "C");
            lambdaQueryWrapper.orderByAsc(AgileSysMenu::getParentId, AgileSysMenu::getMenuSort);
            return agileSysMenuService.list(lambdaQueryWrapper);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取用户分配菜单ID列表
     *
     * @param userId
     * @return
     */
    private List<String> getUserMenuIdByUserId(String userId) {
        List<String> userMenuIdList = new ArrayList<>();
        List<String> userRoleIdList = getUserRoleIdList(userId);
        if (AgileCollectionUtil.isNotEmpty(userRoleIdList)) {
            LambdaQueryWrapper<AgileSysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(AgileSysRoleMenu::getRoleId, userRoleIdList);
            List<AgileSysRoleMenu> agileSysRoleMenuList = agileSysRoleMenuService.list(lambdaQueryWrapper);
            agileSysRoleMenuList.forEach(agileSysRoleMenu -> userMenuIdList.add(agileSysRoleMenu.getMenuId()));
        }
        return userMenuIdList;
    }

    /**
     * 获取用户角色ID列表
     * @param userId
     * @return
     */
    private List<String> getUserRoleIdList(String userId) {
        List<String> userRoleIdList = new ArrayList<>();
        List<AgileSysRole> agileSysRoleList = this.getUserRoleList(userId);
        agileSysRoleList.forEach(agileSysRole -> {
            if (agileSysRole.getRoleStatus().equals("0")) {
                userRoleIdList.add(agileSysRole.getId());
            }
        });
        return userRoleIdList;
    }

    /**
     * 获取用户分配角色对象
     *
     * @param userId
     * @return
     */
    private List<AgileSysRole> getUserRoleList(String userId) {
        List<String> userRoleIdList = agileSysUserRoleService.getUserRoleIdList(userId);
        if (AgileCollectionUtil.isNotEmpty(userRoleIdList)) {
            LambdaQueryWrapper<AgileSysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysRole::getRoleStatus, "0");
            lambdaQueryWrapper.in(AgileSysRole::getId, userRoleIdList);
            return agileSysRoleService.list(lambdaQueryWrapper);
        } else {
            return new ArrayList<>();
        }
    }
}
