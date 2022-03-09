package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.entity.AgileSysRoleDept;
import com.jeeagile.system.entity.AgileSysRoleMenu;
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
    private IAgileSysRoleMenuService agileSysRoleMenuService;

    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;

    @Autowired
    private IAgileSysDeptService agileSysDeptService;

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
            //角色名称
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleName())) {
                lambdaQueryWrapper.like(AgileSysRole::getRoleName, agileSysRole.getRoleName());
            }
            //角色编码
            if (AgileStringUtil.isNotEmpty(agileSysRole.getRoleCode())) {
                lambdaQueryWrapper.like(AgileSysRole::getRoleCode, agileSysRole.getRoleCode());
            }
            //角色状态
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
        List<AgileSysRoleMenu> sysRoleMenuList = agileSysRoleMenuService.selectListByRoleId((String) roleId);
        for (AgileSysRoleMenu sysRoleMenu : sysRoleMenuList) {
            //只取叶子节点
            if (agileSysMenuService.countChild(sysRoleMenu.getMenuId()) < 1) {
                agileSysRole.getMenuIdList().add(sysRoleMenu.getMenuId());
            }
        }
        if (AGILE_DATA_SCOPE_05.equals(agileSysRole.getDataScope())) {
            List<AgileSysRoleDept> sysRoleDeptList = agileSysRoleDeptService.selectListByRoleId((String) roleId);
            for (AgileSysRoleDept sysRoleDept : sysRoleDeptList) {
                //只取叶子节点
                if (agileSysDeptService.countChild(sysRoleDept.getDeptId()) < 1) {
                    agileSysRole.getDeptIdList().add(sysRoleDept.getDeptId());
                }
            }
        }
        return agileSysRole;
    }

    @Override
    public AgileSysRole saveModel(AgileSysRole agileSysRole) {
        this.save(agileSysRole);
        this.saveRoleMenu(agileSysRole.getId(), agileSysRole.getMenuIdList());
        return agileSysRole;
    }

    @Override
    public boolean updateModel(AgileSysRole agileSysRole) {
        this.updateById(agileSysRole);
        agileSysRoleMenuService.deleteByRoleId(agileSysRole.getId());
        this.saveRoleMenu(agileSysRole.getId(), agileSysRole.getMenuIdList());
        return true;
    }

    @Override
    public boolean deleteModel(Serializable roleId) {
        //删除角色已分配菜单
        agileSysRoleMenuService.deleteByRoleId((String) roleId);
        //删除数据权限
        agileSysRoleDeptService.deleteByRoleId((String) roleId);
        //删除角色相关用户
        agileSysUserRoleService.deleteByRoleId((String) roleId);
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
        agileSysRoleDeptService.deleteByRoleId(agileSysRole.getId());
        if (AGILE_DATA_SCOPE_05.equals(agileSysRole.getDataScope())) {
            this.saveRoleDept(agileSysRole.getId(), agileSysRole.getDeptIdList());
        }
        return true;
    }

    /**
     * 拼装查询条件
     */

    /**
     * 保存角色菜单数据
     *
     * @param roleId
     * @param menuIdList
     * @return
     */
    private boolean saveRoleMenu(String roleId, List<String> menuIdList) {
        if (menuIdList != null && !menuIdList.isEmpty()) {
            List<AgileSysRoleMenu> agileSysRoleMenuList = new ArrayList<>();
            for (String menuId : menuIdList) {
                AgileSysRoleMenu agileSysRoleMenu = new AgileSysRoleMenu();
                agileSysRoleMenu.setRoleId(roleId);
                agileSysRoleMenu.setMenuId(menuId);
                agileSysRoleMenuList.add(agileSysRoleMenu);
            }
            return agileSysRoleMenuService.saveBatch(agileSysRoleMenuList);
        }
        return true;
    }

    /**
     * 保存角色部门数据
     *
     * @param roleId
     * @param deptIdList
     * @return
     */
    private boolean saveRoleDept(String roleId, List<String> deptIdList) {
        if (deptIdList != null && !deptIdList.isEmpty()) {
            List<AgileSysRoleDept> agileSysRoleDeptList = new ArrayList<>();
            for (String deptId : deptIdList) {
                AgileSysRoleDept agileSysRoleDept = new AgileSysRoleDept();
                agileSysRoleDept.setRoleId(roleId);
                agileSysRoleDept.setDeptId(deptId);
                agileSysRoleDeptList.add(agileSysRoleDept);
            }
            return agileSysRoleDeptService.saveBatch(agileSysRoleDeptList);
        }
        return true;
    }
}
