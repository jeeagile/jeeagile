package com.jeeagile.core.util.system.util;

import com.jeeagile.core.util.DateUtil;
import com.jeeagile.core.util.FormatUtil;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.system.info.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
public class SystemUtil {
    /**
     * 取得系统属性
     */
    public static String get(String name, String defaultValue) {
        String value = get(name);
        if (StringUtil.isNotEmpty(value)) {
            return value;
        } else {
            return defaultValue;
        }
    }

    /**
     * 取得系统属性
     */
    public static String get(String name, boolean logFlag) {
        String value = null;
        try {
            value = System.getProperty(name);
        } catch (SecurityException e) {
            if (logFlag) {
                log.warn("获取系统属性出现安全异常，属性名：{} ", name);
            }
        }
        if (null == value) {
            try {
                value = System.getenv(name);
            } catch (SecurityException e) {
                if (logFlag) {
                    log.warn("获取系统属性出现安全异常，属性名：{} ", name);
                }
            }
        }
        return value;
    }

    /**
     * 获得System属性
     */
    public static String get(String key) {
        return get(key, false);
    }

    /**
     * @return 属性列表
     */
    public static Properties props() {
        return System.getProperties();
    }

    /**
     * 获取当前进程 PID
     */
    public static long getCurrentPID() {
        return Long.parseLong(getRuntimeMXBean().getName().split("@")[0]);
    }

    /**
     * 返回Java虚拟机类加载系统相关属性
     */
    public static ClassLoadingMXBean getClassLoadingMXBean() {
        return ManagementFactory.getClassLoadingMXBean();
    }

    /**
     * 返回Java虚拟机内存系统相关属性
     */
    public static MemoryMXBean getMemoryMXBean() {
        return ManagementFactory.getMemoryMXBean();
    }

    /**
     * 返回Java虚拟机线程系统相关属性
     */
    public static ThreadMXBean getThreadMXBean() {
        return ManagementFactory.getThreadMXBean();
    }

    /**
     * 返回Java虚拟机运行时系统相关属性
     */
    public static RuntimeMXBean getRuntimeMXBean() {
        return ManagementFactory.getRuntimeMXBean();
    }

    /**
     * 返回Java虚拟机编译系统相关属性
     * 如果没有编译系统，则返回
     */
    public static CompilationMXBean getCompilationMXBean() {
        return ManagementFactory.getCompilationMXBean();
    }

    /**
     * 返回Java虚拟机运行下的操作系统相关信息属性
     */
    public static OperatingSystemMXBean getOperatingSystemMXBean() {
        return ManagementFactory.getOperatingSystemMXBean();
    }

    /**
     * 获取Java虚拟机列表
     */
    public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        return ManagementFactory.getMemoryPoolMXBeans();
    }

    /**
     * 获取Java虚拟机中的列表
     */
    public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
        return ManagementFactory.getMemoryManagerMXBeans();
    }

    /**
     * 获取Java虚拟机中列表
     */
    public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return ManagementFactory.getGarbageCollectorMXBeans();
    }

    /**
     * 取得Java Implementation的信息。
     *
     * @return <code>JavaInfo</code>对象
     */
    public static JavaInfo getJavaInfo() {
        JavaInfo javaInfo = new JavaInfo();

        javaInfo.setVersion(get("java.version"));
        javaInfo.setVendor(get("java.vendor"));
        javaInfo.setVendorUrl(get("java.vendor.url"));

        javaInfo.setRuntimeName(SystemUtil.get("java.runtime.name"));
        javaInfo.setRuntimeVersion(get("java.runtime.version"));
        javaInfo.setHomeDir(SystemUtil.get("java.home"));
        javaInfo.setExtDirs(SystemUtil.get("java.ext.dirs"));
        javaInfo.setClassPath(SystemUtil.get("java.class.path"));
        javaInfo.setClassVersion(SystemUtil.get("java.class.version"));
        javaInfo.setLibraryPath(SystemUtil.get("java.library.path"));

        return javaInfo;
    }

    /**
     * 取得JVM信息。
     */
    public static JvmInfo getJvmInfo() {
        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setName(get("java.vm.name"));
        jvmInfo.setVersion(SystemUtil.get("java.vm.version"));
        jvmInfo.setVendor(SystemUtil.get("java.vm.vendor"));
        jvmInfo.setInfo(SystemUtil.get("java.vm.info"));
        RuntimeMXBean runtimeMXBean = getRuntimeMXBean();
        jvmInfo.setStartArgs(runtimeMXBean.getInputArguments().toString());
        jvmInfo.setStartTime(DateUtil.formatDate(runtimeMXBean.getStartTime()));
        jvmInfo.setRunTime(DateUtil.formatDateAgo(runtimeMXBean.getUptime()));
        return jvmInfo;
    }

    /**
     * 取得OS的信息。
     */
    public static OsInfo getOsInfo() {
        OsInfo osInfo = new OsInfo();
        osInfo.setArch(SystemUtil.get("os.arch"));
        osInfo.setName(SystemUtil.get("os.name"));
        osInfo.setVersion(SystemUtil.get("os.version"));
        osInfo.setFileSeparator(SystemUtil.get("file.separator"));
        osInfo.setLineSeparator(SystemUtil.get("line.separator"));
        osInfo.setPathSeparator(SystemUtil.get("path.separator"));
        return osInfo;
    }

    /**
     * 取得Host的信息。
     */
    public static HostInfo getHostInfo() {
        HostInfo hostInfo = new HostInfo();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostInfo.setName(inetAddress.getHostName());
            hostInfo.setAddress(inetAddress.getHostAddress());
        } catch (UnknownHostException ex) {
            log.error("获取系统信息出现错误！", ex);
        }
        return hostInfo;
    }

    /**
     * 取得Runtime的信息。
     *
     * @return <code>RuntimeInfo</code>对象
     */
    public static RuntimeInfo getRuntimeInfo() {
        Runtime runtime = Runtime.getRuntime();
        RuntimeInfo runtimeInfo = new RuntimeInfo();
        runtimeInfo.setMaxMemory(FormatUtil.formatBytes(runtime.maxMemory()));
        runtimeInfo.setFreeMemory(FormatUtil.formatBytes(runtime.freeMemory()));
        runtimeInfo.setTotalMemory(FormatUtil.formatBytes(runtime.totalMemory()));
        runtimeInfo.setUsableMemory(FormatUtil.formatBytes(runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory()));
        return runtimeInfo;
    }

    /**
     * 用户信息
     */
    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(SystemUtil.get("user.name"));
        userInfo.setHomeDir(SystemUtil.get("user.home"));
        userInfo.setUserDir(SystemUtil.get("user.dir"));
        userInfo.setLanguage(SystemUtil.get("user.language"));
        return userInfo;
    }

    /**
     * 获取JVM中内存总大小
     *
     * @return 内存总大小
     * @since 4.5.4
     */
    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * 获取JVM中内存剩余大小
     *
     * @return 内存剩余大小
     * @since 4.5.4
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * 获取JVM可用的内存总大小
     *
     * @return JVM可用的内存总大小
     * @since 4.5.4
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取总线程数
     *
     * @return 总线程数
     */
    public static int getTotalThreadCount() {
        ThreadGroup parentThread = Thread.currentThread().getThreadGroup();
        while (null != parentThread.getParent()) {
            parentThread = parentThread.getParent();
        }
        return parentThread.activeCount();
    }
}
