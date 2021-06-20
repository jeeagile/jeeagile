package com.jeeagile.logger.controller;

import com.jeeagile.core.protocol.annotation.AgileProvider;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.logger.entity.AgileLoggerOperate;
import com.jeeagile.logger.service.IAgileLoggerOperateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 操作日志管理 前端控制器
 */
@RestController
@RequestMapping("/logger/operate")
@Api(value = "操作日志管理", tags = "操作日志管理")
public class AgileLoggerOperateController extends AgileBaseController {
    @AgileProvider
    private IAgileLoggerOperateService agileLoggerOperateService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询参操作日志", notes = "分页查询参操作日志")
    public AgileResult<AgilePage<AgileLoggerOperate>> selectPage(@RequestBody AgilePageable<AgileLoggerOperate> agilePageable) {
        return this.rtnSuccess(agileLoggerOperateService.selectPage(agilePageable));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除操作日志记录", notes = "删除操作日志记录")
    @AgileLogger(title = "删除操作日志记录", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String configId) {
        agileLoggerOperateService.deleteLoggerOperate(configId);
        return this.rtnSuccess("操作日志记录删除成功！");
    }

    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空操作日志记录", notes = "清空操作日志记录")
    @AgileLogger(title = "清空操作日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileLoggerOperateService.clearLoggerOperate();
        return this.rtnSuccess("操作日志记录清空成功！");
    }
}
