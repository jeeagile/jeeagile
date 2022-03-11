package com.jeeagile.logger.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.frame.annotation.AgileDemo;
import com.jeeagile.frame.annotation.AgileLogger;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.frame.enums.AgileLoggerType;
import com.jeeagile.frame.page.AgilePage;
import com.jeeagile.frame.page.AgilePageable;
import com.jeeagile.frame.support.resolver.annotation.SingleRequestBody;
import com.jeeagile.logger.entity.AgileLoggerLogin;
import com.jeeagile.logger.service.IAgileLoggerLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 登录日志管理 前端控制器
 */
@RestController
@AgileLogger("登录日志管理")
@RequestMapping("/logger/login")
@Api(value = "登录日志管理", tags = "登录日志管理")
public class AgileLoggerLoginController extends AgileBaseController {
    @AgileReference
    private IAgileLoggerLoginService agileLoggerLoginService;

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询登录日志列表", notes = "分页查询登录日志列表")
    public AgileResult<AgilePage<AgileLoggerLogin>> selectPage(@RequestBody AgilePageable<AgileLoggerLogin> agilePageable) {
        return this.success(agileLoggerLoginService.selectPage(agilePageable));
    }

    @AgileDemo
    @PostMapping("/delete")
    @ApiOperation(value = "删除登录日志记录", notes = "删除登录日志记录")
    @AgileLogger(notes = "删除登录日志记录", type = AgileLoggerType.DELETE)
    public AgileResult<String> delete(@SingleRequestBody String configId) {
        agileLoggerLoginService.deleteModel(configId);
        return this.success("登录日志记录删除成功！");
    }


    @AgileDemo
    @PostMapping("/clear")
    @ApiOperation(value = "清空登录日志记录", notes = "清空登录日志记录")
    @AgileLogger(notes = "清空登录日志记录", type = AgileLoggerType.CLEAR)
    public AgileResult clear() {
        agileLoggerLoginService.clearRecord();
        return this.success("登录日志记录清空成功！");
    }
}
