package com.pmpsdk.domain;

/**
 * @Description: 日志应用类  // 类说明
 * @ClassName: Log    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 22:36   // 时间
 * @Version: 1.0     // 版本
 */
public class Log {
    private Long id;
    private Long timestamp;
    private String level;
    private String context;
    private String module;
    private String source = "backend";
    private String projectId;
    private EnvironmentSnapshot environmentSnapshot;
    private String environment;

    public Log() {
    }

    public Log(Long id, Long timestamp, String level, String context, String module, String source, String projectId, EnvironmentSnapshot environmentSnapshot, String environment) {
        this.id = id;
        this.timestamp = timestamp;
        this.level = level;
        this.context = context;
        this.module = module;
        this.source = source;
        this.projectId = projectId;
        this.environmentSnapshot = environmentSnapshot;
        this.environment = environment;
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
     * @return timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * 设置
     * @param timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置
     * @param level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取
     * @return context
     */
    public String getContext() {
        return context;
    }

    /**
     * 设置
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
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

    public String toString() {
        return "Log{id = " + id + ", timestamp = " + timestamp + ", level = " + level + ", context = " + context + ", module = " + module + ", source = " + source + ", projectId = " + projectId + ", environmentSnapshot = " + environmentSnapshot + ", environment = " + environment + "}";
    }
}