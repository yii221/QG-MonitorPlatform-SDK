package com.pmpsdk;

import com.pmpsdk.client.QGAPIClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
@ConfigurationProperties("qgpmpsdk.client")
public class QGAPIClientConfig {

    private String projectToken;
    private String environment = "test";
    private int maliciousThreshold = 3; // 默认阈值3次/秒

    public QGAPIClientConfig() {
    }

    public QGAPIClientConfig(String projectToken, String environment, int maliciousThreshold) {
        this.projectToken = projectToken;
        this.environment = environment;
        this.maliciousThreshold = maliciousThreshold;
    }


    @Bean
    public QGAPIClient qgApiClient() {
        return new QGAPIClient(projectToken, environment, maliciousThreshold);
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
     * @return maliciousThreshold
     */
    public int getMaliciousThreshold() {
        return maliciousThreshold;
    }

    /**
     * 设置
     * @param maliciousThreshold
     */
    public void setMaliciousThreshold(int maliciousThreshold) {
        this.maliciousThreshold = maliciousThreshold;
    }

    public String toString() {
        return "QGAPIClientConfig{projectToken = " + projectToken + ", environment = " + environment + ", maliciousThreshold = " + maliciousThreshold + "}";
    }
}
