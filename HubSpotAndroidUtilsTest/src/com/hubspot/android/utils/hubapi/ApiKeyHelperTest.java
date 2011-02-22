package com.hubspot.android.utils.hubapi;

import android.test.AndroidTestCase;

import com.hubspot.android.utils.mock.MockHttpUtils;

public class ApiKeyHelperTest extends AndroidTestCase {
    private MockHttpUtils httpUtils;
    private ApiKeyHelper keyHelper;

    public void setUp() {
        httpUtils = new MockHttpUtils();
        keyHelper = new ApiKeyHelper(httpUtils);
    }

    public void testRequestApiKey() throws ApiKeyHelperException {
        String expectedKey = "expected";
        String username = "username";
        String password = "password";
        String jsonResponse = "{ \"result\": \"true\", \"message\": \"" + expectedKey + "\", \"portalId\": \"46064\" }";

        httpUtils.setResponseString(jsonResponse);

        try {
            keyHelper.requestApiKey(null, null, null);
            fail("Expected ApiKeyHelperException for all-null input");
        } catch (ApiKeyHelperException apiKeyHelperException) {
            // expected
        }
        try {
            keyHelper.requestApiKey(null, password, null);
            fail("Expected ApiKeyHelperException for null username");
        } catch (ApiKeyHelperException apiKeyHelperException) {
            // expected
        }
        try {
            keyHelper.requestApiKey(username, null, null);
            fail("Expected ApiKeyHelperException for null password");
        } catch (ApiKeyHelperException apiKeyHelperException) {
            // expected
        }

        String actualKey = keyHelper.requestApiKey(username, password, null);
        assertEquals(expectedKey, actualKey);
    }

    public void testAuthErrors() throws ApiKeyHelperException {
        String message = "o shiz";
        String unauthorizedJson = "{ \"result\": \"false\", \"message\": \"" + message
                + "\" }";
        httpUtils.setResponseString(unauthorizedJson);

        try {
            keyHelper.requestApiKey("username", "password", null);
            fail("Expected ApiKeyHelperException");
        } catch (ApiKeyHelperException apiKeyHelperException) {
            assertEquals(message, apiKeyHelperException.getLocalizedMessage());
        }
    }
}
