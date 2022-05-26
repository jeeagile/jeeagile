package com.jeeagile.frame.service.monitor;

import com.jeeagile.core.protocol.annotation.AgileService;
import com.jeeagile.core.util.system.AgileServerInfo;
import com.jeeagile.core.util.system.AgileServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@AgileService
public class AgileMonitorServerServiceImpl implements IAgileMonitorServerService {

    @Autowired
    private Environment environment;

    @Override
    public AgileServerInfo getAgileServerInfo() {
        AgileServerInfo agileServerInfo = AgileServerUtil.getAgileServerInfo();
        agileServerInfo.setServerName(environment.getProperty("spring.application.name"));
        agileServerInfo.setServerIp(agileServerInfo.getHostInfo().getAddress());
        agileServerInfo.setServerPort(environment.getProperty("server.port"));
        return agileServerInfo;
    }
}
