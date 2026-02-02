package com.jeeagile.process.controller;

import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.*;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessDesigner;
import com.jeeagile.process.entity.AgileProcessExpression;
import com.jeeagile.process.entity.AgileProcessScript;
import com.jeeagile.process.service.IAgileProcessDesignerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AgileLogger("流程设计")
@RequestMapping("/process/designer")
@Api(value = "流程设计", tags = "流程设计")
public class AgileProcessDesignerController extends AgileCrudController<IAgileProcessDesignerService, AgileProcessDesigner> {

    @AgileDemo
    @PostMapping("/xml")
    @AgileRequiresPermissions("xml")
    @AgileLogger(notes = "保存流程设计", type = AgileOperateType.UPDATE)
    @ApiOperation(value = "保存流程设计", notes = "保存流程设计")
    public AgileResult<AgileProcessDesigner> xml(@SingleRequestBody String processId, @SingleRequestBody String processXml) {
        return AgileResult.success(agileService.saveProcessXml(processId, processXml));
    }

    @AgileDemo
    @PostMapping("/deployment")
    @AgileRequiresPermissions("deployment")
    @AgileLogger(notes = "流程发布", type = AgileOperateType.UPDATE)
    @ApiOperation(value = "流程发布", notes = "流程发布")
    public AgileResult<AgileProcessDesigner> deployment(@SingleRequestBody String processId) {
        if (agileService.processDeployment(processId)) {
            return AgileResult.success("流程发布成功！");
        } else {
            return AgileResult.error(AgileResultCode.FAIL_OPS, "流程发布失败！");
        }
    }

    @PostMapping("/selectProcessOnlinePageList")
    @ApiOperation(value = "查询流程在线表单", notes = "查询流程在线表单")
    public AgileResult<Object> selectProcessOnlinePageList() {
        return this.success(agileService.selectProcessOnlinePageList());
    }

    @PostMapping(value = "/selectUserPage")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    public AgileResult<AgilePage<AgileSysUser>> selectUserPage(@RequestBody AgilePageable<AgileSysUser> agilePageable) {
        return AgileResult.success(agileService.selectUserPage(agilePageable));
    }

    @PostMapping(value = "/selectRolePage")
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    public AgileResult<AgilePage<AgileSysUser>> selectRolePage(@RequestBody AgilePageable<AgileSysRole> agilePageable) {
        return AgileResult.success(agileService.selectRolePage(agilePageable));
    }

    @PostMapping(value = "/selectDeptPage")
    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    public AgileResult<AgilePage<AgileSysDept>> selectDeptPage(@RequestBody AgilePageable<AgileSysDept> agilePageable) {
        return AgileResult.success(agileService.selectDeptPage(agilePageable));
    }

    @PostMapping(value = "/selectPostPage")
    @ApiOperation(value = "获取岗位列表", notes = "获取岗位列表")
    public AgileResult<AgilePage<AgileSysPost>> selectPostPage(@RequestBody AgilePageable<AgileSysPost> agilePageable) {
        return AgileResult.success(agileService.selectPostPage(agilePageable));
    }

    @PostMapping(value = "/selectGroupPage")
    @ApiOperation(value = "获取用户分组列表", notes = "获取用户分组列表")
    public AgileResult<AgilePage<AgileSysGroup>> selectGroupPage(@RequestBody AgilePageable<AgileSysGroup> agilePageable) {
        return AgileResult.success(agileService.selectGroupPage(agilePageable));
    }

    @PostMapping(value = "/selectExpressionPage")
    @ApiOperation(value = "获取表达式列表", notes = "获取表达式列表")
    public AgileResult<AgilePage<AgileProcessExpression>> selectExpressionPage(@RequestBody AgilePageable<AgileProcessExpression> agilePageable) {
        return AgileResult.success(agileService.selectExpressionPage(agilePageable));
    }

    @PostMapping(value = "/selectScriptPage")
    @ApiOperation(value = "获取脚本列表", notes = "获取脚本列表")
    public AgileResult<AgilePage<AgileProcessScript>> selectScriptPage(@RequestBody AgilePageable<AgileProcessScript> agilePageable) {
        return AgileResult.success(agileService.selectScriptPage(agilePageable));
    }

    @PostMapping(value = "/detailUserNickName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<AgileSysUser>> detailUserNickName(@SingleRequestBody List<String> userIds) {
        return AgileResult.success(agileService.detailUserNickName(userIds));
    }

    @PostMapping(value = "/detailRoleName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailRoleName(@SingleRequestBody List<String> roleIds) {
        return AgileResult.success(agileService.detailRoleName(roleIds));
    }

    @PostMapping(value = "/detailDeptName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailDeptName(@SingleRequestBody List<String> deptIds) {
        return AgileResult.success(agileService.detailDeptName(deptIds));
    }

    @PostMapping(value = "/detailPostName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailPostName(@SingleRequestBody List<String> postIds) {
        return AgileResult.success(agileService.detailPostName(postIds));
    }

    @PostMapping(value = "/detailGroupName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailGroupName(@SingleRequestBody List<String> groupIds) {
        return AgileResult.success(agileService.detailGroupName(groupIds));
    }

    @PostMapping(value = "/detailScriptName")
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<String>> detailScriptName(@SingleRequestBody List<String> scriptIds) {
        return AgileResult.success(agileService.detailScriptName(scriptIds));
    }
}
