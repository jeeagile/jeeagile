package com.jeeagile.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.validate.AgileValidateUtil;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.entity.AgileSysRoleDept;
import com.jeeagile.system.entity.AgileSysRoleMenu;
import com.jeeagile.system.mapper.AgileSysRoleMapper;
import com.jeeagile.system.vo.AgileSysRoleInfo;
import com.jeeagile.system.vo.AgileUpdateStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
    public AgilePage<AgileSysRole> selectRolePage(AgilePageable<AgileSysRole> agilePageable) {
        return this.page(agilePageable, getSysRoleQueryWrapper(agilePageable.getQueryCond()));
    }

    @Override
    public List<AgileSysRole> selectRoleList(AgileSysRole agileSysRole) {
        return this.list(getSysRoleQueryWrapper(agileSysRole));
    }

    @Override
    public AgileSysRoleInfo selectRoleById(String roleId) {
        AgileSysRoleInfo agileSysRoleInfo = new AgileSysRoleInfo();
        AgileSysRole agileSysRole = this.getById(roleId);
        BeanUtils.copyProperties(agileSysRole, agileSysRoleInfo);
        List<AgileSysRoleMenu> sysRoleMenuList = agileSysRoleMenuService.selectListByRoleId(roleId);
        for (AgileSysRoleMenu sysRoleMenu : sysRoleMenuList) {
            //只取叶子节点
            if (agileSysMenuService.countChild(sysRoleMenu.getMenuId()) < 1) {
                agileSysRoleInfo.getMenuIdList().add(sysRoleMenu.getMenuId());
            }
        }
        if (AGILE_DATA_SCOPE_05.equals(agileSysRoleInfo.getDataScope())) {
            List<AgileSysRoleDept> sysRoleDeptList = agileSysRoleDeptService.selectListByRoleId(roleId);
            for (AgileSysRoleDept sysRoleDept : sysRoleDeptList) {
                //只取叶子节点
                if (agileSysDeptService.countChild(sysRoleDept.getDeptId()) < 1) {
                    agileSysRoleInfo.getDeptIdList().add(sysRoleDept.getDeptId());
                }
            }
        }
        return agileSysRoleInfo;
    }

    @Override
    public AgileSysRole saveRole(AgileSysRoleInfo agileSysRoleInfo) {
        //校验业务数据
        AgileValidateUtil.validateObject(agileSysRoleInfo);
        this.save(agileSysRoleInfo);
        this.saveRoleMenu(agileSysRoleInfo.getId(), agileSysRoleInfo.getMenuIdList());
        return agileSysRoleInfo;
    }

    @Override
    public boolean updateRoleById(AgileSysRoleInfo agileSysRoleInfo) {
        //校验业务数据
        AgileValidateUtil.validateObject(agileSysRoleInfo);
        //保存角色信息
        this.updateById(agileSysRoleInfo);
        agileSysRoleMenuService.deleteByRoleId(agileSysRoleInfo.getId());
        this.saveRoleMenu(agileSysRoleInfo.getId(), agileSysRoleInfo.getMenuIdList());
        return true;
    }

    @Override
    public boolean deleteRoleById(String roleId) {
        //删除角色已分配菜单
        agileSysRoleMenuService.deleteByRoleId(roleId);
        //删除数据权限
        agileSysRoleDeptService.deleteByRoleId(roleId);
        //删除角色相关用户
        agileSysUserRoleService.deleteByRoleId(roleId);
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
    public boolean updateRoleDataScope(AgileSysRoleInfo agileSysRoleInfo) {
        AgileSysRole agileSysRole = new AgileSysRole();
        agileSysRole.setId(agileSysRoleInfo.getId());
        agileSysRole.setDataScope(agileSysRole.getDataScope());
        this.updateById(agileSysRoleInfo);
        agileSysRoleDeptService.deleteByRoleId(agileSysRoleInfo.getId());
        if (AGILE_DATA_SCOPE_05.equals(agileSysRoleInfo.getDataScope())) {
            this.saveRoleDept(agileSysRoleInfo.getId(), agileSysRoleInfo.getDeptIdList());
        }
        return true;
    }

    /**
     * 拼装查询条件
     */
    private QueryWrapper<AgileSysRole> getSysRoleQueryWrapper(AgileSysRole agileSysRole) {
        QueryWrapper<AgileSysRole> queryWrapper = new QueryWrapper<>();
        if (agileSysRole != null) {
            //角色名称
            if (StringUtil.isNotEmpty(agileSysRole.getRoleName())) {
                queryWrapper.lambda().like(AgileSysRole::getRoleName, agileSysRole.getRoleName());
            }
            //角色编码
            if (StringUtil.isNotEmpty(agileSysRole.getRoleCode())) {
                queryWrapper.lambda().like(AgileSysRole::getRoleCode, agileSysRole.getRoleCode());
            }
            //角色状态
            if (StringUtil.isNotEmpty(agileSysRole.getRoleStatus())) {
                queryWrapper.lambda().eq(AgileSysRole::getRoleStatus, agileSysRole.getRoleStatus());
            }
        }
        queryWrapper.lambda().orderByAsc(AgileSysRole::getRoleSort);
        return queryWrapper;
    }

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
