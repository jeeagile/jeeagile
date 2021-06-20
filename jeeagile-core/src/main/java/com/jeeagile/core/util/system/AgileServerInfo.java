package com.jeeagile.core.util.system;

import com.jeeagile.core.util.system.info.*;
import com.jeeagile.core.util.system.info.oshi.CpuInfo;
import com.jeeagile.core.util.system.info.oshi.DiskInfo;
import com.jeeagile.core.util.system.info.oshi.MemoryInfo;
import com.jeeagile.core.util.system.info.oshi.SwapInfo;
import lombok.Data;

import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Data
public class AgileServerInfo {
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 服务端口
     */
    private String serverIp;
    /**
     * 服务端口
     */
    private String serverPort;

    /**
     * 当前时间
     */
    private String time;
    /**
     * 主机信息
     */
    private HostInfo hostInfo;
    /**
     * os信息
     */
    private OsInfo osInfo;
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    /**
     * java信息
     */
    private JavaInfo javaInfo;
    /**
     * jvm信息
     */
    private JvmInfo jvmInfo;
    /**
     * 系统运行信息
     */
    private RuntimeInfo runtimeInfo;
    /**
     * cpu信息
     */
    private CpuInfo cpuInfo;
    /**
     * 内存信息
     */
    private MemoryInfo memoryInfo;
    /**
     * 交换区信息
     */
    private SwapInfo swapInfo;
    /**
     * 磁盘信息
     */
    private DiskInfo diskInfo;
    /**
     * 磁盘列表信息
     */
    private List<DiskInfo> diskInfoList;

}
