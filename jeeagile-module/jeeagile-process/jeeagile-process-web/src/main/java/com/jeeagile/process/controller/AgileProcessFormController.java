package com.jeeagile.process.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.process.entity.AgileProcessForm;
import com.jeeagile.process.service.IAgileProcessFormService;
import io.swagger.annotations.Api;
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
}
