package com.pmpsdk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: // 类说明
 * @ClassName: Message    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 17:01   // 时间
 * @Version: 1.0     // 版本
 */

public class Message {
    private Long timestamp;

    private String type;

    private String stack;

    private String data;

    private String model;

    private String source = "backend";

    private String level;

    private Long userId;

    private String projectId;

    private Integer lock = 0;


    public Message() {
    }

    public Message(Long timestamp, String type, String stack, String data, String model, String source, String level, Long userId, String projectId) {
        this.timestamp = timestamp;
        this.type = type;
        this.stack = stack;
        this.data = data;
        this.model = model;
        this.source = source;
        this.level = level;
        this.userId = userId;
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
     * @return data
     */
    public String getData() {
        return data;
    }

    /**
     * 设置
     * @param data
     */
    public void setData(String data) {
        this.data = data;
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
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * @return lock
     */
    public Integer getLock() {
        return lock;
    }
    /**
     * 设置
     * @param lock
     */
    public void setLock(Integer lock) {
        this.lock = lock;
    }

    public String toString() {
        return "Message{timestamp = " + timestamp + ", type = " + type + ", stack = " + stack + ", data = " + data + ", model = " + model + ", source = " + source + ", level = " + level + ", userId = " + userId + ", projectId = " + projectId + "}";
    }
}
