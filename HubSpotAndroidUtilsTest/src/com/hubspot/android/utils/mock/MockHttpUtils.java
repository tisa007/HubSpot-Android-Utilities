package com.hubspot.android.utils.mock;

import java.io.Reader;
import java.io.StringReader;

import org.apache.http.HttpResponse;

import com.hubspot.android.utils.http.HttpUtils;
import com.hubspot.android.utils.http.HttpUtilsException;
import com.hubspot.android.utils.http.IHttpUtils;

public class MockHttpUtils implements IHttpUtils {
    private String responseString;
    private HttpUtilsException responseException;

    private String doString() throws HttpUtilsException {
        if (responseException != null) {
            throw responseException;
        }
        
        return responseString;
    }
    
    private Reader doReader() throws HttpUtilsException {
        if (responseException != null) {
            throw responseException;
        }
        
        return new StringReader(responseString);
    }
    
    @Override
    public String get(String url) throws HttpUtilsException {
        return doString();
    }


    @Override
    public Reader getReaderForPost(String url, String postBody) throws HttpUtilsException {
        return doReader();
    }


    @Override
    public Reader getReaderForPut(String url, String putBody) throws HttpUtilsException {
        return doReader();
    }


    @Override
    public Reader getReaderForUrl(String url) throws HttpUtilsException {
        return doReader();
    }


    @Override
    public String post(String url, String postBody) throws HttpUtilsException {
        return doString();
    }


    @Override
    public String put(String url, String putBody) throws HttpUtilsException {
        return doString();
    }


    public String getResponseString() {
        return responseString;
    }


    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }


    public HttpUtilsException getResponseException() {
        return responseException;
    }


    public void setResponseException(HttpUtilsException responseException) {
        this.responseException = responseException;
    }
}
