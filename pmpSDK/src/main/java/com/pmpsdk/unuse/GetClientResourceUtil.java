package com.pmpsdk.unuse;

import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;


@Component
public class GetClientResourceUtil {
    /**
     * 获取CPU使用率
     * @return
     */
    public static double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            return ((com.sun.management.OperatingSystemMXBean) osBean).getProcessCpuLoad() * 100;
        }
        return 0;
    }

    /**
     * 获取内存使用率
     * @return
     */
    public static MemoryUsage getMemoryUsage() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        return memoryMXBean.getHeapMemoryUsage();
    }

    /**
     * 获取系统负载
     * @return
     */
    public static double getSystemLoadAverage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage();
    }

    /**
     * 获取所有监控数据
     * @return
     */
    public static Map<String, Object> getAllResourceStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cpuUsage", getCpuUsage());
        stats.put("memoryUsage", getMemoryUsage());
        stats.put("systemLoad", getSystemLoadAverage());
        return stats;
    }

}
