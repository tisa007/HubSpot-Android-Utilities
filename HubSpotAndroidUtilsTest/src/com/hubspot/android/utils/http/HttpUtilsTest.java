package com.hubspot.android.utils.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.type.TypeFactory;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.hubapi.ApiCallbackTest;
import com.hubspot.android.utils.mock.MockHttpClient;

public class HttpUtilsTest extends AndroidTestCase {

    private static final String TEST_API_URL = "https://api.hubapi.com/leads/v1/callback-url?hapikey=demo";

    private static final String TEST_PUT_API_URL = "https://api.hubapi.com/blog/v1/posts/ae869ae5-3603-4075-b24f-c57b44931592.json?hapikey=demo";

    private static final String TEST_PUT_API_BODY = "This is a unit test from my android client modified at: " + System.currentTimeMillis() + ".";

    private static final String TEST_POST_API_URL = "https://api.hubapi.com/blog/v1/799e8ccc-d442-489e-b4fd-aea56256fa6b/posts.json?hapikey=demo";

    private static final String TEST_POST_API_BODY = "{\"authorEmail\":\"yshapira@hubspot.com\",\"title\":\"Android Unit Test\",\"body\":\"This is a unit test from my android client.\"}";

    private HttpUtils httpUtils;

    protected void setUp() throws Exception {
        httpUtils = new HttpUtils();
        httpUtils.setHttpClient(new MockHttpClient(false, false, false));
    }

    public void testGet() {
        String response = null;
        try {
            response = httpUtils.get(TEST_API_URL);
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

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.get(TEST_API_URL);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.get(TEST_API_URL);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.get(TEST_API_URL);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
    }

    public void testGetReaderForUrl() {
        try {
            assertNotNull(httpUtils.getReaderForUrl(TEST_API_URL));
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

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForUrl(TEST_API_URL);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForUrl(TEST_API_URL);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.getReaderForUrl(TEST_API_URL);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
    }

    public void testGetReaderForPut() {
        try {
            assertNotNull(httpUtils.getReaderForPut(TEST_PUT_API_URL, TEST_PUT_API_BODY));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.getReaderForPut(null, null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForPut(TEST_PUT_API_URL, TEST_PUT_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForPut(TEST_PUT_API_URL, TEST_PUT_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.getReaderForPut(TEST_PUT_API_URL, TEST_PUT_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
    }

    public void testPut() {
        try {
            assertNotNull(httpUtils.put(TEST_PUT_API_URL, TEST_PUT_API_BODY));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.put(null, null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.put(TEST_PUT_API_URL, TEST_PUT_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.put(TEST_PUT_API_URL, TEST_PUT_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.put(TEST_PUT_API_URL, TEST_PUT_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
    }

    public void testGetReaderForPost() {
        try {
            assertNotNull(httpUtils.getReaderForPost(TEST_POST_API_URL, TEST_POST_API_BODY));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.getReaderForPost(null, null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForPost(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.getReaderForPost(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.getReaderForPost(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
    }

    public void testPost() {
        try {
            assertNotNull(httpUtils.post(TEST_POST_API_URL, TEST_POST_API_BODY));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.put(null, null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.post(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.post(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.post(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
    }

    public void testGetStreamFromConnection() {
        HttpUriRequest request = new HttpGet(TEST_API_URL);
        try {
            assertNotNull(httpUtils.getStreamFromRequest(request));
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run mock get.");
        }

        Exception thrownEx = null;
        try {
            httpUtils.getStreamFromRequest(null);
        } catch (IllegalArgumentException e) {
            thrownEx = e;
        } catch (Exception e) {
            fail("Hit exception when trying to run mock get.");
        }
        assertNotNull("Should have thrown an IllegalArgumentException", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(true, false, false));
        thrownEx = null;
        try {
            httpUtils.getStreamFromRequest(request);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, true, false));
        thrownEx = null;
        try {
            httpUtils.getStreamFromRequest(request);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }
        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);

        httpUtils.setHttpClient(new MockHttpClient(false, false, true));
        thrownEx = null;
        try {
            httpUtils.getStreamFromRequest(request);
        } catch (HttpUtilsException e) {
            thrownEx = e;
        }

        assertNotNull("Should have thrown an HttpUtilsException.", thrownEx);
        assertNotNull(((HttpUtilsException) thrownEx).getResponseCode());
        assertTrue(((HttpUtilsException) thrownEx).getResponseCode() > 201);
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

    public void testRealGet() {
        // Reset http client, allow for DefaultHttpClient instead of Mock.
        httpUtils.setHttpClient(null);
        String response = null;
        try {
            response = httpUtils.get(TEST_API_URL);
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run real get.");
        }
        assertFalse(Utils.isEmpty(response));
    }

    public void testRealPost() {
        // Reset http client, allow for DefaultHttpClient instead of Mock.
        httpUtils.setHttpClient(null);
        String response = null;
        try {
            response = httpUtils.post(TEST_POST_API_URL, TEST_POST_API_BODY);
        } catch (HttpUtilsException e) {
            fail("Hit exception when trying to run real get.");
        }
        assertFalse(Utils.isEmpty(response));
        Log.i("hubspot.utils.http", response);
    }

    public void testRealPut() {
        // Reset http client, allow for DefaultHttpClient instead of Mock.
        httpUtils.setHttpClient(null);
        String response = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String getResponse = httpUtils.get(TEST_PUT_API_URL);
            ApiCallbackTest originalPost = mapper.readValue(getResponse, TypeFactory.type(ApiCallbackTest.class));

            //Actually execute update:
            response = httpUtils.put(TEST_PUT_API_URL, TEST_PUT_API_BODY);

            //Verify update:
            getResponse = httpUtils.get(TEST_PUT_API_URL);
            ApiCallbackTest updatedPost = mapper.readValue(getResponse, TypeFactory.type(ApiCallbackTest.class));
            assertEquals(TEST_PUT_API_BODY, updatedPost.body);
            
            //Revert update:
            httpUtils.put(TEST_PUT_API_URL, originalPost.body);
        } catch (Exception ex) {
            String message = "Hit exception when trying to run real put.";
            Log.e(HttpUtils.LOG_TAG, message, ex);
            fail(message);
        }
        Log.i("hubspot.utils.http", response);
    }
}
