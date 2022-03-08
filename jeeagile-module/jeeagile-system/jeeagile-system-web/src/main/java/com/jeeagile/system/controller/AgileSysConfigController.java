package com.jeeagile.system.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysConfig;
import com.jeeagile.system.service.IAgileSysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 参数管理 前端控制器
 */
@RestController
@RequestMapping("/system/config")
@AgilePermissionsPrefix("system:config")
@Api(value = "参数管理", tags = "参数管理")
public class AgileSysConfigController extends AgileCrudController<IAgileSysConfigService, AgileSysConfig> {

    @PostMapping("/getConfigByKey")
    @ApiOperation(value = "查询参数配置信息", notes = "查询参数配置信息")
    public AgileResult<AgileSysConfig> getConfigByKey(@SingleRequestBody String configKey) {
        return this.success(this.getAgileService().selectConfigByConfigKey(configKey));
    }
}
