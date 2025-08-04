package com.pmpsdk.domain;

/**
 * @Description: 报错信息  // 类说明
 * @ClassName: ErrorMessage    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 20:45   // 时间
 * @Version: 1.0     // 版本
 */
public class ErrorMessage {

    private Long timestamp;

    private String type;

    private String stack;

    private String model;

    private String source = "backend";

    private String level;

    private String projectId;


    public ErrorMessage() {
    }

    public ErrorMessage(Long timestamp, String type, String stack, String model, String source, String level, String projectId) {
        this.timestamp = timestamp;
        this.type = type;
        this.stack = stack;
        this.model = model;
        this.source = source;
        this.level = level;
        this.projectId = projectId;
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
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * 设置
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
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

    public String toString() {
        return "ErrorMessage{timestamp = " + timestamp + ", type = " + type + ", stack = " + stack + ", model = " + model + ", source = " + source + ", level = " + level + ", projectId = " + projectId + "}";
    }
}
