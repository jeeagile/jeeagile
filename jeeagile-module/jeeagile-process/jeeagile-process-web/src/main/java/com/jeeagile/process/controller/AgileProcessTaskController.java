package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessDefinition;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.service.IAgileProcessTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AgileLogger("我的事务")
@RequestMapping("/process/task")
@Api(value = "我的事务", tags = "我的事务")
public class AgileProcessTaskController extends AgileBaseController {
    @AgileReference
    private IAgileProcessTaskService agileProcessTaskService;

    @PostMapping(value = "/selectProcessDefinitionPage")
    @ApiOperation(value = "查询可发起流程列表", notes = "查询可发起流程列表")
    public AgileResult<AgilePage<AgileProcessDefinition>> selectProcessDefinitionPage(@RequestBody AgilePageable<AgileProcessDefinition> agilePageable) {
        return AgileResult.success(agileProcessTaskService.selectProcessDefinitionPage(agilePageable));
    }

    @PostMapping(value = "/detailProcessDefinition")
    @ApiOperation(value = "查看流程定义信息", notes = "查看流程定义信息")
    public AgileResult<AgileProcessDefinition> detailProcessDefinition(@SingleRequestBody String processDefinitionId) {
        return AgileResult.success(agileProcessTaskService.selectProcessDefinitionInfo(processDefinitionId));
    }

    @PostMapping(value = "/startProcessInstance")
    @ApiOperation(value = "查看流程定义信息", notes = "查看流程定义信息")
    public AgileResult<AgileProcessDefinition> startProcessInstance(@SingleRequestBody String processDefinitionId, @SingleRequestBody Map<String, Object> formData) {
        return AgileResult.success(agileProcessTaskService.startProcessInstance(processDefinitionId,formData));
    }

    @PostMapping(value = "/selectProcessInstancePage")
    @ApiOperation(value = "查询我的发起", notes = "查询我的发起")
    public AgileResult<AgilePage<AgileProcessInstance>> selectProcessInstancePage(@RequestBody AgilePageable<AgileProcessInstance> agilePageable) {
        return AgileResult.success(agileProcessTaskService.selectProcessInstancePage(agilePageable));
    }

}
