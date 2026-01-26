package com.jeeagile.process.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.process.entity.AgileProcessModel;
import com.jeeagile.process.service.IAgileProcessModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2022-05-30
 * @description 流程模型
 */
@RestController
@AgileLogger("流程模型")
@RequestMapping("/process/model")
@AgilePermissionsPrefix("process:model")
@Api(value = "流程模型", tags = "流程模型")
public class AgileProcessModelController extends AgileCrudController<IAgileProcessModelService, AgileProcessModel> {

    @AgileDemo
    @PostMapping("/designer")
    @AgileRequiresPermissions("designer")
    @AgileLogger(notes = "保存流程设计", type = AgileOperateType.UPDATE)
    @ApiOperation(value = "保存流程设计", notes = "保存流程设计")
    public AgileResult<AgileProcessModel> designer(@SingleRequestBody String modelId, @SingleRequestBody String modelXml) {
        return AgileResult.success(agileService.saveProcessDesigner(modelId, modelXml));
    }

    @AgileDemo
    @PostMapping("/deployment")
    @AgileRequiresPermissions("deployment")
    @AgileLogger(notes = "流程发布", type = AgileOperateType.UPDATE)
    @ApiOperation(value = "流程发布", notes = "流程发布")
    public AgileResult<AgileProcessModel> deployment(@SingleRequestBody String modelId) {
        if (agileService.processDeployment(modelId)) {
            return AgileResult.success("流程发布成功！");
        } else {
            return AgileResult.error(AgileResultCode.FAIL_OPS, "流程发布失败！");
        }
    }

    @PostMapping("/selectProcessOnlinePageList")
    @ApiOperation(value = "查询流程在线表单", notes = "查询流程在线表单")
    public AgileResult<Object> selectProcessOnlinePageList() {
        return this.success(agileService.selectProcessOnlinePageList());
    }
}
