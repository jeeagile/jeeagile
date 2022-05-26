package com.jeeagile.frame.controller.system;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.frame.entity.system.AgileSysConfig;
import com.jeeagile.frame.entity.system.AgileSysDictData;
import com.jeeagile.frame.service.system.IAgileSysConfigService;
import com.jeeagile.frame.service.system.IAgileSysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AgileLogger("参数管理")
@RequestMapping("/system/common")
@Api(value = "公用接口", tags = "公用接口")
public class AgileSysCommonController extends AgileBaseController {
    @AgileReference
    private IAgileSysConfigService agileSysConfigService;

    @AgileReference
    private IAgileSysDictDataService agileSysDictDataService;

    @PostMapping("/getSysConfig")
    @ApiOperation(value = "获取系统参数配置信息", notes = "获取系统参数配置信息")
    public AgileResult<AgileSysConfig> getSysConfig(@SingleRequestBody String configKey) {
        return this.success(agileSysConfigService.getSysConfig(configKey));
    }

    @PostMapping("/getSysConfigValue")
    @ApiOperation(value = "获取系统参数配置信息", notes = "获取系统参数配置信息")
    public AgileResult<String> getConfigValue(@SingleRequestBody String configKey) {
        return this.success(agileSysConfigService.getSysConfigValue(configKey));
    }

    @PostMapping("/getSysDictData")
    @ApiOperation(value = "根据字典类型查询字典数据列表", notes = "根据字典类型查询字典数据列表")
    public AgileResult<AgileSysDictData> getSysDictData(@SingleRequestBody String dictType, @SingleRequestBody String dictValue) {
        return this.success(agileSysDictDataService.getSysDictData(dictType, dictValue));
    }

    @PostMapping("/getSysDictDataList")
    @ApiOperation(value = "根据字典类型查询字典数据列表", notes = "根据字典类型查询字典数据列表")
    public AgileResult<AgileSysDictData> getSysDictDataList(@SingleRequestBody String dictType) {
        return this.success(agileSysDictDataService.getSysDictDataList(dictType));
    }
}
