package com.jeeagile.quartz.controller;

import com.jeeagile.core.protocol.annotation.AgileProvider;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.quartz.entity.AgileQuartzLog;
import com.jeeagile.quartz.service.IAgileQuartzLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileQuartzLogController extends AgileBaseController {
    @AgileProvider
    private IAgileQuartzLogService agileQuartzLogService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询任务执行日志列表", notes = "分页查询任务执行日志列表")
    @AgileLogger(title = "分页查询任务执行日志列表", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileQuartzLog>> selectPage(@RequestBody AgilePageable<AgileQuartzLog> agilePageable) {
        return this.rtnSuccess(agileQuartzLogService.selectQuartzLogPage(agilePageable));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据日志执行ID获取执行详细信息", notes = "根据日志执行ID获取执行详细信息")
    @AgileLogger(title = "查看任务执行详细信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileQuartzLog> detail(@SingleRequestBody String quartzLogId) {
        return this.rtnSuccess(agileQuartzLogService.selectQuartzLogById(quartzLogId));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除日志记录", notes = "删除日志记录")
    @AgileLogger(title = "删除日志记录", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String quartzLogId) {
        agileQuartzLogService.deleteQuartzLog(quartzLogId);
        return this.rtnSuccess("日志记录删除成功！");
    }

    @PostMapping("/clear")
    @ApiOperation(value = "清空日志记录", notes = "清空日志记录")
    @AgileLogger(title = "清空日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileQuartzLogService.clearQuartzLog();
        return this.rtnSuccess("日志记录清空成功！");
    }
}
