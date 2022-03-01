package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgileRequiresUser;
import com.jeeagile.core.security.base.IAgileUserDetailsService;
import com.jeeagile.core.security.user.AgileLoginUser;
import com.jeeagile.core.security.util.AgileSecurityUtil;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.user.AgileUserData;
import com.jeeagile.system.entity.AgileSysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@RestController
@Api(value = "用户登录", tags = "用户登录")
@RequestMapping("/system/user")
public class AgileSysLoginController extends AgileBaseController {

    @AgileReference
    private IAgileUserDetailsService agileUserDetailsService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @AgileLogger(title = "用户登录", type = AgileLoggerType.LOGIN)
    public AgileResult<AgileUserData> login(@RequestBody AgileLoginUser agileLoginUser) {
        if (StringUtil.isEmpty(agileLoginUser.getUserName())) {
            return this.rtnError(AgileResultCode.FAIL_AUTH, "登录名不能为空!");
        }
        if (StringUtil.isEmpty(agileLoginUser.getPassword())) {
            return this.rtnError(AgileResultCode.FAIL_AUTH, "登录密码不能为空!");
        }
        AgileSecurityUtil.userLogin(agileLoginUser);
        if (AgileSecurityUtil.checkAuthenticated()) {
            return this.rtnSuccess(AgileSecurityUtil.getUserData());
        } else {
            return this.rtnError(AgileResultCode.FAIL_AUTH);
        }
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出", notes = "用户退出")
    public AgileResult userLogout() {
        AgileSecurityUtil.userLogout();
        return this.rtnSuccess();
    }

    @AgileRequiresUser
    @PostMapping("/getInfo")
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    public AgileResult<AgileUserData> getUserInfo() {
        return this.rtnSuccess(AgileSecurityUtil.getUserData());
    }

    @AgileRequiresUser
    @PostMapping("/getMenu")
    @ApiOperation(value = "获取用户分配菜单", notes = "获取用户分配菜单")
    public AgileResult<AgileSysMenu> getUserMenu() {
        try {
            return this.rtnSuccess(agileUserDetailsService.getUserMenu(AgileSecurityUtil.getUserData()));
        } catch (Exception ex) {
            return this.rtnError(ex, "获取用户菜单异常！");
        }
    }
}
