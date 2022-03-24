package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileCollectionUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.entity.AgileSysRoleDept;
import com.jeeagile.system.entity.AgileSysRoleMenu;
import com.jeeagile.system.entity.AgileSysUserRole;
import com.jeeagile.system.mapper.AgileSysRoleMapper;
import com.jeeagile.system.vo.AgileUpdateStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeeagile.core.constants.AgileConstants.AGILE_DATA_SCOPE_05;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileSysRoleServiceImpl extends AgileBaseServiceImpl<AgileSysRoleMapper, AgileSysRole> implements IAgileSysRoleService {

    @Autowired
    private IAgileSysMenuService agileSysMenuService;

    @Autowired
    private IAgileSysDeptService agileSysDeptService;

    @Autowired
    private IAgileSysRoleMenuService agileSysRoleMenuService;

    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;

    @Autowired
    private IAgileSysRoleDeptService agileSysRoleDeptService;

    @Override
    public Object initData() {
        Map initData = new HashMap();
        initData.put("deptList", agileSysDeptService.selectList());
        initData.put("menuList", agileSysMenuService.selectList());
        return initData;
    }

    @Override
    public LambdaQueryWrapper<AgileSysRole> queryWrapper(AgileSysRole agileSysRole) {
        LambdaQueryWrapper<AgileSysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysRole != null) {
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleName())) {
                lambdaQueryWrapper.like(AgileSysRole::getRoleName, agileSysRole.getRoleName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleCode())) {
                lambdaQueryWrapper.like(AgileSysRole::getRoleCode, agileSysRole.getRoleCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleStatus())) {
                lambdaQueryWrapper.eq(AgileSysRole::getRoleStatus, agileSysRole.getRoleStatus());
            }
        }
        lambdaQueryWrapper.orderByAsc(AgileSysRole::getRoleSort);
        return lambdaQueryWrapper;
    }

    @Override
    public AgileSysRole selectModel(Serializable roleId) {
        AgileSysRole agileSysRole = this.getById(roleId);
        agileSysRole.setMenuIdList(this.selectRoleMenuIdList(roleId));
        if (AGILE_DATA_SCOPE_05.equals(agileSysRole.getDataScope())) {
            agileSysRole.setDeptIdList(this.selectRoleDeptIdList(roleId));
        }
        return agileSysRole;
    }

    @Override
    public AgileSysRole saveModel(AgileSysRole agileSysRole) {
        this.save(agileSysRole);
        this.saveRoleMenu(agileSysRole);
        return agileSysRole;
    }

    @Override
    public boolean updateModel(AgileSysRole agileSysRole) {
        this.updateById(agileSysRole);
        this.deleteRoleMenu(agileSysRole.getId());
        this.saveRoleMenu(agileSysRole);
        return true;
    }

    @Override
    public boolean deleteModel(Serializable roleId) {
        this.deleteRoleMenu(roleId);
        this.deleteRoleDept(roleId);
        this.deleteUserRole(roleId);
        return this.removeById(roleId);
    }

    @Override
    public boolean changeRoleStatus(AgileUpdateStatus agileUpdateStatus) {
        AgileSysRole agileSysRole = new AgileSysRole();
        agileSysRole.setId(agileUpdateStatus.getId());
        agileSysRole.setRoleStatus(agileUpdateStatus.getStatus());
        return this.updateById(agileSysRole);
    }

    @Override
    public boolean updateRoleDataScope(AgileSysRole agileSysRole) {
        agileSysRole.setId(agileSysRole.getId());
        agileSysRole.setDataScope(agileSysRole.getDataScope());
        this.updateById(agileSysRole);
        this.deleteRoleDept(agileSysRole.getId());
        if (AGILE_DATA_SCOPE_05.equals(agileSysRole.getDataScope())) {
            this.saveRoleDept(agileSysRole);
        }
        return true;
    }

    /**
     * 查询角色已分配菜单
     *
     * @param roleId
     * @return
     */
    private List<String> selectRoleMenuIdList(Serializable roleId) {
        LambdaQueryWrapper<AgileSysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysRoleMenu::getRoleId, roleId);
        List<AgileSysRoleMenu> roleMenuList = agileSysRoleMenuService.list(lambdaQueryWrapper);
        List<String> roleMenuIdList = new ArrayList<>();
        for (AgileSysRoleMenu sysRoleMenu : roleMenuList) {
            if (agileSysMenuService.countChild(sysRoleMenu.getMenuId()) < 1) {
                roleMenuIdList.add(sysRoleMenu.getMenuId());
            }
        }
        return roleMenuIdList;
    }

    /**
     * 查询角色已分配数据权限
     *
     * @param roleId
     * @return
     */
    private List<String> selectRoleDeptIdList(Serializable roleId) {
        LambdaQueryWrapper<AgileSysRoleDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgileSysRoleDept::getRoleId, roleId);
        List<AgileSysRoleDept> roleDeptList = agileSysRoleDeptService.list(lambdaQueryWrapper);
        List<String> roleDeptIdList = new ArrayList<>();
        for (AgileSysRoleDept sysRoleDept : roleDeptList) {
            if (agileSysDeptService.countChild(sysRoleDept.getDeptId()) < 1) {
                roleDeptIdList.add(sysRoleDept.getDeptId());
            }
        }
        return roleDeptIdList;
    }

    /**
     * 删除角色已分配菜单
     *
     * @param roleId
     * @return
     */
    private boolean deleteRoleMenu(Serializable roleId) {
        if (AgileStringUtil.isNotEmpty(roleId)) {
            LambdaQueryWrapper<AgileSysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysRoleMenu::getRoleId, roleId);
            return agileSysRoleMenuService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 删除用户已分配角色
     *
     * @param roleId
     * @return
     */
    private boolean deleteUserRole(Serializable roleId) {
        if (AgileStringUtil.isNotEmpty(roleId)) {
            LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysUserRole::getRoleId, roleId);
            return agileSysUserRoleService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 删除角色已分配部门数据权限
     *
     * @param roleId
     * @return
     */
    private boolean deleteRoleDept(Serializable roleId) {
        if (AgileStringUtil.isNotEmpty(roleId)) {
            LambdaQueryWrapper<AgileSysRoleDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgileSysRoleDept::getRoleId, roleId);
            return agileSysRoleDeptService.remove(lambdaQueryWrapper);
        } else {
            return true;
        }
    }

    /**
     * 保存角色菜单数据
     *
     * @param agileSysRole
     * @return
     */
    private boolean saveRoleMenu(AgileSysRole agileSysRole) {
        if (AgileCollectionUtil.isNotEmpty(agileSysRole.getMenuIdList())) {
            List<AgileSysRoleMenu> agileSysRoleMenuList = new ArrayList<>();
            for (String menuId : agileSysRole.getMenuIdList()) {
                AgileSysRoleMenu agileSysRoleMenu = new AgileSysRoleMenu();
                agileSysRoleMenu.setRoleId(agileSysRole.getId());
                agileSysRoleMenu.setMenuId(menuId);
                agileSysRoleMenuList.add(agileSysRoleMenu);
            }
            return agileSysRoleMenuService.saveBatch(agileSysRoleMenuList);
        } else {
            return true;
        }
    }

    /**
     * 保存角色部门数据
     *
     * @param agileSysRole
     * @return
     */
    private boolean saveRoleDept(AgileSysRole agileSysRole) {
        if (AgileCollectionUtil.isNotEmpty(agileSysRole.getDeptIdList())) {
            List<AgileSysRoleDept> agileSysRoleDeptList = new ArrayList<>();
            for (String deptId : agileSysRole.getDeptIdList()) {
                AgileSysRoleDept agileSysRoleDept = new AgileSysRoleDept();
                agileSysRoleDept.setRoleId(agileSysRole.getId());
                agileSysRoleDept.setDeptId(deptId);
                agileSysRoleDeptList.add(agileSysRoleDept);
            }
            return agileSysRoleDeptService.saveBatch(agileSysRoleDeptList);
        } else {
            return true;
        }
    }
}
