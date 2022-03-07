package com.jeeagile.core.util.system;

import com.jeeagile.core.util.AgileDateUtil;
import com.jeeagile.core.util.system.util.AgileOshiUtil;
import com.jeeagile.core.util.system.util.AgileSystemUtil;

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

        agileServerInfo.setTime(AgileDateUtil.formatDate(new Date(), "HH:mm:ss"));
        agileServerInfo.setHostInfo(AgileSystemUtil.getHostInfo());
        agileServerInfo.setOsInfo(AgileSystemUtil.getOsInfo());
        agileServerInfo.setJavaInfo(AgileSystemUtil.getJavaInfo());
        agileServerInfo.setJvmInfo(AgileSystemUtil.getJvmInfo());
        agileServerInfo.setRuntimeInfo(AgileSystemUtil.getRuntimeInfo());
        agileServerInfo.setUserInfo(AgileSystemUtil.getUserInfo());

        agileServerInfo.setCpuInfo(AgileOshiUtil.getCpuInfo());
        agileServerInfo.setDiskInfo(AgileOshiUtil.getDiskInfo());
        agileServerInfo.setDiskInfoList(AgileOshiUtil.getDiskInfoList());
        agileServerInfo.setMemoryInfo(AgileOshiUtil.getMemoryInfo());
        agileServerInfo.setSwapInfo(AgileOshiUtil.getSwapInfo());
        return agileServerInfo;
    }
}
