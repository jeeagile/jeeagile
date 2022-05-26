package com.jeeagile.frame.controller.system;

import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.entity.system.AgileSysConfig;
import com.jeeagile.frame.service.system.IAgileSysConfigService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 参数管理 前端控制器
 */
@RestController
@AgileLogger("参数管理")
@RequestMapping("/system/config")
@AgilePermissionsPrefix("system:config")
@Api(value = "参数管理", tags = "参数管理")
public class AgileSysConfigController extends AgileCrudController<IAgileSysConfigService, AgileSysConfig> {

}
