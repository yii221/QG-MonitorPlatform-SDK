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
