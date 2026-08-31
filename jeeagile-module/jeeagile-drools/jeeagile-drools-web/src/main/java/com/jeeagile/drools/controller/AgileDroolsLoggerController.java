package com.jeeagile.drools.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.drools.entity.AgileDroolsSceneLogger;
import com.jeeagile.drools.service.IAgileDroolsSceneLoggerService;
import com.jeeagile.drools.vo.AgileDroolsLoggerInfo;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.entity.system.AgileSysOperateLogger;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @创建人 wangcy
 * @创建日期 2024-02-29
 * @描述 规则引擎 执行日志 前端控制器
 */
@RestController
@RequestMapping("/drools/logger")
@Api(value = "规则引擎 执行日志管理", tags = "规则引擎 执行日志管理")
public class AgileDroolsLoggerController extends AgileBaseController {
    @AgileReference
    private IAgileDroolsSceneLoggerService agileDroolsSceneLoggerService;

    @PostMapping(value = "/page")
    @AgileRequiresPermissions("drools:logger:page")
    @ApiOperation(value = "分页查询执行日志列表", notes = "分页查询执行日志列表")
    public AgileResult<AgilePage<AgileDroolsSceneLogger>> selectPage(@RequestBody AgilePageable<AgileDroolsSceneLogger> agilePageable) {
        return this.success(agileDroolsSceneLoggerService.selectPage(agilePageable));
    }


    @PostMapping("/detail")
    @AgileRequiresPermissions("drools:logger:detail")
    @ApiOperation(value = "根据执行日志ID查询执行日志详细信息", notes = "根据执行日志ID查询执行日志详细信息")
    public AgileResult<AgileDroolsLoggerInfo> detailLogger(@SingleRequestBody String loggerId) {
        return this.success(agileDroolsSceneLoggerService.loggerInfo(loggerId));
    }

    @PostMapping("/statistic")
    @AgileRequiresPermissions("drools:logger:statistic")
    @ApiOperation(value = "日志统计信息", notes = "日志统计信息")
    public AgileResult<Map<String, Object>> statisticInfo(@RequestBody AgileDroolsSceneLogger agileDroolsSceneLogger) {
        return this.success(agileDroolsSceneLoggerService.statisticInfo(agileDroolsSceneLogger));
    }
}
