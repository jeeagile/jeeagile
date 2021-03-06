package com.jeeagile.quartz.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.service.IAgileQuartzJobService;
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
@AgileLogger("定时任务")
@RequestMapping("/quartz/job")
@AgilePermissionsPrefix("quartz:job")
@Api(value = "定时任务", tags = "定时任务")
public class AgileQuartzJobController extends AgileCrudController<IAgileQuartzJobService, AgileQuartzJob> {
    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新任务状态", notes = "更新任务状态")
    @AgileLogger(notes = "更新任务状态", type = AgileLoggerType.UPDATE)
    public AgileResult<String> changeJobStatus(@RequestBody AgileQuartzJob agileQuartzJob) {
        this.agileBaseService.changeQuartzJobStatus(agileQuartzJob);
        return this.success();
    }

    @PostMapping("/execute")
    @ApiOperation(value = "执行任务", notes = "执行任务")
    @AgileLogger(notes = "执行任务", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("execute")
    public AgileResult execute(@SingleRequestBody String quartzJobId) {
        this.agileBaseService.executeQuartzJob(quartzJobId);
        return this.success("执行任务成功！");
    }
}
