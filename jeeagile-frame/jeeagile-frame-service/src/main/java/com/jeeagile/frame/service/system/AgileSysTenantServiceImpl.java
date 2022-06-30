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
import com.jeeagile.frame.entity.system.AgileSysMenu;
import com.jeeagile.frame.entity.system.AgileSysTenant;
import com.jeeagile.frame.entity.system.AgileSysUser;
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
            AgileSecurityContext.putTenantId(agileSysTenant.getId());
            this.deleteTenantUser();
            this.deleteTenantMenu();
            AgileSecurityContext.removeTenant();
        }
        this.removeById(id);
        return true;
    }

    /**
     * 租户模式删除租户下所有用户
     */
    private void deleteTenantUser() {
        LambdaQueryWrapper<AgileSysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysUserService.remove(lambdaQueryWrapper);
    }

    /**
     * 租户模式删除租户分配菜单
     */
    private void deleteTenantMenu() {
        LambdaQueryWrapper<AgileSysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        agileSysMenuService.remove(lambdaQueryWrapper);
    }


    @Override
    public boolean audit(AgileSysTenant agileSysTenant) {
        AgileSysTenant agileSysTenantOld = this.getById(agileSysTenant.getId());
        if (agileSysTenantOld == null || agileSysTenantOld.isEmptyPk()) {
            throw new AgileFrameException("租户信息已不存在！");
        }

        if (AgileAuditStatus.PASS.getCode().equals(agileSysTenant.getAuditStatus())) {
            AgileSecurityContext.putTenantId(agileSysTenantOld.getId());
            this.initTenantAdminUser(agileSysTenant);
            this.initTenantMenu();
            AgileSecurityContext.removeTenant();
        }
        return this.updateModel(agileSysTenant);
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

        String userId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(userId).parentId(systemId).menuName("用户管理").menuSort(1).menuComp("system/user/index").menuPath("user").menuIcon("user").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String roleId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(roleId).parentId(systemId).menuName("角色管理").menuSort(2).menuComp("system/role/index").menuPath("role").menuIcon("role").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String menuId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(menuId).parentId(systemId).menuName("菜单管理").menuSort(3).menuComp("system/menu/index").menuPath("menu").menuIcon("menu").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String deptId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(deptId).parentId(systemId).menuName("部门管理").menuSort(4).menuComp("system/dept/index").menuPath("dept").menuIcon("dept").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String postId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(postId).parentId(systemId).menuName("岗位管理").menuSort(5).menuComp("system/post/index").menuPath("post").menuIcon("post").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String dictId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(dictId).parentId(systemId).menuName("字典管理").menuSort(6).menuComp("system/dict/index").menuPath("dict").menuIcon("dict").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String configId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(configId).parentId(systemId).menuName("参数设置").menuSort(7).menuComp("system/config/index").menuPath("config").menuIcon("config").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());


        String monitorId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(monitorId).parentId("0").menuName("系统监控").menuSort(2).menuPath("monitor").menuIcon("monitor").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String onlineId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(onlineId).parentId(monitorId).menuName("在线用户").menuSort(1).menuComp("monitor/online/index").menuPath("online").menuIcon("online").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String druidId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(druidId).parentId(monitorId).menuName("数据监控").menuSort(2).menuComp("monitor/druid/index").menuPath("druid").menuIcon("druid").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String serverId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(serverId).parentId(monitorId).menuName("服务监控").menuSort(3).menuComp("monitor/server/index").menuPath("server").menuIcon("server").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());


        String toolId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(toolId).parentId("0").menuName("系统工具").menuSort(3).menuPath("tool").menuIcon("tool").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String generatorId=AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(generatorId).parentId(toolId).menuName("代码生成").menuSort(1).menuComp("tool/generator/index").menuPath("generator").menuIcon("generator").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(toolId).menuName("系统接口").menuSort(2).menuComp("tool/swagger/index").menuPath("swagger").menuIcon("swagger").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(toolId).menuName("表单构建").menuSort(3).menuComp("tool/form/index").menuPath("form").menuIcon("form").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(toolId).menuName("流程设计").menuSort(4).menuComp("tool/process/index").menuPath("process").menuIcon("process").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String quartzId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(quartzId).parentId("0").menuName("任务管理").menuSort(4).menuPath("quartz").menuIcon("quartz").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String jobId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(jobId).parentId(quartzId).menuName("定时任务").menuSort(1).menuComp("quartz/job/index").menuPath("job").menuIcon("job").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(quartzId).menuName("执行日志").menuSort(2).menuComp("quartz/logger/index").menuPath("quartzLogger").menuIcon("logger").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());



        String loggerId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(loggerId).parentId("0").menuName("日志管理").menuSort(5).menuPath("logger").menuIcon("logger").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(loggerId).menuName("定时任务").menuSort(1).menuComp("logger/operate/index").menuPath("operate").menuIcon("operate").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        agileSysMenuList.add(AgileSysMenu.builder().id(AgileStringUtil.getUuid()).parentId(loggerId).menuName("执行日志").menuSort(2).menuComp("logger/login/index").menuPath("login").menuIcon("login").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());


        String processId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(processId).parentId("0").menuName("流程管理").menuSort(6).menuPath("process").menuIcon("process").menuType("M").menuVisible("0").menuStatus("0").menuFrame("1").build());

        String formId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(formId).parentId(processId).menuName("流程表单").menuSort(1).menuComp("process/form/index").menuPath("form").menuIcon("form").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());
        String modelId = AgileStringUtil.getUuid();
        agileSysMenuList.add(AgileSysMenu.builder().id(modelId).parentId(processId).menuName("流程模型").menuSort(2).menuComp("process/model/index").menuPath("model").menuIcon("example").menuType("C").menuVisible("0").menuStatus("0").menuFrame("1").build());


        agileSysMenuService.saveBatch(agileSysMenuList);
    }
}
