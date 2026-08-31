package com.jeeagile.drools.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.drools.entity.AgileDroolsModelField;
import com.jeeagile.drools.service.IAgileDroolsModelFieldService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2026-03-13 15:26:20
 * @description 规则引擎 数据对象字段 前端控制器
 */
@RestController
@AgileLogger("规则引擎 数据对象字段")
@RequestMapping("/drools/field")
@AgilePermissionsPrefix("drools:field")
@Api(value = "规则引擎 数据对象字段", tags = "规则引擎 数据对象字段")
public class AgileDroolsModelFieldController extends AgileCrudController<IAgileDroolsModelFieldService, AgileDroolsModelField> {

}
