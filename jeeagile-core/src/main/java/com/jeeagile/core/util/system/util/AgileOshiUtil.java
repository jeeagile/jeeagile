package com.jeeagile.core.util.system.util;

import com.jeeagile.core.util.AgileFormatUtil;
import com.jeeagile.core.util.system.info.oshi.CpuInfo;
import com.jeeagile.core.util.system.info.oshi.DiskInfo;
import com.jeeagile.core.util.system.info.oshi.MemoryInfo;
import com.jeeagile.core.util.system.info.oshi.SwapInfo;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class AgileOshiUtil {
    private static final SystemInfo systemInfo;
    /**
     * 硬件信息
     */
    private static final HardwareAbstractionLayer hardware;
    /**
     * 系统信息
     */
    private static final OperatingSystem os;

    static {
        systemInfo = new SystemInfo();
        hardware = systemInfo.getHardware();
        os = systemInfo.getOperatingSystem();
    }

    /**
     * 获取操作系统相关信息，包括系统版本、文件系统、进程等
     */
    public static OperatingSystem getOs() {
        return os;
    }

    /**
     * 获取硬件相关信息，包括内存、硬盘、网络设备、显示器、USB、声卡等
     */
    public static HardwareAbstractionLayer getHardware() {
        return hardware;
    }

    /**
     * 获取BIOS中计算机相关信息，比如序列号、固件版本等
     */
    public static ComputerSystem getSystem() {
        return hardware.getComputerSystem();
    }

    /**
     * 获取内存相关信息，比如总内存、可用内存等
     */
    public static GlobalMemory getMemory() {
        return hardware.getMemory();
    }

    /**
     * 获取CPU（处理器）相关信息，比如CPU负载等
     */
    public static CentralProcessor getProcessor() {
        return hardware.getProcessor();
    }

    /**
     * 获取传感器相关信息，例如CPU温度、风扇转速等，传感器可能有多个
     */
    public static Sensors getSensors() {
        return hardware.getSensors();
    }

    /**
     * 获取磁盘相关信息，可能有多个磁盘（包括可移动磁盘等）
     */
    public static List<HWDiskStore> getDiskStores() {
        return hardware.getDiskStores();
    }

    /**
     * 获取网络相关信息，可能多块网卡
     */
    public static List<NetworkIF> getNetworkIFs() {
        return hardware.getNetworkIFs();
    }


    /**
     * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
     */
    public static CpuInfo getCpuInfo() {
        return getCpuInfo(1000);
    }

    /**
     * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
     */
    public static CpuInfo getCpuInfo(long waitingTime) {
        return getCpuInfo(AgileOshiUtil.getProcessor(), waitingTime);
    }

    /**
     * 获取交换区使用信息
     */
    public static SwapInfo getSwapInfo() {
        return getSwapInfo(AgileOshiUtil.getMemory());
    }

    /**
     * 获取内存使用信息
     */
    public static MemoryInfo getMemoryInfo() {
        return getMemoryInfo(AgileOshiUtil.getMemory());
    }

    /**
     * 获取磁盘利用信息
     *
     * @return
     */
    public static DiskInfo getDiskInfo() {
        return getDiskInfo(AgileOshiUtil.getOs());
    }

    /**
     * 获取磁盘列表
     *
     * @return
     */
    public static List<DiskInfo> getDiskInfoList() {
        return getDiskInfoList(AgileOshiUtil.getOs());
    }

    /**
     * 获取系统CPU 系统使用率、用户使用率、利用率等等 相关信息
     */
    private static CpuInfo getCpuInfo(CentralProcessor processor, long waitingTime) {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setCpuName(processor.getProcessorIdentifier().getName());
        cpuInfo.setPackageNum(processor.getPhysicalPackageCount());
        cpuInfo.setCoreNum(processor.getPhysicalProcessorCount());
        cpuInfo.setLogicNum(processor.getLogicalProcessorCount());
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 设置延迟
        Util.sleep(waitingTime);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = Math.max(user + nice + cSys + idle + ioWait + irq + softIrq + steal, 0);
        final DecimalFormat format = new DecimalFormat("#.00");
        cpuInfo.setToTal(totalCpu);
        cpuInfo.setSys(Double.parseDouble(format.format(cSys <= 0 ? 0 : (100d * cSys / totalCpu))));
        cpuInfo.setUsed(Double.parseDouble(format.format(user <= 0 ? 0 : (100d * user / totalCpu))));
        if (totalCpu == 0) {
            cpuInfo.setWait(0);
        } else {
            cpuInfo.setWait(Double.parseDouble(format.format(100d * ioWait / totalCpu)));
        }
        cpuInfo.setFree(Double.parseDouble(format.format(idle <= 0 ? 0 : (100d * idle / totalCpu))));
        cpuInfo.setCpuModel(processor.toString());
        return cpuInfo;
    }

    /**
     * 获取交换区信息
     */
    private static SwapInfo getSwapInfo(GlobalMemory memory) {
        SwapInfo swapInfo = new SwapInfo();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        swapInfo.setTotal(AgileFormatUtil.formatBytes(total));
        swapInfo.setUsed(AgileFormatUtil.formatBytes(used));
        swapInfo.setAvailable(AgileFormatUtil.formatBytes(total - used));
        if (used == 0) {
            swapInfo.setUsageRate("0");
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            swapInfo.setUsageRate(df.format(used / (double) total * 100));
        }
        return swapInfo;
    }

    /**
     * 获取内存信息
     */
    private static MemoryInfo getMemoryInfo(GlobalMemory memory) {
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(AgileFormatUtil.formatBytes(memory.getTotal()));
        memoryInfo.setAvailable(AgileFormatUtil.formatBytes(memory.getAvailable()));
        memoryInfo.setUsed(AgileFormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        DecimalFormat df = new DecimalFormat("0.00");
        memoryInfo.setUsageRate(df.format((memory.getTotal() - memory.getAvailable()) / (double) memory.getTotal() * 100));
        return memoryInfo;
    }

    /**
     * 获取磁盘信息
     */
    private static DiskInfo getDiskInfo(OperatingSystem os) {
        DiskInfo diskInfo = new DiskInfo();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fileStoreList = fileSystem.getFileStores();
        long total = 0;
        long available = 0;
        for (OSFileStore fileStore : fileStoreList) {
            total += fileStore.getTotalSpace();
            available += fileStore.getUsableSpace();
        }
        diskInfo.setTotal(total > 0 ? AgileFormatUtil.formatBytes(total) : "?");
        diskInfo.setAvailable(AgileFormatUtil.formatBytes(available));
        diskInfo.setUsed(AgileFormatUtil.formatBytes(total - available));
        DecimalFormat df = new DecimalFormat("0.00");
        diskInfo.setUsageRate(df.format((total - available) / (double) total * 100));
        return diskInfo;
    }

    /**
     * 获取磁盘信息
     */
    private static List<DiskInfo> getDiskInfoList(OperatingSystem os) {
        List<DiskInfo> diskInfoList = new ArrayList<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fileStoreList = fileSystem.getFileStores();
        for (OSFileStore fileStore : fileStoreList) {
            DiskInfo diskInfo = new DiskInfo();
            diskInfo.setFileMount(fileStore.getMount().replaceAll("\\\\", ""));
            diskInfo.setFileType(fileStore.getType());
            diskInfo.setFileDesc(fileStore.getDescription());
            diskInfo.setTotal(fileStore.getTotalSpace() > 0 ? AgileFormatUtil.formatBytes(fileStore.getTotalSpace()) : "?");
            diskInfo.setAvailable(AgileFormatUtil.formatBytes(fileStore.getUsableSpace()));
            diskInfo.setUsed(AgileFormatUtil.formatBytes(fileStore.getTotalSpace() - fileStore.getUsableSpace()));
            DecimalFormat df = new DecimalFormat("0.00");
            diskInfo.setUsageRate(df.format((fileStore.getTotalSpace() - fileStore.getUsableSpace()) / (double) fileStore.getTotalSpace() * 100));
            diskInfoList.add(diskInfo);
        }
        return diskInfoList;
    }

}
