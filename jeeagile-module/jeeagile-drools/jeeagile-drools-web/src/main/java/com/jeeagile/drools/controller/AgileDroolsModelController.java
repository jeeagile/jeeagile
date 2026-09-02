package com.jeeagile.drools.controller;

import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.drools.entity.AgileDroolsModel;
import com.jeeagile.drools.service.IAgileDroolsModelService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2026-03-12 10:58:02
 * @description 规则引擎 数据对象 前端控制器
 */
@RestController
@AgileLogger("规则引擎 数据对象")
@RequestMapping("/drools/model")
@AgilePermissionsPrefix("drools:model")
@Api(value = "规则引擎 数据对象", tags = "规则引擎 数据对象")
public class AgileDroolsModelController extends AgileCrudController<IAgileDroolsModelService, AgileDroolsModel> {
    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    @AgileLogger(notes = "更新状态", type = AgileOperateType.UPDATE)
    public AgileResult<String> changeStatus(@SingleRequestBody String modelId, @SingleRequestBody String modelStatus) {
        if (this.agileService.changeStatus(modelId, modelStatus)) {
            return this.success();
        } else {
            return this.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "数据对象状态更新失败！");
        }
    }

    @PostMapping(value = "/validate")
    @ApiOperation(value = "验证数据对象", notes = "验证数据对象")
    @AgileLogger(notes = "验证数据对象", type = AgileOperateType.OTHER)
    public AgileResult<Object> validate(@SingleRequestBody String modelId) {
        AgileDroolsModel agileDroolsModel = this.agileService.getById(modelId);
        if (agileDroolsModel == null || agileDroolsModel.isEmptyPk()) {
            return this.error(AgileResultCode.FAIL_SEARCH_EXCEPTION, "数据对象不存在！");
        }
        this.agileService.validateModel(agileDroolsModel);
        return this.success();
    }
}
