package com.jeeagile.process.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.service.IAgileProcessDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2022-06-06
 * @description 流程定义
 */
@RestController
@AgileLogger("流程定义")
@RequestMapping("/process/definition")
@AgilePermissionsPrefix("process:definition")
@Api(value = "流程定义", tags = "流程定义")
public class AgileProcessDefinitionController extends AgileCrudController<IAgileProcessDefinitionService, AgileProcessDefinition> {
    @PostMapping("/active")
    @AgileRequiresPermissions("active")
    @AgileLogger(notes = "流程定义激活", type = AgileLoggerType.UPDATE)
    @ApiOperation(value = "流程定义激活", notes = "流程定义激活")
    public AgileResult<AgileProcessDefinition> active(@SingleRequestBody String id) {
        try {
            return AgileResult.success(this.getAgileBaseService().processDefinitionActive(id));
        } catch (Exception ex) {
            return AgileResult.error(ex, "流程定义激活异常！");
        }
    }

    @PostMapping("/suspend")
    @AgileRequiresPermissions("suspend")
    @AgileLogger(notes = "流程定义挂起", type = AgileLoggerType.UPDATE)
    @ApiOperation(value = "流程定义挂起", notes = "流程定义挂起")
    public AgileResult<AgileProcessDefinition> suspend(@SingleRequestBody String id) {
        try {
            return AgileResult.success(this.getAgileBaseService().processDefinitionSuspend(id));
        } catch (Exception ex) {
            return AgileResult.error(ex, "流程定义挂起异常！");
        }
    }
}
