package com.hubspot.android.utils.http;

import java.util.Map;

public class HttpUtilsException extends Exception {
    private String url;
    private String method;
    private Integer responseCode;
    private Map<String, Object> params;

    public HttpUtilsException(final String url) {
        this(url, null, null, null);
    }

    public HttpUtilsException(final String url, final String method, final Map<String, Object> params) {
        this(url, method, null, params, null);
    }

    public HttpUtilsException(final String url, final String method, final Integer responseCode, final Map<String, Object> params) {
        this(url, method, responseCode, params, null);
    }

    public HttpUtilsException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpUtilsException(final Throwable throwable) {
        this(null, throwable);
    }

    public HttpUtilsException(final String url, final String method, final Integer responseCode, final Map<String, Object> params,
            final Throwable cause) {
        super(String.format("Error making %s request to '%s'", url, method), cause);
        this.url = url;
        this.method = method;
        this.responseCode = responseCode;
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
