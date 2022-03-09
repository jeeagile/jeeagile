package com.jeeagile.system.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.service.IAgileSysUserService;
import com.jeeagile.system.vo.AgileUpdatePwd;
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
 * @description 用户管理 前端控制器
 */
@RestController
@RequestMapping("/system/user")
@AgilePermissionsPrefix("system:user")
@Api(value = "用户管理", tags = "用户管理")
public class AgileSysUserController extends AgileCrudController<IAgileSysUserService, AgileSysUser> {
    @AgileDemo
    @PostMapping(value = "/resetPwd")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @AgileLogger(title = "重置用户密码", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:user:resetPwd")
    public AgileResult<Object> resetUserPassword(@RequestBody AgileUpdatePwd agileUpdatePwd) {
        this.getAgileService().resetUserPwd(agileUpdatePwd);
        return this.success();
    }

    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新用户状态", notes = "更新用户状态")
    @AgileLogger(title = "更新用户状态", type = AgileLoggerType.UPDATE)
    public AgileResult<Object> changeUserStatus(@RequestBody AgileUpdateStatus agileUpdateStatus) {
        this.getAgileService().changeUserStatus(agileUpdateStatus);
        return this.success();
    }
}
