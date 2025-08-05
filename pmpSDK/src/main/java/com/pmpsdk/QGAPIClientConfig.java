package com.pmpsdk;


import com.pmpsdk.client.QGAPIClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Setter
@Getter
@Configuration
@Data
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
@ConfigurationProperties("qgpmpsdk.client")
public class QGAPIClientConfig {

    private String accessKey;
    private String secretKey;
    private String projectToken;
    private String sentryUrl;
    private String apiBaseUrl;
    private String environment = "test"; // 默认值为 "test"



    @Bean
    public QGAPIClient qgApiClient() {
        return new QGAPIClient(accessKey, secretKey, projectToken, sentryUrl, apiBaseUrl, environment);
    }
}
