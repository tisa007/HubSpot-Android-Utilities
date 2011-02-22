package com.hubspot.android.utils.hubapi;

import java.util.List;

import android.test.AndroidTestCase;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.http.HttpUtilsException;

public class ApiHelperTest extends AndroidTestCase {

    private static final String TEST_BASE_API_URL = "https://api.hubapi.com/blog/v1/";
    private static final String TEST_LIST_API_PATH = "list?max=10";
    private static final String TEST_API_KEY = "demo";

    private static final String TEST_SINGLE_API_PATH = "799e8ccc-d442-489e-b4fd-aea56256fa6b.json";    

    private static final String TEST_CREATE_API_PATH = "799e8ccc-d442-489e-b4fd-aea56256fa6b/posts.json";

    private static final String TEST_UPDATE_API_PATH = "e507cc27-2e7d-4799-8665-78cb361b9175.json";

    private static final String TEST_PUT_API_BODY = "{\"body\":\"This is a unit test from my android client modified at: " + System.currentTimeMillis() + ".\"}";
    
    private ApiHelper apiHelper;

    public void testReadUrlToList() {
        Exception thrownEx = null;
        try {
            apiHelper.readUrlToList(null, null);
        } catch (IllegalArgumentException ex) {
            thrownEx = ex;
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get IllegalArgumentException.");
        }
        assertNotNull("Didn't throw IllegalArugmentException for a nul url.", thrownEx);

        List<ApiCallbackTest> testResults = null;
        try {
            testResults = apiHelper.readUrlToList(TEST_LIST_API_PATH, ApiCallbackTest.class);
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get list objects.");
        }
        assertNotNull(testResults);

        for (ApiCallbackTest testResult : testResults) {
            assertFalse(Utils.isEmpty(testResult.guid));
        }
    }

    public void testReadUrlToObject() {
        Exception thrownEx = null;
        try {
            apiHelper.readUrlToObject(null, null);
        } catch (IllegalArgumentException ex) {
            thrownEx = ex;
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get IllegalArgumentException.");
        }
        assertNotNull("Didn't throw IllegalArugmentException for a nul url.", thrownEx);

        ApiCallbackTest testResult = null;
        try {
            testResult = apiHelper.readUrlToObject(TEST_SINGLE_API_PATH, ApiCallbackTest.class);
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get list objects.");
        }
        assertNotNull(testResult);
        assertFalse(Utils.isEmpty(testResult.guid));
    }

    public void testPostNewApiObject() {
        Exception thrownEx = null;
        try {
            apiHelper.postNewApiObject(null, null);
        } catch (IllegalArgumentException ex) {
            thrownEx = ex;
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get IllegalArgumentException.");
        }
        assertNotNull("Didn't throw IllegalArugmentException for a nul url.", thrownEx);

        ApiCallbackTest testPutObj = new ApiCallbackTest();

        thrownEx = null;
        try {
            apiHelper.postNewApiObject(TEST_CREATE_API_PATH, testPutObj);
        } catch (ApiHelperException ex) {
            thrownEx = ex;
        }
        assertNotNull(thrownEx);
        assertTrue(((ApiHelperException) thrownEx).getCause() instanceof HttpUtilsException);
        assertTrue(((ApiHelperException) thrownEx).getResponseCode() > 201);

        testPutObj.authorEmail = "yshapira@hubspot.com";
        testPutObj.title = "Android ApiHelper Unit Test";
        testPutObj.body = "This is a unit test from my android Api Helper client";

        ApiCallbackTest testResult = null;
        try {
            testResult = (ApiCallbackTest) apiHelper.postNewApiObject(TEST_CREATE_API_PATH, testPutObj);
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get post new object.");
        }
        assertNotNull(testResult);
        assertFalse(Utils.isEmpty(testResult.guid));
    }

    public void testUpdateApiObject() {
        
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        apiHelper = new ApiHelper(TEST_API_KEY, TEST_BASE_API_URL);
    }
}
