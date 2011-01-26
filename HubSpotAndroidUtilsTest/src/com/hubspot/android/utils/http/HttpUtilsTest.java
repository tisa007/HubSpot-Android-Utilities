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
            fail("Hit exception when trying to run simple get.");
        }
        assertFalse(Utils.isEmpty(response));
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
