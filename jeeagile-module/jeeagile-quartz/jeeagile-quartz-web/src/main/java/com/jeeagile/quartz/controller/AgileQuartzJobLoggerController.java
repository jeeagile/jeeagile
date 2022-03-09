package com.jeeagile.quartz.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.quartz.entity.AgileQuartzJobLogger;
import com.jeeagile.quartz.service.IAgileQuartzJobLoggerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@RestController
@RequestMapping("/quartz/job/logger")
@Api(value = "任务执行日志管理", tags = "任务执行日志管理")
public class AgileQuartzJobLoggerController extends AgileBaseController {
    @AgileReference
    private IAgileQuartzJobLoggerService agileQuartzLoggerService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @AgileLogger(title = "分页查询", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileQuartzJobLogger>> selectPage(@RequestBody AgilePageable<AgileQuartzJobLogger> agilePageable) {
        return this.success(agileQuartzLoggerService.selectPage(agilePageable));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "查看信息", notes = "查看信息")
    @AgileLogger(title = "查看信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileQuartzJobLogger> detail(@SingleRequestBody String quartzLogId) {
        return this.success(agileQuartzLoggerService.selectModel(quartzLogId));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @AgileLogger(title = "删除数据", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String quartzLogId) {
        agileQuartzLoggerService.deleteModel(quartzLogId);
        return this.success("日志记录删除成功！");
    }

    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空日志记录", notes = "清空日志记录")
    @AgileLogger(title = "清空日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileQuartzLoggerService.clearRecord();
        return this.success("日志记录清空成功！");
    }
}
