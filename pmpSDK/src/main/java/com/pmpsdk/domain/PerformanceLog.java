package com.pmpsdk.domain;

import com.pmpsdk.annotation.ThrowSDKException;

/**
 * @Description: 性能日志  // 类说明
 * @ClassName: PerformanceLog    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/5 15:48   // 时间
 * @Version: 1.0     // 版本
 */
@ThrowSDKException
public class PerformanceLog {
    private Long id;
    private String api;
    private long duration;
    private long timestamp;
    private String module;
    private String projectId;
    private String source = "backend";
    private boolean slow;
    private String environment;
    private EnvironmentSnapshot environmentSnapshot;


    public PerformanceLog() {
    }

    public PerformanceLog(Long id, String api, long duration, long timestamp, String module, String projectId, String source, boolean slow, String environment, EnvironmentSnapshot environmentSnapshot) {
        this.id = id;
        this.api = api;
        this.duration = duration;
        this.timestamp = timestamp;
        this.module = module;
        this.projectId = projectId;
        this.source = source;
        this.slow = slow;
        this.environment = environment;
        this.environmentSnapshot = environmentSnapshot;
    }

    /**
     * 获取
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
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

    /**
     * 获取
     * @return environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * 设置
     * @param environment
     */
    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * 获取
     * @return environmentSnapshot
     */
    public EnvironmentSnapshot getEnvironmentSnapshot() {
        return environmentSnapshot;
    }

    /**
     * 设置
     * @param environmentSnapshot
     */
    public void setEnvironmentSnapshot(EnvironmentSnapshot environmentSnapshot) {
        this.environmentSnapshot = environmentSnapshot;
    }

    public String toString() {
        return "PerformanceLog{id = " + id + ", api = " + api + ", duration = " + duration + ", timestamp = " + timestamp + ", module = " + module + ", projectId = " + projectId + ", source = " + source + ", slow = " + slow + ", environment = " + environment + ", environmentSnapshot = " + environmentSnapshot + "}";
    }
}
