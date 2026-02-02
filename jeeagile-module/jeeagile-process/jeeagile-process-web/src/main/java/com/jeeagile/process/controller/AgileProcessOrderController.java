package com.jeeagile.process.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessOrder;
import com.jeeagile.process.service.IAgileProcessOrderService;
import com.jeeagile.process.vo.AgileProcessHistory;
import com.jeeagile.process.vo.OnlineOrderQueryParam;
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
@RequestMapping("/process/order")
@Api(value = "流程实例", tags = "流程实例")
public class AgileProcessOrderController extends AgileBaseController {
    @AgileReference
    private IAgileProcessOrderService agileProcessOrderService;

    @PostMapping(value = "/apply")
    @ApiOperation(value = "查询我的发起", notes = "查询我的发起")
    public AgileResult<AgilePage<AgileProcessOrder>> selectOrderPage(@RequestBody AgilePageable<AgileProcessOrder> agilePageable) {
        return AgileResult.success(agileProcessOrderService.selectOrderPage(agilePageable));
    }

    @PostMapping(value = "/detail")
    @ApiOperation(value = "查看流程工单信息", notes = "查看流程工单信息")
    public AgileResult<AgileProcessOrder> detail(@SingleRequestBody String orderId) {
        return AgileResult.success(agileProcessOrderService.selectOrderInfo(orderId));
    }

    @PostMapping(value = "/start")
    @ApiOperation(value = "启动流程工单", notes = "启动流程工单")
    public AgileResult<AgileProcessOrder> startProcess(@SingleRequestBody String processDefinitionId, @SingleRequestBody Map<String, Object> orderData) {
        return AgileResult.success(agileProcessOrderService.startProcess(processDefinitionId, orderData));
    }

    @PostMapping(value = "/history")
    @ApiOperation(value = "查看工单历史审批", notes = "查看工单历史审批")
    public AgileResult<List<AgileProcessHistory>> history(@SingleRequestBody String orderId) {
        return AgileResult.success(agileProcessOrderService.selectOrderHistory(orderId));
    }

    @PostMapping(value = "/cancel")
    @ApiOperation(value = "工单撤销", notes = "工单撤销")
    public AgileResult<AgileProcessOrder> cancel(@SingleRequestBody String orderId) {
        return AgileResult.success(agileProcessOrderService.cancelOrder(orderId));
    }

    @PostMapping(value = "/selectOnlineOrderList")
    @ApiOperation(value = "查询流程在线工单列表", notes = "查询流程在线工单列表")
    public AgileResult<Map> selectOnlineOrderList(@RequestBody AgilePageable<OnlineOrderQueryParam> agilePageable) {
        return AgileResult.success(agileProcessOrderService.selectOnlineOrderList(agilePageable));
    }

}
