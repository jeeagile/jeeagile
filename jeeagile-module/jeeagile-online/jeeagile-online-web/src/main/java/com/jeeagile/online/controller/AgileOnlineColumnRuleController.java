package com.jeeagile.online.controller;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgilePermissionsPrefix;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileCrudController;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.online.entity.AgileOnlineColumnRule;
import com.jeeagile.online.service.IAgileOnlineColumnRuleService;
import com.jeeagile.online.vo.OnlineColumnRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2023-08-21
 * @description 在线表单 字段规则配置 前端控制器
 */
@RestController
@AgileLogger("字段规则配置")
@RequestMapping("/online/column/rule")
@AgilePermissionsPrefix("online:column:rule")
@Api(value = "字段规则配置", tags = "字段规则配置")
public class AgileOnlineColumnRuleController extends AgileCrudController<IAgileOnlineColumnRuleService, AgileOnlineColumnRule> {
    @PostMapping(value = "/columnRuleList")
    @ApiOperation(value = "字段规则配置列表", notes = "字段规则配置列表")
    public AgileResult<List<OnlineColumnRule>> columnRuleList(@SingleRequestBody String columnId) {
        return this.success(agileService.columnRuleList(columnId));
    }
}
