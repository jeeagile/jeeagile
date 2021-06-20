package com.jeeagile.core.util.system;

import com.jeeagile.core.util.DateUtil;
import com.jeeagile.core.util.system.util.OshiUtil;
import com.jeeagile.core.util.system.util.SystemUtil;

import java.util.Date;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description 获取监控信息
 */
public class AgileServerUtil {
    /**
     * 获取监控信息
     */
    public static AgileServerInfo getAgileServerInfo() {
        AgileServerInfo agileServerInfo = new AgileServerInfo();

        agileServerInfo.setTime(DateUtil.formatDate(new Date(), "HH:mm:ss"));
        agileServerInfo.setHostInfo(SystemUtil.getHostInfo());
        agileServerInfo.setOsInfo(SystemUtil.getOsInfo());
        agileServerInfo.setJavaInfo(SystemUtil.getJavaInfo());
        agileServerInfo.setJvmInfo(SystemUtil.getJvmInfo());
        agileServerInfo.setRuntimeInfo(SystemUtil.getRuntimeInfo());
        agileServerInfo.setUserInfo(SystemUtil.getUserInfo());

        agileServerInfo.setCpuInfo(OshiUtil.getCpuInfo());
        agileServerInfo.setDiskInfo(OshiUtil.getDiskInfo());
        agileServerInfo.setDiskInfoList(OshiUtil.getDiskInfoList());
        agileServerInfo.setMemoryInfo(OshiUtil.getMemoryInfo());
        agileServerInfo.setSwapInfo(OshiUtil.getSwapInfo());
        return agileServerInfo;
    }
}
