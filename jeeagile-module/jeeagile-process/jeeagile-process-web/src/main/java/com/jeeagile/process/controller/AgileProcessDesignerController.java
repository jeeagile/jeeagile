package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.entity.system.AgileSysUser;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
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
    @AgileRequiresPermissions("user")
    @AgileLogger(notes = "获取用户信息", type = AgileLoggerType.SELECT)
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public AgileResult<AgilePage<AgileSysUser>> selectUserPage(@RequestBody AgilePageable<AgileSysUser> agilePageable) {
        return AgileResult.success(agileProcessDesignerService.selectUserPage(agilePageable));
    }

    @PostMapping(value = "/detailUserNickName")
    @AgileRequiresPermissions("user")
    @AgileLogger(notes = "获取用户昵称", type = AgileLoggerType.SELECT)
    @ApiOperation(value = "获取用户昵称", notes = "获取用户昵称")
    public AgileResult<AgilePage<AgileSysUser>> detailUserNickName(@SingleRequestBody String userIds) {
        return AgileResult.success(agileProcessDesignerService.detailUserNickName(userIds));
    }
}
