package com.pmpsdk.utils;

import com.pmpsdk.annotation.ThrowSDKException;
import com.pmpsdk.client.QGAPIClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@ThrowSDKException
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 获取配置类中的项目token
     */
    public static String PROJECT_TOKEN;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 通过类获取bean
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext != null) {
            return applicationContext.getBean(clazz);
        }
        return null;
    }

    /**
     * 项目运行时读取 token
     */
    @PostConstruct
    public void initProjectToken() {
        QGAPIClient client = getBean(QGAPIClient.class);
        if (client != null) {
            PROJECT_TOKEN = client.getProjectToken();
        }
    }
}
