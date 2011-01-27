package com.hubspot.android.utils.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.hubspot.android.utils.Utils;

/**
 * A set of utilities for retrieving and uploading data via HTTP requests.
 */
public class HttpUtils {
    private HttpClient httpClient;

    private final String LOG_TAG = "hubspot.utils";

    /**
     * Retrieve the contents of a url as a Reader.
     * @param url
     * @return
     * @throws HttpUtilsException
     */
    public Reader getReaderForUrl(final String url) throws HttpUtilsException {
        if (Utils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be null or empty to run GET request.");
        }

        HttpGet conn = new HttpGet(url);
        try {
            return new BufferedReader(new InputStreamReader(getStreamFromConnection(conn), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            String message = String.format("Unsupported encoding for response stream when calling %s", url);
            Log.e(LOG_TAG, message, e);
            throw new HttpUtilsException(message, e);
        }
    }

    /**
     * Retrieve the contents of a url as a string.
     * @param url
     * @return
     * @throws HttpUtilsException
     */
    public String get(final String url) throws HttpUtilsException {
        if (Utils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be null or empty to run GET request.");
        }

        HttpGet httpGet = new HttpGet(url);
        return convertStreamToString(getStreamFromConnection(httpGet));
    }

    /**
     * Create an InputStream from a HttpUriRequest.
     * 
     * @param connection
     * @return
     * @throws HttpUtilsException
     */
    protected InputStream getStreamFromConnection(final HttpUriRequest connection) throws HttpUtilsException {
        if (connection == null) {
            throw new IllegalArgumentException("Can't get response stream from null HttpUriRequest");
        }

        HttpEntity entity = null;
        try {
            HttpResponse response = getHttpClient().execute(connection);
            if (response != null) {
                entity = response.getEntity();
                return entity.getContent();
            }
            throw new HttpUtilsException(connection.getURI().toString(), connection.getMethod(), null, null);
        } catch (IOException ioException) {
            Log.w(LOG_TAG, ioException);
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException ioException2) {
                    Log.w(LOG_TAG, ioException2);
                }
            }
            throw new HttpUtilsException(connection.getURI().toString(), connection.getMethod(), null, ioException);
        }
    }

    /**
     * Reads an InputStream to its end, and closes the stream
     * 
     * @param is
     * @return
     * @throws IOException
     */
    protected String convertStreamToString(final InputStream is) throws HttpUtilsException {
        if (is == null) {
            throw new HttpUtilsException("Null input stream, can't convert to string.");
        }

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] buf = new byte[2048];
            int ret = 0;
            while ((ret = is.read(buf)) > 0) {
                os.write(buf, 0, ret);
            }
            is.close();

            return new String(os.toByteArray(), "UTF-8");
        } catch (IOException e) {
            String message = "Exception while trying to parse inputs stream from response.";
            Log.e(LOG_TAG, message, e);
            throw new HttpUtilsException(message, e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                // Swallow it, nothing to really do here.
            }
        }
    }

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }
    
    protected void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
