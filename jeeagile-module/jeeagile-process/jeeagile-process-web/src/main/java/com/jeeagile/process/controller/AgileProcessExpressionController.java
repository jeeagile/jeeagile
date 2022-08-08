package com.jeeagile.process.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.process.entity.AgileProcessExpression;
import com.jeeagile.process.service.IAgileProcessExpressionService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2022-08-08
 * @description 流程表达式
 */
@RestController
@AgileLogger("流程表达式")
@RequestMapping("/process/expression")
@AgilePermissionsPrefix("process:expression")
@Api(value = "流程表达式", tags = "流程表达式")
public class AgileProcessExpressionController extends AgileCrudController<IAgileProcessExpressionService, AgileProcessExpression> {

}
