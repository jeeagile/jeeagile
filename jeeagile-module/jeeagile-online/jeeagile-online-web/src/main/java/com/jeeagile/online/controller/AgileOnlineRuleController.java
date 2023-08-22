package com.jeeagile.online.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.online.entity.AgileOnlineRule;
import com.jeeagile.online.service.IAgileOnlineRuleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2023-08-21
 * @description 在线表单 规则配置 前端控制器
 */
@RestController
@AgileLogger("规则配置")
@RequestMapping("/online/rule")
@AgilePermissionsPrefix("online:rule")
@Api(value = "规则配置", tags = "规则配置")
public class AgileOnlineRuleController extends AgileCrudController<IAgileOnlineRuleService, AgileOnlineRule> {

}
