package com.jeeagile.frame.controller.monitor;

import com.jeeagile.core.result.AgileResult;
import com.jeeagile.core.security.annotation.AgileRequiresPermissions;
import com.jeeagile.core.util.system.AgileServerInfo;
import com.jeeagile.core.util.system.AgileServerUtil;
import com.jeeagile.frame.controller.AgileBaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 服务器监控 前端控制器
 */
@RestController
@RequestMapping("/monitor/server")
@Api(value = "服务器监控", tags = "服务器监控")
public class AgileMonitorServerController extends AgileBaseController {

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
        return this.success(agileServerInfo);
    }
}
