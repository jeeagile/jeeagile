package com.jeeagile.system.controller;

import com.jeeagile.core.protocol.annotation.AgileProvider;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.system.entity.AgileSysConfig;
import com.jeeagile.system.service.IAgileSysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 参数管理 前端控制器
 */
@RestController
@RequestMapping("/system/config")
@Api(value = "参数管理", tags = "参数管理")
public class AgileSysConfigController extends AgileBaseController {

    @AgileProvider
    private IAgileSysConfigService agileSysConfigService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询参数配置列表", notes = "分页查询参数配置列表")
    @AgileLogger(title = "分页查询参数配置", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileSysConfig>> selectPage(@RequestBody AgilePageable<AgileSysConfig> agilePageable) {
        return this.rtnSuccess(agileSysConfigService.selectConfigPage(agilePageable));
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "查询参数配置列表", notes = "查询参数配置列表")
    @AgileLogger(title = "查询参数配置列表", type = AgileLoggerType.SELECT)
    public AgileResult<List<AgileSysConfig>> selectList(@RequestBody AgileSysConfig agileSysConfig) {
        return this.rtnSuccess(agileSysConfigService.selectConfigList(agileSysConfig));
    }

    @PostMapping("/getConfigByKey")
    @ApiOperation(value = "根据参数键值查询参数配置信息", notes = "根据参数键值查询参数配置信息")
    public AgileResult<AgileSysConfig> getConfigByKey(@SingleRequestBody String configKey) {
        return this.rtnSuccess(agileSysConfigService.selectConfigByConfigKey(configKey));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据参数配置ID查询参数配置详细信息", notes = "根据参数配置ID查询参数配置详细信息")
    @AgileLogger(title = "查询参数配置信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileSysConfig> detail(@SingleRequestBody String configId) {
        return this.rtnSuccess(agileSysConfigService.selectConfigById(configId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增参数配置", notes = "新增参数配置")
    @AgileLogger(title = "新增参数配置", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("system:config:add")
    public AgileResult<AgileSysConfig> save(@RequestBody AgileSysConfig agileSysConfig) {
        return this.rtnSuccess(agileSysConfigService.saveConfig(agileSysConfig));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "编辑参数配置信息", notes = "编辑参数配置信息")
    @AgileLogger(title = "编辑参数配置信息", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("system:config:update")
    public AgileResult update(@RequestBody AgileSysConfig agileSysConfig) {
        agileSysConfigService.updateConfigById(agileSysConfig);
        return this.rtnSuccess("参数配置信息更新成功！");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除参数配置", notes = "删除参数配置")
    @AgileLogger(title = "删除参数配置", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("system:config:delete")
    public AgileResult delete(@SingleRequestBody String configId) {
        agileSysConfigService.deleteConfigById(configId);
        return this.rtnSuccess("参数配置信息删除成功！");
    }


}
