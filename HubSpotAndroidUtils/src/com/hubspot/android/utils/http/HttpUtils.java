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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.hubspot.android.utils.Utils;

/**
 * A set of utilities for retrieving and uploading data via HTTP requests.
 */
public class HttpUtils implements IHttpUtils {
    private HttpClient httpClient;

    private final String LOG_TAG = "hubspot.utils";

    /* (non-Javadoc)
     * @see com.hubspot.android.utils.http.IHttpUtils#getReaderForUrl(java.lang.String)
     */
    public Reader getReaderForUrl(final String url) throws HttpUtilsException {
        if (Utils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be null or empty to run GET request.");
        }

        HttpGet httpGet = new HttpGet(url);
        try {
            return new BufferedReader(new InputStreamReader(getStreamFromRequest(httpGet), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            String message = String.format("Unsupported encoding for response stream when calling %s", url);
            Log.e(LOG_TAG, message, e);
            throw new HttpUtilsException(message, e);
        }
    }

    /* (non-Javadoc)
     * @see com.hubspot.android.utils.http.IHttpUtils#get(java.lang.String)
     */
    public String get(final String url) throws HttpUtilsException {
        if (Utils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be null or empty to run GET request.");
        }

        HttpGet httpGet = new HttpGet(url);
        return convertStreamToString(getStreamFromRequest(httpGet));
    }

    /* (non-Javadoc)
     * @see com.hubspot.android.utils.http.IHttpUtils#getReaderForPut(java.lang.String, java.lang.String)
     */
    public Reader getReaderForPut(final String url, final String putBody) throws HttpUtilsException {
        HttpPut httpPut = createHttpPutRequest(url, putBody);
        try {
            return new BufferedReader(new InputStreamReader(getStreamFromRequest(httpPut), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            String message = String.format("Unsupported encoding for response stream when calling %s", url);
            Log.e(LOG_TAG, message, e);
            throw new HttpUtilsException(message, e);
        }
    }

    /* (non-Javadoc)
     * @see com.hubspot.android.utils.http.IHttpUtils#put(java.lang.String, java.lang.String)
     */
    public String put(final String url, final String putBody) throws HttpUtilsException {
        HttpPut httpPut = createHttpPutRequest(url, putBody);
        return convertStreamToString(getStreamFromRequest(httpPut));
    }

    private HttpPut createHttpPutRequest(final String url, final String putBody) throws HttpUtilsException {
        if (Utils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be null or empty to run PUT request.");
        }

        if (Utils.isEmpty(putBody)) {
            throw new IllegalArgumentException("Put body must not be null or empty to run PUT request.");
        }

        HttpPut httpPut = new HttpPut(url);
        //TODO: make this support non-JSON types.
        httpPut.setHeader("Content-type", "application/json");
        ByteArrayEntity putEntity;
        try {
            putEntity = new ByteArrayEntity(putBody.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new HttpUtilsException("Unsupported encoding for PUT body.", e);
        }
        httpPut.setEntity(putEntity);
        return httpPut;
    }

    
    /* (non-Javadoc)
     * @see com.hubspot.android.utils.http.IHttpUtils#getReaderForPost(java.lang.String, java.lang.String)
     */
    public Reader getReaderForPost(final String url, final String postBody) throws HttpUtilsException {
        HttpPost httpPost = createHttpPostRequest(url, postBody);
        try {
            return new BufferedReader(new InputStreamReader(getStreamFromRequest(httpPost), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            String message = String.format("Unsupported encoding for response stream when calling %s", url);
            Log.e(LOG_TAG, message, e);
            throw new HttpUtilsException(message, e);
        }
    }

    /* (non-Javadoc)
     * @see com.hubspot.android.utils.http.IHttpUtils#post(java.lang.String, java.lang.String)
     */
    public String post(final String url, final String postBody) throws HttpUtilsException {
        HttpPost httpPost = createHttpPostRequest(url, postBody);
        return convertStreamToString(getStreamFromRequest(httpPost));
    }

    private HttpPost createHttpPostRequest(final String url, final String postBody) throws HttpUtilsException {
        if (Utils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be null or empty to run POST request.");
        }

        if (Utils.isEmpty(postBody)) {
            throw new IllegalArgumentException("Post body must not be null or empty to run POST request.");
        }

        HttpPost httpPost = new HttpPost(url);
        //TODO: make this support non-JSON types.
        httpPost.setHeader("Content-type", "application/json");
        ByteArrayEntity postEntity;
        try {
            postEntity = new ByteArrayEntity(postBody.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new HttpUtilsException("Unsupported encoding for POST body.", e);
        }
        httpPost.setEntity(postEntity);
        return httpPost;
    }
    
    /**
     * Create an InputStream from a HttpUriRequest.
     * 
     * @param connection
     * @return
     * @throws HttpUtilsException
     */
    protected InputStream getStreamFromRequest(final HttpUriRequest connection) throws HttpUtilsException {
        // TODO: try to get params out of request for exception logging.
        if (connection == null) {
            throw new IllegalArgumentException("Can't get response stream from null HttpUriRequest");
        }

        HttpEntity entity = null;
        try {
            HttpResponse response = getHttpClient().execute(connection);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201) {
                    throw new HttpUtilsException(connection.getURI().toString(), connection.getMethod(), response
                            .getStatusLine().getStatusCode(), null);
                }
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
            throw new HttpUtilsException(connection.getURI().toString(), connection.getMethod(), null, null,
                    ioException);
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
