package com.hubspot.android.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WebUtils {
    private static final DefaultHttpClient httpClient = new DefaultHttpClient();

    private static final String LOG_TAG = "hubspot.utils";

    /**
     * Retrieve the contents of a url as a Reader.
     * @param location
     * @return
     * @throws WebUtilsException
     */
    public static Reader getReaderForUrl(final String location) throws WebUtilsException {
        HttpGet conn = new HttpGet(location);
        try {
            return new BufferedReader(new InputStreamReader(getStreamFromConnection(conn), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            String message = String.format("Unsupported encoding for response stream when calling %s", location);
            Log.e(LOG_TAG, message, e);
            throw new WebUtilsException(message, e);
        }
    }

    /**
     * Retrieve the contents of a url as a string.
     * @param url
     * @return
     * @throws WebUtilsException
     */
    public static String get(final String url) throws WebUtilsException {
        HttpGet httpGet = new HttpGet(url);
        return convertStreamToString(getStreamFromConnection(httpGet));
    }

    /**
     * Create an InputStream from a HttpUriRequest.
     * 
     * @param connection
     * @return
     * @throws WebUtilsException
     */
    private static InputStream getStreamFromConnection(final HttpUriRequest connection) throws WebUtilsException {
        HttpEntity entity = null;
        try {
            HttpResponse response = httpClient.execute(connection);
            entity = response.getEntity();
            return entity.getContent();
        } catch (IOException ioException) {
            Log.w(LOG_TAG, ioException);
            throw new WebUtilsException(connection.getURI().toString(), connection.getMethod(), null, ioException);
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException ioException2) {
                    Log.w(LOG_TAG, ioException2);
                }
            }
        }
    }

    /**
     * Reads an InputStream to its end, and closes the stream
     * 
     * @param is
     * @return
     * @throws IOException
     */
    private static String convertStreamToString(final InputStream is) throws WebUtilsException {
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
            throw new WebUtilsException(message, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Swallow it, nothing to really do here.
            }
        }
    }
}
