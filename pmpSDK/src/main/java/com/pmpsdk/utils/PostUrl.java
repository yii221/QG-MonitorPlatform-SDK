package com.pmpsdk.utils;

import com.pmpsdk.annotation.ThrowSDKException;

@ThrowSDKException
public enum PostUrl {
    LOG("http://192.168.1.156:8080/users/sdk"),
    ERROR("http://192.168.1.233:8080/messages/error"),
    GET_ALERT_RULE("http://192.168.1.233:8080/messages/getAlertRule");

    private final String url;

    PostUrl(String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }
}
