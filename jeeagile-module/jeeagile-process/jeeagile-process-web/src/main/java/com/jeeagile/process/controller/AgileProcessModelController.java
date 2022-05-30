package com.jeeagile.process.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.service.IAgileProcessModelService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
@RestController
@AgileLogger("流程模型")
@RequestMapping("/process/model")
@AgilePermissionsPrefix("process:model")
@Api(value = "流程模型", tags = "流程模型")
public class AgileProcessModelController extends AgileCrudController<IAgileProcessModelService, AgileProcessModel> {
}
