package com.hubspot.android.utils.http;

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
        // TODO: implement me
        assertTrue(false);
    }

    public void testGetStreamFromConnection() {
        // TODO: implement me
        assertTrue(false);
    }

    public void testConvertStreamToString() {
        // TODO: implement me
        assertTrue(false);
    }
}
