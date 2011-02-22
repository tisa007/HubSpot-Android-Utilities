package com.hubspot.android.utils.hubapi;

public class ApiKeyHelperException extends Exception {
    public ApiKeyHelperException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public ApiKeyHelperException(String message) {
        super(message);
    }
}
