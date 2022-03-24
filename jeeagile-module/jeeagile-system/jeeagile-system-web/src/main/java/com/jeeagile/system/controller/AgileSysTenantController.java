package com.jeeagile.system.controller;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.system.entity.AgileSysTenant;
import com.jeeagile.system.service.IAgileSysTenantService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 参数管理 前端控制器
 */
@RestController
@AgileLogger("租户管理")
@RequestMapping("/system/tenant")
@AgilePermissionsPrefix("system:tenant")
@Api(value = "租户管理", tags = "租户管理")
public class AgileSysTenantController extends AgileCrudController<IAgileSysTenantService, AgileSysTenant> {
}
