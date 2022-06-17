package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.process.entity.AgileProcessTask;
import com.jeeagile.process.service.IAgileProcessTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AgileLogger("流程任务")
@RequestMapping("/process/task")
@Api(value = "流程任务", tags = "流程任务")
public class AgileProcessTaskController extends AgileBaseController {
    @AgileReference
    private IAgileProcessTaskService agileProcessTaskService;

    @PostMapping(value = "/todo")
    @ApiOperation(value = "查询我的代办", notes = "查询我的代办")
    public AgileResult<AgilePage<AgileProcessTask>> selectMainVersion(@RequestBody AgilePageable<AgileProcessTask> agilePageable) {
        return AgileResult.success(agileProcessTaskService.selectTodo(agilePageable));
    }

}
