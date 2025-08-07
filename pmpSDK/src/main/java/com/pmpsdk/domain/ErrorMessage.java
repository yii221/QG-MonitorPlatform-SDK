package com.pmpsdk.domain;

/**
 * @Description: 报错信息  // 类说明
 * @ClassName: ErrorMessage    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 20:45   // 时间
 * @Version: 1.0     // 版本
 */
public class ErrorMessage {

    private Long id;
    private Long timestamp;
    private String type;
    private String stack;
    private String module;
    private String source = "backend";
    private String projectId;
    private String environment;
    private EnvironmentSnapshot environmentSnapshot;
    private DataType dataType = DataType.ERROR;


    public ErrorMessage() {
    }

    public ErrorMessage(Long id, Long timestamp, String type, String stack, String module, String source, String projectId, String environment, EnvironmentSnapshot environmentSnapshot) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
        this.stack = stack;
        this.module = module;
        this.source = source;
        this.projectId = projectId;
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
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取
     * @return stack
     */
    public String getStack() {
        return stack;
    }

    /**
     * 设置
     * @param stack
     */
    public void setStack(String stack) {
        this.stack = stack;
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
        return "ErrorMessage{id = " + id + ", timestamp = " + timestamp + ", type = " + type + ", stack = " + stack + ", module = " + module + ", source = " + source + ", projectId = " + projectId + ", environment = " + environment + ", environmentSnapshot = " + environmentSnapshot + "}";
    }

    public DataType getDataType() {
        return dataType;
    }
}
