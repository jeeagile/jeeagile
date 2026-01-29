package com.jeeagile.frame.controller.online;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.util.AgileStringUtil;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.service.online.IAgileOnlineOperationService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.frame.vo.online.OnlineQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author JeeAgile
 * @date 2023-08-14
 * @description 在线表单 表单操作 前端控制器
 */
@RestController
@AgileLogger("表单操作")
@RequestMapping("/online/operation")
@Api(value = "表单操作", tags = "表单操作")
public class AgileOnlineOperationController extends AgileBaseController {
    @AgileReference
    private IAgileOnlineOperationService agileOnlineOperationService;

    @PostMapping(value = "/selectDictData")
    @ApiOperation(value = "查询字典数据", notes = "查询字典数据")
    public AgileResult<List<Map>> selectDictData(@RequestBody OnlineQueryParam onlineQueryParam) {
        return this.success(agileOnlineOperationService.selectDictData(onlineQueryParam));
    }

    @PostMapping(value = "/selectPageData")
    @ApiOperation(value = "分页查询数据列表", notes = "分页查询数据列表")
    public AgileResult<Map> selectPageData(@RequestBody AgilePageable<OnlineQueryParam> agilePageable) {
        return this.success(agileOnlineOperationService.selectPageData(agilePageable));
    }

    @PostMapping(value = "/selectOneData")
    @ApiOperation(value = "查看数据明细", notes = "查看数据明细")
    public AgileResult<Map> selectOneData(@SingleRequestBody String tableId, @SingleRequestBody String dataId) {
        return this.success(agileOnlineOperationService.selectOneData(tableId, dataId));
    }

    @AgileDemo
    @PostMapping(value = "/saveTableData")
    @ApiOperation(value = "保存数据", notes = "保存数据")
    public AgileResult<Map> saveMasterData(@SingleRequestBody String tableId, @SingleRequestBody Map masterData, @SingleRequestBody Map slaveData) {
        if (AgileStringUtil.isNotEmpty(agileOnlineOperationService.saveTableData(tableId, masterData, slaveData))) {
            return this.success("数据保存成功！");
        } else {
            return this.success(AgileResultCode.FAIL_OPS_SAVE, "数据保存成功！");
        }
    }

    @AgileDemo
    @PostMapping(value = "/updateTableData")
    @ApiOperation(value = "保存数据", notes = "保存数据")
    public AgileResult updateTableData(@SingleRequestBody String tableId, @SingleRequestBody Map masterData, @SingleRequestBody Map slaveData) {
        if (agileOnlineOperationService.updateTableData(tableId, masterData, slaveData)) {
            return this.success("数据更新成功！");
        } else {
            return this.success(AgileResultCode.FAIL_OPS_UPDATE, "数据更新失败！");
        }
    }

    @AgileDemo
    @PostMapping(value = "/deleteTableData")
    @ApiOperation(value = "删除数据", notes = "删除数据")
    public AgileResult deleteTableData(@SingleRequestBody String tableId, @SingleRequestBody String dataId) {
        if (agileOnlineOperationService.deleteTableData(tableId, dataId)) {
            return this.success("数据删除成功！");
        } else {
            return this.error(AgileResultCode.FAIL_OPS_DELETE, "数据删除失败！");
        }
    }

}
