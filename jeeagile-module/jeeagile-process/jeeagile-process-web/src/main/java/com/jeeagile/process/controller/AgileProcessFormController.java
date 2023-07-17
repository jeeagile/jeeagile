package com.jeeagile.process.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessForm;
import com.jeeagile.process.service.IAgileProcessFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程表单
 */
@RestController
@AgileLogger("流程表单")
@RequestMapping("/process/form")
@AgilePermissionsPrefix("process:form")
@Api(value = "流程表单", tags = "流程表单")
public class AgileProcessFormController extends AgileCrudController<IAgileProcessFormService, AgileProcessForm> {
    @PostMapping("/designer")
    @AgileRequiresPermissions("designer")
    @AgileLogger(notes = "保存流程表单设计", type = AgileLoggerType.UPDATE)
    @ApiOperation(value = "保存流程表单设计", notes = "保存流程表单设计")
    public AgileResult<AgileProcessForm> designer(@SingleRequestBody String formId, @SingleRequestBody String formConf, @SingleRequestBody String formFields) {
        return AgileResult.success(agileService.saveProcessFormDesigner(formId, formConf, formFields));
    }
}
