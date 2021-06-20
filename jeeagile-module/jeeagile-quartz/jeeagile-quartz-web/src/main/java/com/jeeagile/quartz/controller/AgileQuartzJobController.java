package com.jeeagile.quartz.controller;

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
import com.jeeagile.quartz.entity.AgileQuartzJob;
import com.jeeagile.quartz.service.IAgileQuartzJobService;
import com.jeeagile.quartz.vo.AgileUpdateStatus;
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
@RequestMapping("/quartz/job")
@Api(value = "定时任务管理", tags = "定时任务管理")
public class AgileQuartzJobController extends AgileBaseController {
    @AgileProvider
    private IAgileQuartzJobService agileQuartzJobService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询定时任务列表", notes = "分页查询定时任务列表")
    @AgileLogger(title = "分页查询定时任务", type = AgileLoggerType.SELECT)
    public AgileResult<AgilePage<AgileQuartzJob>> selectPage(@RequestBody AgilePageable<AgileQuartzJob> agilePageable) {
        return this.rtnSuccess(agileQuartzJobService.selectQuartzJobPage(agilePageable));
    }

    @PostMapping("/detail")
    @ApiOperation(value = "根据任务ID查询定时任务详细信息", notes = "根据任务ID查询定时任务详细信息")
    @AgileLogger(title = "查看定时任务信息", type = AgileLoggerType.DETAIL)
    public AgileResult<AgileQuartzJob> detail(@SingleRequestBody String quartzJobId) {
        return this.rtnSuccess(agileQuartzJobService.selectQuartzJobById(quartzJobId));
    }

    @AgileDemo
    @PostMapping("/add")
    @ApiOperation(value = "新增定时任务", notes = "新增定时任务")
    @AgileLogger(title = "新增定时任务", type = AgileLoggerType.ADD)
    @AgileRequiresPermissions("quartz:job:add")
    public AgileResult<AgileQuartzJob> save(@RequestBody AgileQuartzJob agileQuartzJob) {
        return this.rtnSuccess(agileQuartzJobService.saveQuartzJob(agileQuartzJob));
    }

    @AgileDemo
    @PostMapping("/update")
    @ApiOperation(value = "编辑定时任务信息", notes = "编辑定时任务信息")
    @AgileLogger(title = "编辑定时任务信息", type = AgileLoggerType.UPDATE)
    @AgileRequiresPermissions("quartz:job:update")
    public AgileResult update(@RequestBody AgileQuartzJob agileQuartzJob) {
        agileQuartzJobService.updateQuartzJobById(agileQuartzJob);
        return this.rtnSuccess("定时任务信息更新成功！");
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除定时任务", notes = "删除定时任务")
    @AgileLogger(title = "删除定时任务", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("quartz:job:delete")
    public AgileResult delete(@SingleRequestBody String quartzJobId) {
        agileQuartzJobService.deleteQuartzJobById(quartzJobId);
        return this.rtnSuccess("定时任务信息删除成功！");
    }

    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新任务状态", notes = "更新任务状态")
    @AgileLogger(title = "更新任务状态", type = AgileLoggerType.UPDATE)
    public AgileResult<String> changeRoleStatus(@RequestBody AgileUpdateStatus agileUpdateStatus) {
        agileQuartzJobService.changeQuartzJobStatus(agileUpdateStatus);
        return this.rtnSuccess();
    }

    @PostMapping("/execute")
    @ApiOperation(value = "执行任务", notes = "执行任务")
    @AgileLogger(title = "执行任务", type = AgileLoggerType.DELETE)
    @AgileRequiresPermissions("quartz:job:execute")
    public AgileResult execute(@SingleRequestBody String quartzJobId) {
        agileQuartzJobService.deleteQuartzJobById(quartzJobId);
        return this.rtnSuccess("定时任务信息删除成功！");
    }
}
