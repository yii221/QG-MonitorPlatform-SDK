package com.pmpsdk.utils;

public enum PostUrl {
    LOG("http://192.168.1.156:8080/users/sdk"),
    ERROR("http://192.168.1.233:8080/messages/error"),
    PERFORMANCE("http://192.168.1.233:8080/messages/performance"),
    METHOD_USE_COUNT("http://192.168.1.233:8080/messages/getMethodUseCount");

    private final String url;

    PostUrl(String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }
}
