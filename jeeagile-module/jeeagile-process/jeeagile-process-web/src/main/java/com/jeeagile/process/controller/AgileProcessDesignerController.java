package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessScript;
import com.jeeagile.process.service.IAgileProcessDesignerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AgileLogger("流程表单")
@RequestMapping("/process/designer")
@AgilePermissionsPrefix("process:designer")
@Api(value = "流程设计", tags = "流程设计")
public class AgileProcessDesignerController extends AgileBaseController {
    @AgileReference
    private IAgileProcessDesignerService agileProcessDesignerService;

    @PostMapping(value = "/selectUserPage")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    public AgileResult<AgilePage<AgileSysUser>> selectUserPage(@RequestBody AgilePageable<AgileSysUser> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectUserPage(agilePageable));
    }

    @PostMapping(value = "/selectRolePage")
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    public AgileResult<AgilePage<AgileSysUser>> selectRolePage(@RequestBody AgilePageable<AgileSysRole> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectRolePage(agilePageable));
    }

    @PostMapping(value = "/selectDeptPage")
    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    public AgileResult<AgilePage<AgileSysDept>> selectDeptPage(@RequestBody AgilePageable<AgileSysDept> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectDeptPage(agilePageable));
    }

    @PostMapping(value = "/selectPostPage")
    @ApiOperation(value = "获取岗位列表", notes = "获取岗位列表")
    public AgileResult<AgilePage<AgileSysPost>> selectPostPage(@RequestBody AgilePageable<AgileSysPost> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectPostPage(agilePageable));
    }

    @PostMapping(value = "/selectGroupPage")
    @ApiOperation(value = "获取用户分组列表", notes = "获取用户分组列表")
    public AgileResult<AgilePage<AgileSysGroup>> selectGroupPage(@RequestBody AgilePageable<AgileSysGroup> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectGroupPage(agilePageable));
    }

    @PostMapping(value = "/selectScriptPage")
    @ApiOperation(value = "获取脚本列表", notes = "获取脚本列表")
    public AgileResult<AgilePage<AgileProcessScript>> selectScriptPage(@RequestBody AgilePageable<AgileProcessScript> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectScriptPage(agilePageable));
    }

    @PostMapping(value = "/detailUserNickName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<AgileSysUser>> detailUserNickName(@SingleRequestBody String userIds) {
        return AgileResult.success(agileProcessDesignerService.detailUserNickName(userIds));
    }

    @PostMapping(value = "/detailRoleName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailRoleName(@SingleRequestBody String roleCodes) {
        return AgileResult.success(agileProcessDesignerService.detailRoleName(roleCodes));
    }

    @PostMapping(value = "/detailDeptName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailDeptName(@SingleRequestBody String deptCodes) {
        return AgileResult.success(agileProcessDesignerService.detailDeptName(deptCodes));
    }

    @PostMapping(value = "/detailPostName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailPostName(@SingleRequestBody String postCodes) {
        return AgileResult.success(agileProcessDesignerService.detailPostName(postCodes));
    }

    @PostMapping(value = "/detailGroupName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailGroupName(@SingleRequestBody String groupCodes) {
        return AgileResult.success(agileProcessDesignerService.detailGroupName(groupCodes));
    }

    @PostMapping(value = "/detailScriptName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailScriptName(@SingleRequestBody String scriptCodes) {
        return AgileResult.success(agileProcessDesignerService.detailScriptName(scriptCodes));
    }
}
