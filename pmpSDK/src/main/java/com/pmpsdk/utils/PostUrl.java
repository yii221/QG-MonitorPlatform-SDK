package com.pmpsdk.utils;

public enum PostUrl {
    LOG("http://47.113.224.195:30422/api/backend/log"),
    ERROR("http://47.113.224.195:30422/api/backend/error"),
    PERFORMANCE("http://47.113.224.195:30422/api/backend/performance"),
    METHOD_USE_COUNT("http://47.113.224.195:30422/api/backend/getMethodUseCount");

    private final String url;

    PostUrl(String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }
}
