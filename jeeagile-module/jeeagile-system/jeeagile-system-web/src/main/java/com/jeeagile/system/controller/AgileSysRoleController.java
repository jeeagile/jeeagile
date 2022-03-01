package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.service.IAgileSysRoleService;
import com.jeeagile.system.vo.AgileSysRoleInfo;
import com.jeeagile.system.vo.AgileUpdateStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 角色管理 前端控制器
 */
@RestController
@RequestMapping("/system/role")
@Api(value = "角色管理", tags = "角色管理")
public class AgileSysRoleController extends AgileBaseController {

    @AgileReference
    private IAgileSysRoleService agileSysRoleService;

    @PostMapping(value = "/selectPage")
    @ApiOperation(value = "分页查询角色列表", notes = "分页查询角色列表")
    @AgileLogger(title = "查询角色列表", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileSysRole>> selectRolePage(@RequestBody AgilePageable<AgileSysRole> agilePageable) {
        return this.rtnSuccess(agileSysRoleService.selectRolePage(agilePageable));
    }

    @PostMapping("/selectList")
    @ApiOperation(value = "查询角色列表", notes = "查询角色列表")
    @AgileLogger(title = "查询角色列表", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysRole>> selectRoleList(@RequestBody AgileSysRole agileSysRole) {
        return this.rtnSuccess(agileSysRoleService.selectRoleList(agileSysRole));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据角色ID获取角色详细信息", notes = "根据角色ID获取角色详细信息")
    @AgileLogger(title = "查看角色信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysRoleInfo> detailRole(@SingleRequestBody String roleId) {
        return this.rtnSuccess(agileSysRoleService.selectRoleById(roleId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增角色", notes = "新增角色")
    @AgileLogger(title = "新增角色", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:role:add")
    public AgileResult<AgileSysRoleInfo> addRole(@RequestBody AgileSysRoleInfo agileSysRoleInfo) {
        return this.rtnSuccess(agileSysRoleService.saveRole(agileSysRoleInfo));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "修改角色信息", notes = "修改角色信息")
    @AgileLogger(title = "修改角色信息", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:role:update")
    public AgileResult<String> updateRole(@RequestBody AgileSysRoleInfo agileSysRoleInfo) {
        agileSysRoleService.updateRoleById(agileSysRoleInfo);
        return this.rtnSuccess();
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除角色信息", notes = "删除角色信息")
    @AgileLogger(title = "删除角色信息", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:role:delete")
    public AgileResult<AgileSysRole> deleteRole(@SingleRequestBody String roleId) {
        agileSysRoleService.deleteRoleById(roleId);
        return this.rtnSuccess();
    }

    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新角色状态", notes = "更新角色状态")
    @AgileLogger(title = "更新角色状态", type = AgileLoggerType.UPDATE)
    public AgileResult<String> changeRoleStatus(@RequestBody AgileUpdateStatus agileUpdateStatus) {
        agileSysRoleService.changeRoleStatus(agileUpdateStatus);
        return this.rtnSuccess();
    }

    @AgileDemo
    @PostMapping(value = "/dataScope")
    @ApiOperation(value = "更新角色权限范围", notes = "更新角色权限范围")
    @AgileLogger(title = "更新角色权限范围", type = AgileLoggerType.UPDATE)
    public AgileResult<String> dataScope(@RequestBody AgileSysRoleInfo agileSysRoleInfo) {
        agileSysRoleService.updateRoleDataScope(agileSysRoleInfo);
        return this.rtnSuccess();
    }
}

