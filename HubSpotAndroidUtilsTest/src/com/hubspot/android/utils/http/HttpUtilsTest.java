package com.hubspot.android.utils.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.test.AndroidTestCase;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.mock.MockHttpClient;

public class HttpUtilsTest extends AndroidTestCase {

    private HttpUtils httpUtils;

    protected void setUp() throws Exception {
        httpUtils = new HttpUtils();
        httpUtils.setHttpClient(new MockHttpClient(false, false));
    }

    public void testGet() {
        String response = null;
        try {
            response = httpUtils.get("https://hubapi.com/leads/v1/callback-url?hapikey=demo");
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertFalse(Utils.isEmpty(response));

        Exception thrownEx = null;
        try {
            httpUtils.get(null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false));
        thrownEx = null;
        try {
            httpUtils.get("https://hubapi.com/leads/v1/callback-url?hapikey=demo");
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true));
        thrownEx = null;
        try {
            httpUtils.get("https://hubapi.com/leads/v1/callback-url?hapikey=demo");
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
    }

    public void testGetReaderForUrl() {
        try {
            assertNotNull(httpUtils.getReaderForUrl("https://hubapi.com/leads/v1/callback-url?hapikey=demo"));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.getReaderForUrl(null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForUrl("https://hubapi.com/leads/v1/callback-url?hapikey=demo");
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true));
        thrownEx = null;
        try {
            httpUtils.getReaderForUrl("https://hubapi.com/leads/v1/callback-url?hapikey=demo");
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
    }

    public void testGetStreamFromConnection() {
        HttpUriRequest request = new HttpGet("https://hubapi.com/leads/v1/callback-url?hapikey=demo");
        try {
            assertNotNull(httpUtils.getStreamFromConnection(request));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.getStreamFromConnection(null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false));
        thrownEx = null;
        try {
            httpUtils.getStreamFromConnection(request);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true));
        thrownEx = null;
        try {
            httpUtils.getStreamFromConnection(request);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
    }

    public void testConvertStreamToString() {
        InputStream testStream = null;
        try {
            testStream = new ByteArrayInputStream("Test results".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertNotNull(testStream);

        try {
            assertFalse(Utils.isEmpty(httpUtils.convertStreamToString(testStream)));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }


        HttpUtilsException thrownEx = null;
        try {
            httpUtils.convertStreamToString(null);
        } catch (HttpUtilsException ex) {
            thrownEx = ex;
        }
        assertNotNull(thrownEx);
    
    }
}
