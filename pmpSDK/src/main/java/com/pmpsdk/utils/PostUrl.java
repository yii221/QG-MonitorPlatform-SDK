package com.pmpsdk.utils;

public enum PostUrl {
    LOG("http://192.168.1.233:8080/messages/log"),
    ERROR("http://192.168.1.233:8080/messages/error");

    private final String url;

    PostUrl(String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }
}
