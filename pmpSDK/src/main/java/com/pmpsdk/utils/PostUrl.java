package com.pmpsdk.utils;

public enum PostUrl {
    LOG("http://192.168.1.233:8080/backend/log"),
    ERROR("http://192.168.1.161:8080/backend/error"),
    PERFORMANCE("http://192.168.1.156:8080/backend/performance"),
    METHOD_USE_COUNT("http://192.168.1.233:8080/backend/getMethodUseCount");

    private final String url;

    PostUrl(String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }
}
