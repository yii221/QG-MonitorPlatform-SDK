package com.pmpsdk.utils;

import com.pmpsdk.client.QGAPIClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取配置类中的项目token
     */
    public static String PROJECT_TOKEN;
    public static int MALICIOUS_THRESHOLD;

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
     * 项目运行时读取 yml
     */
    @PostConstruct
    public static void initReadYml() {
        QGAPIClient client = getBean(QGAPIClient.class);
        if (client != null) {
            PROJECT_TOKEN = client.getProjectToken();
            MALICIOUS_THRESHOLD = client.getMaliciousThreshold() >= 0
                    ? client.getMaliciousThreshold()
                    : 999999;
            System.err.println("\n===Q=G===>项目加载成功，当前token：" +
                    (PROJECT_TOKEN != null && PROJECT_TOKEN.length() > 3 ?
                            "*****" + PROJECT_TOKEN.substring(PROJECT_TOKEN.length() - 3) :
                            (PROJECT_TOKEN != null ? PROJECT_TOKEN : "无效token")) +
                    "\n");
        }
    }
}
