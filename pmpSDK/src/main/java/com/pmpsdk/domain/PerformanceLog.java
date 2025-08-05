package com.pmpsdk.domain;

/**
 * @Description: 性能日志  // 类说明
 * @ClassName: PerformanceLog    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/5 15:48   // 时间
 * @Version: 1.0     // 版本
 */

public class PerformanceLog {
    private String api;
    private long duration;
    private long timestamp;
    private String module;
    private String projectId;
    private boolean slow;


    public PerformanceLog() {
    }

    public PerformanceLog(String api, long duration, long timestamp, String module, String projectId, boolean slow) {
        this.api = api;
        this.duration = duration;
        this.timestamp = timestamp;
        this.module = module;
        this.projectId = projectId;
        this.slow = slow;
    }

    /**
     * 获取
     * @return api
     */
    public String getApi() {
        return api;
    }

    /**
     * 设置
     * @param api
     */
    public void setApi(String api) {
        this.api = api;
    }

    /**
     * 获取
     * @return duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * 设置
     * @param duration
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 获取
     * @return timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 设置
     * @param timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取
     * @return module
     */
    public String getModule() {
        return module;
    }

    /**
     * 设置
     * @param module
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 获取
     * @return projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * 设置
     * @param projectId
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取
     * @return slow
     */
    public boolean isSlow() {
        return slow;
    }

    /**
     * 设置
     * @param slow
     */
    public void setSlow(boolean slow) {
        this.slow = slow;
    }

    public String toString() {
        return "PerformanceLog{api = " + api + ", duration = " + duration + ", timestamp = " + timestamp + ", module = " + module + ", projectId = " + projectId + ", slow = " + slow + "}";
    }
}
