package com.hubspot.android.utils.hubapi;

public class ApiHelperException extends Exception {

    private String hapiUrl;

    public ApiHelperException(String hapiUrl, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.hapiUrl = hapiUrl;
    }

    public String getHapiUrl() {
        return hapiUrl;
    }
}
