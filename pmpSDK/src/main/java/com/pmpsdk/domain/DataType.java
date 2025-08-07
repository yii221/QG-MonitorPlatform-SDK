package com.pmpsdk.domain;


public enum DataType {

    LOG("log"),
    ERROR("error"),
    PERFORMANCE("performance");

    private final String type;

    DataType(String type) {
        this.type = type;
    }

    String getType() {
        return type;
    }
}
