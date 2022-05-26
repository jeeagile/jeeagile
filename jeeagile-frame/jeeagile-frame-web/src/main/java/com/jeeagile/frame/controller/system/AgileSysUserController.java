package com.jeeagile.frame.controller.system;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.service.system.IAgileSysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 用户管理 前端控制器
 */
@RestController
@AgileLogger("用户管理")
@RequestMapping("/system/user")
@AgilePermissionsPrefix("system:user")
@Api(value = "用户管理", tags = "用户管理")
public class AgileSysUserController extends AgileCrudController<IAgileSysUserService, AgileSysUser> {

    @AgileDemo
    @PostMapping(value = "/password")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @AgileLogger(notes = "重置用户密码", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("password")
    public AgileResult<Object> resetUserPassword(@SingleRequestBody String userId, @SingleRequestBody String password) {
        this.agileBaseService.resetUserPassword(userId, password);
        return this.success();
    }

    @AgileDemo
    @PostMapping(value = "/status")
    @ApiOperation(value = "更新用户状态", notes = "更新用户状态")
    @AgileLogger(notes = "更新用户状态", type = AgileLoggerType.UPDATE)
    public AgileResult<Object> changeStatus(@SingleRequestBody String userId, @SingleRequestBody String userStatus) {
        this.agileBaseService.changeUserStatus(userId, userStatus);
        return this.success();
    }
}
