package com.pmpsdk;


import com.pmpsdk.annotation.ThrowSDKException;
import com.pmpsdk.client.QGAPIClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan

/**
 * ----------------------------------------------
 * yml配置方式
 * qgpmpsdk:
 *   client:
 *     access-key: "your_access_key_123"
 *     secret-key: "your_secret_key_abc"
 *     project-token: "your_project_token_123"
 *     sentry-url: "your_sentry_url_123"
 *     api-base-url: "your_api_base_url_123"
 * ----------------------------------------------
 * properties方式
 * qgpmpsdk.client.access-key=your_access_key_123
 * qgpmpsdk.client.secret-key=your_secret_key_abc
 * qgpmpsdk.client.project-token=your_project_token_123
 * qgpmpsdk.client.sentry-url=your_sentry_url_123
 * qgpmpsdk.client.api-base-url=your_api_base_url_123
 */
@ThrowSDKException
@ConfigurationProperties("qgpmpsdk.client")
public class QGAPIClientConfig {

    private String projectToken;
    private String environment = "默认test";

    public QGAPIClientConfig() {
    }

    public QGAPIClientConfig(String projectToken, String environment) {
        this.projectToken = projectToken;
        this.environment = environment;
    }


    @Bean
    public QGAPIClient qgApiClient() {
        return new QGAPIClient(projectToken, environment);
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

    public String toString() {
        return "QGAPIClientConfig{projectToken = " + projectToken + ", environment = " + environment + "}";
    }
}
