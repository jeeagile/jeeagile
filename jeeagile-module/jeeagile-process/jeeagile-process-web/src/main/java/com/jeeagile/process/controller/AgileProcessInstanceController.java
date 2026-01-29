package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessInstance;
import com.jeeagile.process.service.IAgileProcessInstanceService;
import com.jeeagile.process.vo.AgileProcessHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AgileLogger("流程实例")
@RequestMapping("/process/instance")
@Api(value = "流程实例", tags = "流程实例")
public class AgileProcessInstanceController extends AgileBaseController {
    @AgileReference
    private IAgileProcessInstanceService agileProcessInstanceService;


    @PostMapping(value = "/apply")
    @ApiOperation(value = "查询我的发起", notes = "查询我的发起")
    public AgileResult<AgilePage<AgileProcessInstance>> selectProcessInstancePage(@RequestBody AgilePageable<AgileProcessInstance> agilePageable) {
        return AgileResult.success(agileProcessInstanceService.selectProcessInstancePage(agilePageable));
    }

    @PostMapping(value = "/detail")
    @ApiOperation(value = "查看流程实例信息", notes = "查看流程实例信息")
    public AgileResult<AgileProcessInstance> detail(@SingleRequestBody String processInstanceId) {
        return AgileResult.success(agileProcessInstanceService.selectProcessInstanceInfo(processInstanceId));
    }

    @PostMapping(value = "/start")
    @ApiOperation(value = "启动流程实例", notes = "启动流程实例")
    public AgileResult<AgileProcessInstance> startProcessInstance(@SingleRequestBody String processDefinitionId, @SingleRequestBody Map<String, Object> formData) {
        return AgileResult.success(agileProcessInstanceService.startProcessInstance(processDefinitionId,formData));
    }

    @PostMapping(value = "/history")
    @ApiOperation(value = "查看流程实例历史审批", notes = "查看流程实例历史审批")
    public AgileResult<List<AgileProcessHistory>> history(@SingleRequestBody String processInstanceId) {
        return AgileResult.success(agileProcessInstanceService.selectProcessInstanceHistory(processInstanceId));
    }

    @PostMapping(value = "/cancel")
    @ApiOperation(value = "流程撤销", notes = "流程撤销")
    public AgileResult<AgileProcessInstance> cancel(@SingleRequestBody String instanceId) {
        try {
            return AgileResult.success(agileProcessInstanceService.cancelProcessInstance(instanceId));
        } catch (Exception ex) {
            return AgileResult.error(ex, "流程撤销异常！");
        }
    }
}
