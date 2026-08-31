package com.jeeagile.drools.controller;

import com.jeeagile.core.constants.AgileOperateType;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.result.AgileResultCode;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.drools.vo.AgileDroolsRuleInfo;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.drools.entity.AgileDroolsRule;
import com.jeeagile.drools.service.IAgileDroolsRuleService;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author JeeAgile
 * @date 2026-03-13 16:30:40
 * @description 规则引擎 规则配置 前端控制器
 */
@RestController
@AgileLogger("规则引擎 规则配置")
@RequestMapping("/drools/rule")
@AgilePermissionsPrefix("drools:rule")
@Api(value = "规则引擎 规则配置", tags = "规则引擎 规则配置")
public class AgileDroolsRuleController extends AgileCrudController<IAgileDroolsRuleService, AgileDroolsRule> {
    @AgileDemo
    @PostMapping(value = "/changeStatus")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    @AgileLogger(notes = "更新状态", type = AgileOperateType.UPDATE)
    public AgileResult<String> changeStatus(@SingleRequestBody String ruleId, @SingleRequestBody String ruleStatus) {
        if (this.agileService.changeStatus(ruleId, ruleStatus)) {
            return AgileResult.success();
        } else {
            return AgileResult.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "规则状态更新失败！");
        }
    }

    @PostMapping(value = "/info")
    @ApiOperation(value = "查询规则详细信息", notes = "查询规则详细信息")
    @AgileLogger(notes = "查询规则详细信息", type = AgileOperateType.SELECT)
    public AgileResult<AgileDroolsRuleInfo> info(@SingleRequestBody String ruleId) {
        return AgileResult.success(this.agileService.info(ruleId));
    }

    @AgileDemo
    @PostMapping(value = "/saveRuleContent")
    @ApiOperation(value = "更新状态", notes = "更新状态")
    @AgileLogger(notes = "更新状态", type = AgileOperateType.UPDATE)
    public AgileResult<String> saveRuleContent(@RequestBody AgileDroolsRule agileDroolsRule) {
        if (this.agileService.saveRuleContent(agileDroolsRule)) {
            return AgileResult.success();
        } else {
            return AgileResult.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "角色状态更新失败！");
        }
    }

    @PostMapping("/validateRuleContent")
    @ApiOperation(value = "验证规则", notes = "验证规则")
    @AgileLogger(notes = "验证规则", type = AgileOperateType.OTHER)
    public AgileResult<Object> validateRuleContent(@SingleRequestBody String ruleId, @SingleRequestBody String ruleContent) {
        if (this.agileService.validateRuleContent(ruleId, ruleContent)) {
            return AgileResult.success();
        } else {
            return AgileResult.error(AgileResultCode.FAIL_UPDATE_EXCEPTION, "规则内容验证异常！");
        }
    }

    @PostMapping("/test")
    @ApiOperation(value = "规则测试", notes = "规则测试")
    @AgileLogger(notes = "规则测试", type = AgileOperateType.OTHER)
    public AgileResult<Object> test(@SingleRequestBody String ruleId, @SingleRequestBody String ruleContent, @SingleRequestBody Map paramData) {
        return AgileResult.success(this.agileService.test(ruleId, ruleContent, paramData));
    }


}
