package com.jeeagile.frame.service.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jeeagile.core.enums.AgileAuditStatus;
import com.jeeagile.core.enums.AgileUserStatus;
import com.jeeagile.core.exception.AgileFrameException;
import com.jeeagile.core.exception.AgileValidateException;
import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.security.context.AgileSecurityContext;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.core.util.tenant.AgileTenantUtil;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.mapper.system.AgileSysTenantMapper;
import com.jeeagile.frame.service.AgileBaseServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2022-06-24
 * @description 租户管理
 */
@AgileService
public class AgileSysTenantServiceImpl extends AgileBaseServiceImpl<AgileSysTenantMapper, AgileSysTenant> implements IAgileSysTenantService {
    @Autowired
    private IAgileSysUserService agileSysUserService;
    @Autowired
    private IAgileSysMenuService agileSysMenuService;
    @Autowired
    private IAgileSysDictTypeService agileSysDictTypeService;
    @Autowired
    private IAgileSysDictDataService agileSysDictDataService;
    @Autowired
    private IAgileSysRoleService agileSysRoleService;
    @Autowired
    private IAgileSysPostService agileSysPostService;
    @Autowired
    private IAgileSysDeptService agileSysDeptService;
    @Autowired
    private IAgileSysConfigService agileSysConfigService;
    @Autowired
    private IAgileSysRoleDeptService agileSysRoleDeptService;
    @Autowired
    private IAgileSysRoleMenuService agileSysRoleMenuService;
    @Autowired
    private IAgileSysUserRoleService agileSysUserRoleService;
    @Autowired
    private IAgileSysUserPostService agileSysUserPostService;


    @Override
    public LambdaQueryWrapper<AgileSysTenant> queryWrapper(AgileSysTenant agileSysTenant) {
        LambdaQueryWrapper<AgileSysTenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysTenant != null) {
            if (AgileStringUtil.isNotEmpty(agileSysTenant.getTenantName())) {
                lambdaQueryWrapper.like(AgileSysTenant::getTenantName, agileSysTenant.getTenantName());
            }
            if (AgileStringUtil.isNotEmpty(agileSysTenant.getTenantCode())) {
                lambdaQueryWrapper.eq(AgileSysTenant::getTenantCode, agileSysTenant.getTenantCode());
            }
            if (AgileStringUtil.isNotEmpty(agileSysTenant.getEnableStatus())) {
                lambdaQueryWrapper.eq(AgileSysTenant::getEnableStatus, agileSysTenant.getEnableStatus());
            }
        }
        lambdaQueryWrapper.ne(AgileSysTenant::getTenantCode, AgileTenantUtil.getDefaultTenant());
        return lambdaQueryWrapper;
    }

    @Override
    public AgileSysTenant selectModel(Serializable id) {
        AgileSysTenant agileSysTenant = this.getById(id);
        if (agileSysTenant != null && agileSysTenant.isNotEmptyPk()) {
            String signData = agileSysTenant.getTenantCode() + agileSysTenant.getId();
            agileSysTenant.setTenantSign(DigestUtils.md2Hex(signData.getBytes()));
        }
        return agileSysTenant;
    }

    @Override
    public void saveModelValidate(AgileSysTenant agileSysTenant) {
        if(!AgileTenantUtil.isTenantEnable()){
            throw new AgileFrameException("租户模式未开启请勿创建租户！");
        }
        agileSysTenant.setAuditStatus(AgileAuditStatus.AUDIT.getCode());
        this.validateData(agileSysTenant);
    }

    @Override
    public boolean updateModel(AgileSysTenant agileSysTenant) {
        agileSysTenant.setTenantCode(null);
        this.updateModelValidate(agileSysTenant);
        AgileSysTenant agileSysTenantOld = this.getById(agileSysTenant.getId());
        if (AgileAuditStatus.PASS.getCode().equals(agileSysTenantOld.getAuditStatus())) {
            AgileSecurityContext.putTenantId(agileSysTenantOld.getId());
            if (!agileSysTenant.getTenantName().equals(agileSysTenantOld.getTenantName())) {
                this.updateTenantAdminUser(agileSysTenant);
            }
            AgileSecurityContext.removeTenant();
        }
        if (this.updateById(agileSysTenant)) {
            return true;
        }
        return false;
    }

    /**
     * 更新租户管理员名称
     *
     * @param agileSysTenant
     */
    private void updateTenantAdminUser(AgileSysTenant agileSysTenant) {
        LambdaUpdateWrapper<AgileSysUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(AgileSysUser::getNickName, agileSysTenant.getTenantName());
        lambdaUpdateWrapper.eq(AgileSysUser::getUserName, agileSysTenant.getTenantCode());
        agileSysUserService.update(lambdaUpdateWrapper);
    }

    @Override
    public void updateModelValidate(AgileSysTenant agileSysTenant) {
        this.validateData(agileSysTenant);
    }

    /**
     * 校验参数名称或参数键名不能与已存在的数据重复
     */
    private void validateData(AgileSysTenant agileSysTenant) {
        LambdaQueryWrapper<AgileSysTenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (agileSysTenant.getId() != null) {
            lambdaQueryWrapper.ne(AgileSysTenant::getId, agileSysTenant.getId());
        }
        lambdaQueryWrapper.and(wrapper ->
                wrapper.eq(AgileSysTenant::getTenantCode, agileSysTenant.getTenantCode()).or().eq(AgileSysTenant::getTenantName, agileSysTenant.getTenantName())
        );
        if (this.count(lambdaQueryWrapper) > 0) {
            throw new AgileValidateException("租户编码或租户名称已存在！");
        }
    }

    @Override
    public boolean deleteModel(Serializable id) {
        AgileSysTenant agileSysTenant = this.getById(id);
        if (agileSysTenant == null || agileSysTenant.isEmptyPk()) {
            throw new AgileFrameException("租户信息已不存在！");
        }
        if (AgileAuditStatus.PASS.getCode().equals(agileSysTenant.getAuditStatus())) {
            this.deleteTenantInfo(agileSysTenant);
        }
        this.removeById(id);
        return true;
    }

    @Override
    public AgileSysTenant agileSysTenantInfo(Serializable tenantId) {
        AgileSecurityContext.putDisableTenant(true);
        AgileSysTenant agileSysTenant = this.selectModel(tenantId);
        AgileSecurityContext.removeDisableTenant();
        return agileSysTenant;
    }

    @Override
    public boolean audit(AgileSysTenant agileSysTenant) {
        AgileSysTenant agileSysTenantOld = this.getById(agileSysTenant.getId());
        if (agileSysTenantOld == null || agileSysTenantOld.isEmptyPk()) {
            throw new AgileFrameException("租户信息已不存在！");
        }

        if (AgileAuditStatus.PASS.getCode().equals(agileSysTenant.getAuditStatus())) {
            initTenantInfo(agileSysTenantOld);
        }
        return this.updateModel(agileSysTenant);
    }

    /**
     * 初始化租户信息
     *
     * @param agileSysTenant
     */
    private void initTenantInfo(AgileSysTenant agileSysTenant) {
        AgileSecurityContext.putTenantId(agileSysTenant.getId());
        this.initTenantAdminUser(agileSysTenant);
        this.initTenantDept(agileSysTenant);
        this.initTenantMenu();
        this.initTenantDictType();
        this.initTenantDictData();
        this.initTenantConfig();
        AgileSecurityContext.removeTenant();
    }

    /**
     * 初始化租户默认部门
     *
     * @param agileSysTenant
     */
    private void initTenantDept(AgileSysTenant agileSysTenant) {
        AgileSysDept agileSysDept = new AgileSysDept();
        agileSysDept.setDeptCode(agileSysTenant.getTenantCode());
        agileSysDept.setDeptName(agileSysTenant.getTenantName());
        agileSysDept.setDeptStatus("0");
        agileSysDeptService.save(agileSysDept);
    }

    /**
     * 创建租户默认管理员
     *
     * @param agileSysTenant
     */
    private void initTenantAdminUser(AgileSysTenant agileSysTenant) {
        AgileSysUser agileSysUser = new AgileSysUser();
        agileSysUser.setUserName(agileSysTenant.getTenantCode());
        agileSysUser.setNickName(agileSysTenant.getTenantName());
        agileSysUser.setUserStatus(AgileUserStatus.NORMAL.getCode());
        agileSysUser.setUserSort(0);
        agileSysUser.setUserPwd(AgileSecurityUtil.encryptPassword(agileSysTenant.getTenantCode()));
        agileSysUserService.save(agileSysUser);
    }

    /**
     * 初始化租户菜单
     */
    private void initTenantMenu() {
        List<AgileSysMenu> agileSysMenuList = new ArrayList<>();
        String taskId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(taskId).parentId("0").menuName("我的事务").menuSort(0).menuPath("process/task").menuIcon("education").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String startId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(startId).parentId(taskId).menuName("流程发起").menuSort(0).menuComp("process/task/start").menuPath("start").menuIcon("edit").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String todoId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(todoId).parentId(taskId).menuName("我的代办").menuSort(0).menuComp("process/task/todo").menuPath("todo").menuIcon("message").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String doneId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(doneId).parentId(taskId).menuName("我的已办").menuSort(0).menuComp("process/task/done").menuPath("done").menuIcon("clipboard").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String applyId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(applyId).parentId(taskId).menuName("我的发起").menuSort(0).menuComp("process/task/apply").menuPath("apply").menuIcon("button").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String systemId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(systemId).parentId("0").menuName("系统管理").menuSort(1).menuPath("system").menuIcon("system").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String tenantId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(tenantId).parentId(systemId).menuName("租户管理").menuPerm("system:tenant:page").menuSort(1).menuComp("system/tenant/index").menuPath("tenant").menuIcon("tenant").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(tenantId).menuName("租户明细").menuPerm("system:tenant:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(tenantId).menuName("租户新增").menuPerm("system:tenant:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(tenantId).menuName("租户修改").menuPerm("system:tenant:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(tenantId).menuName("租户删除").menuPerm("system:tenant:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(tenantId).menuName("租户导入").menuPerm("system:tenant:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(tenantId).menuName("租户导出").menuPerm("system:tenant:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());


        String userId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(userId).parentId(systemId).menuName("用户管理").menuPerm("system:user:page").menuSort(1).menuComp("system/user/index").menuPath("user").menuIcon("user").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("用户明细").menuPerm("system:user:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("用户新增").menuPerm("system:user:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("用户修改").menuPerm("system:user:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("用户删除").menuPerm("system:user:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("重置密码").menuPerm("system:user:password").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("用户导入").menuPerm("system:user:import").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(userId).menuName("用户导出").menuPerm("system:user:export").menuSort(7).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String roleId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(roleId).parentId(systemId).menuName("角色管理").menuPerm("system:role:page").menuSort(2).menuComp("system/role/index").menuPath("role").menuIcon("role").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("角色明细").menuPerm("system:role:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("角色新增").menuPerm("system:role:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("角色修改").menuPerm("system:role:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("角色删除").menuPerm("system:role:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("数据权限").menuPerm("system:role:scope").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("角色导入").menuPerm("system:role:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(roleId).menuName("角色导出").menuPerm("system:role:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String menuId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(menuId).parentId(systemId).menuName("菜单管理").menuPerm("system:menu:list").menuSort(3).menuComp("system/menu/index").menuPath("menu").menuIcon("menu").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单明细").menuPerm("system:menu:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单新增").menuPerm("system:menu:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单修改").menuPerm("system:menu:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单删除").menuPerm("system:menu:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单导入").menuPerm("system:menu:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单导出").menuPerm("system:menu:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(menuId).menuName("菜单排序").menuPerm("system:menu:sort").menuSort(7).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String deptId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(deptId).parentId(systemId).menuName("部门管理").menuPerm("system:dept:list").menuSort(4).menuComp("system/dept/index").menuPath("dept").menuIcon("dept").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(deptId).menuName("部门明细").menuPerm("system:dept:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(deptId).menuName("部门新增").menuPerm("system:dept:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(deptId).menuName("部门修改").menuPerm("system:dept:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(deptId).menuName("部门删除").menuPerm("system:dept:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(deptId).menuName("部门导入").menuPerm("system:dept:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(deptId).menuName("部门导出").menuPerm("system:dept:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String postId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(postId).parentId(systemId).menuName("岗位管理").menuPerm("system:post:page").menuSort(5).menuComp("system/post/index").menuPath("post").menuIcon("post").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(postId).menuName("岗位明细").menuPerm("system:post:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(postId).menuName("岗位新增").menuPerm("system:post:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(postId).menuName("岗位修改").menuPerm("system:post:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(postId).menuName("岗位删除").menuPerm("system:post:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(postId).menuName("岗位导入").menuPerm("system:post:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(postId).menuName("岗位导出").menuPerm("system:post:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String dictId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(dictId).parentId(systemId).menuName("字典管理").menuPerm("system:dict:type:page").menuSort(6).menuComp("system/dict/index").menuPath("dict").menuIcon("dict").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典明细").menuPerm("system:dict:type:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典新增").menuPerm("system:dict:type:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典修改").menuPerm("system:dict:type:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典删除").menuPerm("system:dict:type:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典导入").menuPerm("system:dict:type:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典导出").menuPerm("system:dict:type:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据列表").menuPerm("system:dict:data:page").menuSort(7).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据列表").menuPerm("system:dict:data:list").menuSort(8).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据明细").menuPerm("system:dict:data:detail").menuSort(9).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据新增").menuPerm("system:dict:data:add").menuSort(10).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据修改").menuPerm("system:dict:data:update").menuSort(11).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据删除").menuPerm("system:dict:data:delete").menuSort(12).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据导入").menuPerm("system:dict:data:import").menuSort(13).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(dictId).menuName("字典数据导出").menuPerm("system:dict:data:export").menuSort(14).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String configId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(configId).parentId(systemId).menuName("参数设置").menuPerm("system:config:page").menuSort(7).menuComp("system/config/index").menuPath("config").menuIcon("config").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(configId).menuName("参数明细").menuPerm("system:config:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(configId).menuName("参数新增").menuPerm("system:config:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(configId).menuName("参数修改").menuPerm("system:config:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(configId).menuName("参数删除").menuPerm("system:config:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(configId).menuName("参数导入").menuPerm("system:config:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(configId).menuName("参数导出").menuPerm("system:config:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String operateLoggerId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(operateLoggerId).parentId(systemId).menuName("操作日志").menuPerm("logger:operate:page").menuSort(1).menuComp("system/logger/operate").menuPath("operate").menuIcon("operate").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(operateLoggerId).menuName("查看").menuPerm("logger:operate:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(operateLoggerId).menuName("删除").menuPerm("logger:operate:delete").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(operateLoggerId).menuName("清空").menuPerm("logger:operate:clear").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String loginLoggerId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(loginLoggerId).parentId(systemId).menuName("登录日志").menuPerm("logger:login:page").menuSort(2).menuComp("system/logger/login").menuPath("login").menuIcon("login").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(loginLoggerId).menuName("删除").menuPerm("logger:login:delete").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(loginLoggerId).menuName("清空").menuPerm("logger:login:clear").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());


        String monitorId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(monitorId).parentId("0").menuName("系统监控").menuSort(2).menuPath("monitor").menuIcon("monitor").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String onlineId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(onlineId).parentId(monitorId).menuName("在线用户").menuPerm("monitor:online:list").menuSort(1).menuComp("monitor/online/index").menuPath("online").menuIcon("online").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(onlineId).menuName("批量强退").menuPerm("monitor:online:batchLogout").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(onlineId).menuName("单户强退").menuPerm("monitor:online:forceLogout").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String druidId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(druidId).parentId(monitorId).menuName("数据监控").menuPerm("'monitor:druid:list").menuSort(2).menuComp("monitor/druid/index").menuPath("druid").menuIcon("druid").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String serverId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(serverId).parentId(monitorId).menuName("服务监控").menuPerm("monitor:server:info").menuSort(3).menuComp("monitor/server/index").menuPath("server").menuIcon("server").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());


        String toolId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(toolId).parentId("0").menuName("系统工具").menuSort(3).menuPath("tool").menuIcon("tool").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String generatorId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(generatorId).parentId(toolId).menuName("代码生成").menuPerm("tool:generator:page").menuSort(1).menuComp("tool/generator/index").menuPath("generator").menuIcon("generator").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(generatorId).menuName("生成修改").menuPerm("tool:generator:update").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(generatorId).menuName("生成删除").menuPerm("tool:generator:delete").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(generatorId).menuName("导入代码").menuPerm("tool:generator:import").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(generatorId).menuName("同步信息").menuPerm("tool:generator:sync").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(generatorId).menuName("预览代码").menuPerm("tool:generator:preview").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(generatorId).menuName("生成代码").menuPerm("tool:generator:code").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(toolId).menuName("系统接口").menuSort(2).menuComp("tool/swagger/index").menuPath("swagger").menuIcon("swagger").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(toolId).menuName("表单构建").menuSort(3).menuComp("tool/form/index").menuPath("form").menuIcon("form").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(toolId).menuName("流程设计").menuSort(4).menuComp("tool/process/index").menuPath("process").menuIcon("process").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String quartzId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(quartzId).parentId("0").menuName("任务管理").menuSort(4).menuPath("quartz").menuIcon("quartz").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String quartzJobId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(quartzJobId).parentId(quartzId).menuName("定时任务").menuPerm("quartz:job:page").menuSort(1).menuComp("quartz/job/index").menuPath("job").menuIcon("job").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务明细").menuPerm("quartz:job:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务新增").menuPerm("quartz:job:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务修改").menuPerm("quartz:job:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务删除").menuPerm("quartz:job:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务导入").menuPerm("quartz:job:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务导出").menuPerm("quartz:job:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzJobId).menuName("任务执行").menuPerm("quartz:job:execute").menuSort(7).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String quartzLoggerId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(quartzLoggerId).parentId(quartzId).menuName("执行日志").menuPerm("job:logger:page").menuSort(2).menuComp("quartz/logger/index").menuPath("quartzLogger").menuIcon("logger").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzLoggerId).menuName("查看").menuPerm("quartz:logger:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzLoggerId).menuName("删除").menuPerm("quartz:logger:delete").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzLoggerId).menuName("清空").menuPerm("quartz:logger:clear").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());



        String processId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(processId).parentId("0").menuName("流程管理").menuSort(6).menuPath("process").menuIcon("process").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String formId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(formId).parentId(processId).menuName("流程表单").menuPerm("process:form:page").menuSort(1).menuComp("process/form/index").menuPath("form").menuIcon("form").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单明细").menuPerm("process:form:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单新增").menuPerm("process:form:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单修改").menuPerm("process:form:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单删除").menuPerm("process:form:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单导入").menuPerm("process:form:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单导出").menuPerm("process:form:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单预览").menuPerm("process:form:view").menuSort(7).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(formId).menuName("流程表单设计").menuPerm("process:form:designer").menuSort(8).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String modelId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(modelId).parentId(processId).menuName("流程模型").menuPerm("process:model:page").menuSort(2).menuComp("process/model/index").menuPath("model").menuIcon("example").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型明细").menuPerm("process:model:detail").menuSort(1).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型新增").menuPerm("process:model:add").menuSort(2).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型修改").menuPerm("process:model:update").menuSort(3).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型删除").menuPerm("process:model:delete").menuSort(4).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型导入").menuPerm("process:model:import").menuSort(5).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型导出").menuPerm("process:model:export").menuSort(6).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型预览").menuPerm("process:model:view").menuSort(7).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(modelId).menuName("流程模型设计").menuPerm("process:model:designer").menuSort(8).menuIcon("#").menuType("F").menuVisible("0").menuStatus("0").menuFrame("1").build());

        agileSysMenuService.saveBatch(agileSysMenuList);
    }

    /**
     * 初始化租户字典类型
     */
    private void initTenantDictType() {
        List<AgileSysDictType> agileSysDictTypeList = new ArrayList<>();
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_user_sex").dictName("用户性别").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_show_visible").dictName("菜单状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_normal_disable").dictName("系统开关").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_job_status").dictName("任务状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_yes_no").dictName("系统是否").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_data_scope").dictName("数据范围").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_common_status").dictName("系统状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_status").dictName("日志状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictName("日志类型").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_enable_status").dictName("启用开关").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("sys_audit_status").dictName("系统审核状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("process_deployment_status").dictName("流程发布状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("process_form_type").dictName("流程表单类型").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("process_task_status").dictName("流程任务状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeList.add(AgileSysDictType.builder().id(AgileStringUtil.getUuid()).dictType("process_instance_status").dictName("流程实例状态").dictStatus("0").systemFlag("0").build());
        agileSysDictTypeService.saveBatch(agileSysDictTypeList);
    }

    /**
     * 初始化租户字典数据
     */
    private void initTenantDictData() {
        List<AgileSysDictData> agileSysDictDataList = new ArrayList<>();
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_user_sex").dictLabel("男").dictValue("0").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_user_sex").dictLabel("女").dictValue("1").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_user_sex").dictLabel("未知").dictValue("2").dictSort(3).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_show_visible").dictLabel("显示").dictValue("0").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_show_visible").dictLabel("隐藏").dictValue("1").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_normal_disable").dictLabel("正常").dictValue("0").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_normal_disable").dictLabel("停用").dictValue("1").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_job_status").dictLabel("启用").dictValue("0").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_job_status").dictLabel("暂停").dictValue("1").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_yes_no").dictLabel("是").dictValue("1").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_yes_no").dictLabel("否").dictValue("0").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_data_scope").dictLabel("全部数据权限").dictValue("01").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_data_scope").dictLabel("本部门数据权限").dictValue("02").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_data_scope").dictLabel("本部门及以下数据权限").dictValue("03").dictSort(3).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_data_scope").dictLabel("仅本人数据权限").dictValue("04").dictSort(4).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_data_scope").dictLabel("自定义部门数据权限").dictValue("05").dictSort(5).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_common_status").dictLabel("成功").dictValue("0").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_common_status").dictLabel("失败").dictValue("1").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_status").dictLabel("成功").dictValue("0").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_status").dictLabel("失败").dictValue("1").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("查询数据").dictValue("SELECT").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("查看明细").dictValue("DETAIL").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("新增数据").dictValue("ADD").dictSort(3).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("修改数据").dictValue("UPDATE").dictSort(4).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("删除数据").dictValue("DELETE").dictSort(5).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("用户授权").dictValue("GRANT").dictSort(6).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("导出数据").dictValue("EXPORT").dictSort(7).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("导入数据").dictValue("IMPORT").dictSort(8).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("清空数据").dictValue("CLEAR").dictSort(9).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("用户强退").dictValue("FORCE").dictSort(10).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("代码生成").dictValue("GENERATOR").dictSort(11).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_logger_type").dictLabel("其他操作").dictValue("OTHER").dictSort(12).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_enable_status").dictLabel("启用").dictValue("0").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_enable_status").dictLabel("停用").dictValue("1").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_audit_status").dictLabel("审核中").dictValue("0").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_audit_status").dictLabel("审核通过").dictValue("1").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("sys_audit_status").dictLabel("审核拒绝").dictValue("2").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_deployment_status").dictLabel("已发布").dictValue("1").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_deployment_status").dictLabel("未发布").dictValue("2").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_form_type").dictLabel("流程表单").dictValue("1").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_form_type").dictLabel("业务表单").dictValue("2").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_task_status").dictLabel("已撤销").dictValue("0").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_task_status").dictLabel("审批中").dictValue("1").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_task_status").dictLabel("审批通过").dictValue("2").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_task_status").dictLabel("审批驳回").dictValue("3").dictSort(3).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_instance_status").dictLabel("已撤销").dictValue("0").dictSort(0).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_instance_status").dictLabel("处理中").dictValue("1").dictSort(1).dictStatus("0").systemFlag("0").build());
        agileSysDictDataList.add(AgileSysDictData.builder().id(AgileStringUtil.getUuid()).dictType("process_instance_status").dictLabel("已完成").dictValue("2").dictSort(2).dictStatus("0").systemFlag("0").build());
        agileSysDictDataService.saveBatch(agileSysDictDataList);
    }

    /**
     * 初始化租户系统配置
     */
    private void initTenantConfig() {
        List<AgileSysConfig> agileSysConfigList = new ArrayList<>();
        agileSysConfigList.add(AgileSysConfig.builder().id(AgileStringUtil.getUuid()).configName("用户管理-账号初始密码").configKey("sys.user.pwd").configValue("123456").systemFlag("1").build());
        agileSysConfigService.saveBatch(agileSysConfigList);
    }

    /**
     * 删除租户信息
     *
     * @param agileSysTenant
     */
    private void deleteTenantInfo(AgileSysTenant agileSysTenant) {
        AgileSecurityContext.putTenantId(agileSysTenant.getId());
        this.deleteTenantUserRole();
        this.deleteTenantUserPost();
        this.deleteTenantRoleMenu();
        this.deleteTenantRoleDept();
        this.deleteTenantDictType();
        this.deleteTenantDictData();
        this.deleteTenantConfig();
        this.deleteTenantPost();
        this.deleteTenantDept();
        this.deleteTenantUser();
        this.deleteTenantMenu();
        this.deleteTenantRole();
        AgileSecurityContext.removeTenant();
    }

    /**
     * 删除租户用户
     */
    private void deleteTenantUser() {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysUserService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户岗位
     */
    private void deleteTenantPost() {
        LambdaQueryWrapper<AgileSysPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysPostService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户部门
     */
    private void deleteTenantDept() {
        LambdaQueryWrapper<AgileSysDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysDeptService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户系统配置
     */
    private void deleteTenantConfig() {
        LambdaQueryWrapper<AgileSysConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysConfigService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户用户角色
     */
    private void deleteTenantUserRole() {
        LambdaQueryWrapper<AgileSysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysUserRoleService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户用户岗位
     */
    private void deleteTenantUserPost() {
        LambdaQueryWrapper<AgileSysUserPost> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysUserPostService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户菜单
     */
    private void deleteTenantMenu() {
        LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysMenuService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户角色
     */
    private void deleteTenantRole() {
        LambdaQueryWrapper<AgileSysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysRoleService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户角色菜单
     */
    private void deleteTenantRoleMenu() {
        LambdaQueryWrapper<AgileSysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysRoleMenuService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户角色部门
     */
    private void deleteTenantRoleDept() {
        LambdaQueryWrapper<AgileSysRoleDept> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysRoleDeptService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户字典类型
     */
    private void deleteTenantDictType() {
        LambdaQueryWrapper<AgileSysDictType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysDictTypeService.remove(lambdaQueryWrapper);
    }

    /**
     * 删除租户字典数据
     */
    private void deleteTenantDictData() {
        LambdaQueryWrapper<AgileSysDictData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysDictDataService.remove(lambdaQueryWrapper);
    }

}
