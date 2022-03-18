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
import com.jeeagile.quartz.entity.AgileQuartzLogger;
import com.jeeagile.quartz.service.IAgileQuartzLoggerService;
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
@AgileLogger("任务日志")
@RequestMapping("/quartz/logger")
@Api(value = "任务日志", tags = "任务日志")
public class AgileQuartzLoggerController extends AgileBaseController {
    @AgileReference
    private IAgileQuartzLoggerService agileQuartzLoggerService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @AgileLogger(notes = "分页查询任务执行日志", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileQuartzLogger>> selectPage(@RequestBody AgilePageable<AgileQuartzLogger> agilePageable) {
        return this.success(agileQuartzLoggerService.selectPage(agilePageable));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "查看信息", notes = "查看信息")
    @AgileLogger(notes = "查看任务执行日志详细信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileQuartzLogger> detail(@SingleRequestBody String quartzLogId) {
        return this.success(agileQuartzLoggerService.selectModel(quartzLogId));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @AgileLogger(notes = "删除任务执行日志数据", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String quartzLogId) {
        agileQuartzLoggerService.deleteModel(quartzLogId);
        return this.success("日志记录删除成功！");
    }

    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空任务执行日志记录", notes = "清空任务执行日志记录")
    @AgileLogger(notes = "清空任务执行日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileQuartzLoggerService.clearRecord();
        return this.success("日志记录清空成功！");
    }
}
