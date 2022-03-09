package com.jeeagile.system.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.system.entity.AgileSysRole;
import com.jeeagile.system.service.IAgileSysRoleService;
import com.jeeagile.system.vo.AgileUpdateStatus;
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
@RequestMapping("/system/role")
@AgilePermissionsPrefix("system:role")
@Api(value = "角色管理", tags = "角色管理")
public class AgileSysRoleController extends AgileCrudController<IAgileSysRoleService,AgileSysRole> {
    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新角色状态", notes = "更新角色状态")
    @AgileLogger(title = "更新角色状态", type = AgileLoggerType.UPDATE)
    public AgileResult<String> changeRoleStatus(@RequestBody AgileUpdateStatus agileUpdateStatus) {
        this.getAgileService().changeRoleStatus(agileUpdateStatus);
        return this.success();
    }

    @AgileDemo
    @PostMapping(value = "/dataScope")
    @ApiOperation(value = "更新角色权限范围", notes = "更新角色权限范围")
    @AgileLogger(title = "更新角色权限范围", type = AgileLoggerType.UPDATE)
    public AgileResult<String> dataScope(@RequestBody AgileSysRole agileSysRole) {
        this.getAgileService().updateRoleDataScope(agileSysRole);
        return this.success();
    }
}

