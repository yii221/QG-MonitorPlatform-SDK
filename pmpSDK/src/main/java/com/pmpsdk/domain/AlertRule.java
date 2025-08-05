package com.pmpsdk.domain;


public class AlertRule {
    private Long id;
    private String errorType;
    private Integer threshold;
    private String projectToken;

    public AlertRule() {
    }

    public AlertRule(Long id, String errorType, Integer threshold, String projectToken) {
        this.id = id;
        this.errorType = errorType;
        this.threshold = threshold;
        this.projectToken = projectToken;
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
     * @return errorType
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * 设置
     * @param errorType
     */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
     * 获取
     * @return threshold
     */
    public Integer getThreshold() {
        return threshold;
    }

    /**
     * 设置
     * @param threshold
     */
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    /**
     * 获取
     * @return projectToken
     */
    public String getProjectToken() {
        return projectToken;
    }

    /**
     * 设置
     * @param projectToken
     */
    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }

    public String toString() {
        return "AlertRule{id = " + id + ", errorType = " + errorType + ", threshold = " + threshold + ", projectToken = " + projectToken + "}";
    }
}
