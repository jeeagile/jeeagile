package com.jeeagile.monitor.controller;

import com.jeeagile.core.protocol.annotation.AgileReference;
import com.jeeagile.core.protocol.properties.AgileProtocolProperties;
import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.system.AgileServerInfo;
import com.jeeagile.core.util.system.AgileServerUtil;
import com.jeeagile.frame.controller.AgileBaseController;
import com.jeeagile.monitor.service.IAgileServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 服务器监控 前端控制器
 */
@RestController
@RequestMapping("/monitor/server")
@Api(value = "服务器监控", tags = "服务器监控")
public class AgileServerController extends AgileBaseController {

    @AgileReference
    private IAgileServerService agileServerService;

    @Resource
    private AgileProtocolProperties agileProtocolProperties;

    @Autowired
    private Environment environment;

    @PostMapping(value = "/info")
    @ApiOperation(value = "获取服务监控信息", notes = "获取服务监控信息")
    @AgileRequiresPermissions("monitor:server:info")
    public AgileResult<AgileServerInfo> serverInfo() {
        AgileServerInfo agileServerInfo = AgileServerUtil.getAgileServerInfo();
        agileServerInfo.setServerName(environment.getProperty("spring.application.name"));
        agileServerInfo.setServerIp(agileServerInfo.getHostInfo().getAddress());
        agileServerInfo.setServerPort(environment.getProperty("server.port"));
        return this.rtnSuccess(agileServerInfo);
    }
}
