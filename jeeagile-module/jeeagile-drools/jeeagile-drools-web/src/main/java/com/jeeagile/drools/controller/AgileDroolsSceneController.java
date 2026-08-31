package com.jeeagile.drools.controller;

import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.core.security.annotation.AgileRequiresGuest;
import com.jeeagile.drools.vo.AgileDroolsSceneInfo;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.drools.entity.AgileDroolsScene;
import com.jeeagile.drools.service.IAgileDroolsSceneService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-18 09:39:36
 * @description 规则引擎 规则场景 前端控制器
 */
@RestController
@AgileLogger("规则引擎 规则场景")
@RequestMapping("/drools/scene")
@AgilePermissionsPrefix("drools:scene")
@Api(value = "规则引擎 规则场景", tags = "规则引擎 规则场景")
public class AgileDroolsSceneController extends AgileCrudController<IAgileDroolsSceneService, AgileDroolsScene> {
    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    @AgileLogger(notes = "更新状态", type = AgileOperateType.UPDATE)
    public AgileResult<String> changeStatus(@SingleRequestBody String sceneId, @SingleRequestBody String sceneStatus) {
        if (this.agileService.changeStatus(sceneId, sceneStatus)) {
            return AgileResult.success();
        } else {
            return AgileResult.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "规则状态更新失败！");
        }
    }

    @PostMapping(value = "/info")
    @ApiOperation(value = "查询规则详细信息", notes = "查询规则详细信息")
    @AgileLogger(notes = "查询规则详细信息", type = AgileOperateType.SELECT)
    public AgileResult<AgileDroolsSceneInfo> info(@SingleRequestBody String sceneId) {
        return AgileResult.success(this.agileService.info(sceneId));
    }

    @ResponseBody
    @AgileRequiresGuest
    @PostMapping(value = "/execute")
    @ApiOperation(value = "场景规则执行", notes = "场景规则执行")
    @AgileLogger(notes = "场景规则执行", type = AgileOperateType.UPDATE)
    public AgileResult<Object> execute(@PathVariable String sceneCode, @RequestBody Map paramData) {
        return AgileResult.success(this.agileService.execute(sceneCode, paramData));
    }
}
