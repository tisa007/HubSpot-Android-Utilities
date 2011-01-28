package com.hubspot.android.utils.mock;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class MockHttpClient implements HttpClient {

    private boolean throwExceptions;
    private boolean returnEmpty;
    private boolean returnErrors;

    public MockHttpClient(final boolean throwExceptions, final boolean returnEmpty, final boolean returnErrors) {
        this.throwExceptions = throwExceptions;
        this.returnEmpty = returnEmpty;
        this.returnErrors = returnErrors;
    }

    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        return executeMockRequest();
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException,
            ClientProtocolException {
        return executeMockRequest();
    }

    /**
     * Run real work of returning errors, throwing exceptions, etc.
     * 
     * @return
     * @throws IOException
     */
    private HttpResponse executeMockRequest() throws IOException {
        if (throwExceptions) {
            throw new IOException();
        }
        if (returnEmpty) {
            return null;
        }
        if (returnErrors) {
            return new MockHttpResponse(500);
        }
        return new MockHttpResponse(201);

    }

    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1, ResponseHandler<? extends T> arg2, HttpContext arg3)
            throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1, ResponseHandler<? extends T> arg2) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1, HttpContext arg2) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpParams getParams() {
        // TODO Auto-generated method stub
        return null;
    }

}
