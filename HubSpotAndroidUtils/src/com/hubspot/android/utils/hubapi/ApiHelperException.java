package com.hubspot.android.utils.hubapi;

import com.hubspot.android.utils.http.HttpUtilsException;

public class ApiHelperException extends Exception {

    private Integer responseCode;
    
    private String hapiUrl;

    public ApiHelperException(String hapiUrl, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.hapiUrl = hapiUrl;
        if (throwable instanceof HttpUtilsException) {
            this.responseCode = ((HttpUtilsException) throwable).getResponseCode();
        }
    }

    public String getHapiUrl() {
        return hapiUrl;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
}
