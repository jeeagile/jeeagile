package com.jeeagile.system.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.service.IAgileSysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 角色管理 前端控制器
 */
@RestController
@AgileLogger("角色管理")
@RequestMapping("/system/role")
@AgilePermissionsPrefix("system:role")
@Api(value = "角色管理", tags = "角色管理")
public class AgileSysRoleController extends AgileCrudController<IAgileSysRoleService, AgileSysRole> {
    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新角色状态", notes = "更新角色状态")
    @AgileLogger(notes = "更新角色状态", type = AgileLoggerType.UPDATE)
    public AgileResult<String> changeRoleStatus(@SingleRequestBody String roleId, @SingleRequestBody String roleStatus) {
        if (this.agileBaseService.changeRoleStatus(roleId, roleStatus)) {
            return this.success();
        } else {
            return this.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "角色状态更新失败！");
        }
    }

    @AgileDemo
    @PostMapping(value = "/dataScope")
    @ApiOperation(value = "更新角色权限范围", notes = "更新角色权限范围")
    @AgileLogger(notes = "更新角色权限范围", type = AgileLoggerType.UPDATE)
    public AgileResult<String> dataScope(@RequestBody AgileSysRole agileSysRole) {
        if (this.agileBaseService.updateRoleDataScope(agileSysRole)) {
            return this.success();
        } else {
            return this.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "角色权限范围更新失败！");
        }
    }
}

