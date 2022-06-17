package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.process.service.IAgileProcessTaskService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AgileLogger("流程任务")
@RequestMapping("/process/task")
@Api(value = "流程任务", tags = "流程任务")
public class AgileProcessTaskController extends AgileBaseController {
    @AgileReference
    private IAgileProcessTaskService agileProcessTaskService;


}
