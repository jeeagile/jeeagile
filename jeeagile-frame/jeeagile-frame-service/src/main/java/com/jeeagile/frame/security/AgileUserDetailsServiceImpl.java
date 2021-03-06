package com.jeeagile.frame.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.constants.AgileConstants;
import com.jeeagile.core.enums.AgileAuditStatus;
import com.jeeagile.core.enums.AgileEnableStatus;
import com.jeeagile.core.enums.AgileUserStatus;
import com.jeeagile.core.exception.AgileAuthException;
import com.jeeagile.core.exception.AgileBaseException;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.user.AgileBaseUser;
import com.jeeagile.core.security.userdetails.IAgileUserDetailsService;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.service.system.*;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.frame.util.AgileBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
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
    @Autowired
    private IAgileSysTenantService agileSysTenantService;

    @Override
    public AgileUserData getUserData(String loginName) {
        try {
            AgileSysUser agileSysUser = this.getAgileSysUser(loginName);
            if (agileSysUser == null) {
                throw new AgileAuthException("?????????" + loginName + "???????????????????????????");
            }
            this.checkAgileSysUser(agileSysUser);
            return getAgileUserData(agileSysUser);
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("???????????????????????????", ex);
            throw new AgileAuthException("???????????????????????????");
        }
    }

    @Override
    public AgileBaseUser getUserData(String loginName, String tenantId, String tenantSign) {
        AgileSysTenant agileSysTenant = agileSysTenantService.agileSysTenantInfo(tenantId);
        this.checkAgileSysTenant(agileSysTenant, tenantSign);
        AgileSecurityContext.putTenantId(agileSysTenant.getId());
        AgileUserData agileUserData = getUserData(loginName);
        agileUserData.setTenantId(agileSysTenant.getId());
        agileUserData.setTenantCode(agileSysTenant.getTenantCode());
        agileUserData.setTenantName(agileSysTenant.getTenantName());
        AgileSecurityContext.removeTenant();
        return agileUserData;
    }

    private void checkAgileSysTenant(AgileSysTenant agileSysTenant, String tenantSign) {
        if (agileSysTenant == null || agileSysTenant.isEmptyPk()) {
            throw new AgileFrameException(AgileResultCode.WARN_VALIDATE_PASSED, "??????????????????????????????");
        }
        if (!agileSysTenant.getTenantSign().equals(tenantSign)) {
            throw new AgileFrameException(AgileResultCode.WARN_VALIDATE_PASSED, "?????????????????????");
        }
        if (!AgileEnableStatus.ENABLE.getCode().equals(agileSysTenant.getEnableStatus())) {
            throw new AgileFrameException(AgileResultCode.WARN_VALIDATE_PASSED, "?????????????????????");
        }
        if (!AgileAuditStatus.PASS.getCode().equals(agileSysTenant.getAuditStatus())) {
            throw new AgileFrameException(AgileResultCode.WARN_VALIDATE_PASSED, "???????????????????????????????????????");
        }
        if (agileSysTenant.getExpirationDate() != null) {
            if (new Date().after(agileSysTenant.getExpirationDate())) {
                throw new AgileFrameException(AgileResultCode.WARN_VALIDATE_PASSED, "????????????????????????");
            }
        }

    }

    private AgileSysUser getAgileSysUser(String userName) {
        QueryWrapper<AgileSysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgileSysUser::getUserName, userName);
        return agileSysUserService.getOne(queryWrapper);
    }

    private void checkAgileSysUser(AgileSysUser agileSysUser) {
        if (agileSysUser == null) {
            throw new AgileAuthException(AgileResultCode.FAIL_USER_DISABLE, "??????????????????????????????");
        }
        if (!AgileUserStatus.NORMAL.getCode().equals(agileSysUser.getUserStatus())) {
            throw new AgileAuthException(AgileResultCode.FAIL_USER_DISABLE, "??????????????????");
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
    public List<String> getUserPermList(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                //???????????????????????????????????????????????????
                if (agileBaseUser.isSuperAdmin() || agileBaseUser.getUserName().equals(agileBaseUser.getTenantCode())) {
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
            log.error("?????????????????????????????????", ex);
            throw new AgileAuthException("?????????????????????????????????");
        }
    }

    @Override
    public List<String> getUserRoleList(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                if (agileBaseUser.isSuperAdmin() || agileBaseUser.getUserName().equals(agileBaseUser.getTenantCode())) {
                    List<String> userRoleList = new ArrayList<>();
                    userRoleList.add("admin");
                    return userRoleList;
                } else {
                    return this.getUserRoleCodeList(agileBaseUser.getUserId());
                }
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("?????????????????????????????????", ex);
            throw new AgileAuthException("?????????????????????????????????");
        }
    }

    @Override
    public List getUserMenuList(AgileBaseUser agileBaseUser) {
        try {
            if (agileBaseUser != null) {
                if (agileBaseUser.isSuperAdmin() || agileBaseUser.getUserName().equals(agileBaseUser.getTenantCode())) {
                    return this.getAdminMenu();
                } else {
                    return this.getUserMenuByUserId(agileBaseUser.getUserId());
                }
            } else {
                throw new AgileAuthException(AgileResultCode.FAIL_USER_INFO);
            }
        } catch (AgileBaseException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("?????????????????????????????????", ex);
            throw new AgileAuthException("?????????????????????????????????", ex);
        }
    }

    @Override
    public List<String> getUserDataScopeList(AgileBaseUser agileBaseUser) {
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
            log.error("?????????????????????????????????", ex);
            throw new AgileAuthException("?????????????????????????????????");
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
            log.error("?????????????????????????????????", ex);
            throw new AgileAuthException("?????????????????????????????????");
        }
    }

    /**
     * ??????????????????????????????
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
     * ?????????????????????????????????
     *
     * @return
     */
    private List<AgileSysMenu> getAdminMenu() {
        LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysMenu::getMenuStatus, "0");
        lambdaQueryWrapper.in(AgileSysMenu::getMenuType, "M", "C");
        lambdaQueryWrapper.orderByAsc(AgileSysMenu::getParentId, AgileSysMenu::getMenuSort);
        return agileSysMenuService.list(lambdaQueryWrapper);
    }

    /**
     * ??????????????????????????????
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
     * ???????????????????????????
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
     * ????????????????????????ID??????
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
     * ??????????????????ID??????
     *
     * @param userId
     * @return
     */
    private List<String> getUserRoleCodeList(String userId) {
        List<String> userRoleCodeList = new ArrayList<>();
        List<AgileSysRole> agileSysRoleList = this.getUserRoleList(userId);
        agileSysRoleList.forEach(agileSysRole -> {
            if (agileSysRole.getRoleStatus().equals("0")) {
                userRoleCodeList.add(agileSysRole.getRoleCode());
            }
        });
        return userRoleCodeList;
    }

    /**
     * ??????????????????ID??????
     *
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
     * ??????????????????????????????
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
