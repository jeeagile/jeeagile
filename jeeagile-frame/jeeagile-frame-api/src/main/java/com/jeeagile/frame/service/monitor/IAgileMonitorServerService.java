package com.jeeagile.frame.service.monitor;

import com.jeeagile.core.util.system.AgileServerInfo;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 服务器监控接口类
 */
public interface IAgileMonitorServerService {

    /**
     * 获取服务器监控信息
     * @return
     */
    AgileServerInfo getAgileServerInfo();

}
