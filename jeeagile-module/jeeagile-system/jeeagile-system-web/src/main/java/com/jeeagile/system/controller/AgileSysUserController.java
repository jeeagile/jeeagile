package com.jeeagile.system.controller;

import com.jeeagile.core.cache.constants.AgileCacheConstants;
import com.jeeagile.core.cache.util.AgileCacheUtil;
import com.jeeagile.core.protocol.annotation.AgileProvider;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysUser;
import com.jeeagile.system.service.IAgileSysUserService;
import com.jeeagile.system.vo.AgileSysUserInfo;
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
@Api(value = "用户管理", tags = "用户管理")
public class AgileSysUserController extends AgileBaseController {

    @AgileProvider
    private IAgileSysUserService agileSysUserService;

    @PostMapping(value = "/selectPage")
    @ApiOperation(value = "分页查询用户列表", notes = "分页查询用户列表")
    @AgileLogger(title = "查询用户列表", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileSysUser>> selectUserPage(@RequestBody AgilePageable<AgileSysUser> agilePageable) {
        return this.rtnSuccess(agileSysUserService.selectUserPage(agilePageable));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据用户ID获取用户详细信息", notes = "根据用户ID获取用户详细信息")
    @AgileLogger(title = "查看用户信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysUser> detailUser(@SingleRequestBody String userId) {
        return this.rtnSuccess(agileSysUserService.selectUserById(userId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @AgileLogger(title = "新增用户", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:user:add")
    public AgileResult<AgileSysUser> addUser(@RequestBody AgileSysUserInfo agileSysUserInfo) {
        return this.rtnSuccess(agileSysUserService.saveUser(agileSysUserInfo));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @AgileLogger(title = "修改用户信息", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:user:update")
    public AgileResult<Object> updateUser(@RequestBody AgileSysUserInfo agileSysUserInfo) {
        agileSysUserService.updateUserById(agileSysUserInfo);
        return this.rtnSuccess("用户信息修改成功！");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    @AgileLogger(title = "删除用户信息", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:user:delete")
    public AgileResult<Object> delete(@SingleRequestBody String userId) {
        agileSysUserService.deleteUserById(userId);
        return this.rtnSuccess("用户信息删除成功！");
    }

    @AgileDemo
    @PostMapping(value = "/resetPwd")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @AgileLogger(title = "重置用户密码", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:user:resetPwd")
    public AgileResult<Object> resetUserPassword(@RequestBody AgileUpdatePwd agileUpdatePwd) {
        agileSysUserService.resetUserPwd(agileUpdatePwd);
        AgileCacheUtil.evict(AgileCacheConstants.AGILE_CACHE_AUTHENTICATION_NAME,"test");
        return this.rtnSuccess();
    }

    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新用户状态", notes = "更新用户状态")
    @AgileLogger(title = "更新用户状态", type = AgileLoggerType.UPDATE)
    public AgileResult<Object> changeUserStatus(@RequestBody AgileUpdateStatus agileUpdateStatus) {
        agileSysUserService.changeUserStatus(agileUpdateStatus);
        return this.rtnSuccess();
    }

}
