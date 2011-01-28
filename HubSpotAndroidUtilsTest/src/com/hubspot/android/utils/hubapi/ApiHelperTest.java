package com.hubspot.android.utils.hubapi;

import java.util.List;

import android.test.AndroidTestCase;

import com.hubspot.android.utils.Utils;

public class ApiHelperTest extends AndroidTestCase {

    private static final String TEST_BASE_API_URL = "https://api.hubapi.com/leads/v1/";
    private static final String TEST_API_PATH = "list?max=10";
    private static final String TEST_API_KEY = "demo";

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
            testResults = apiHelper.readUrlToList(TEST_API_PATH, ApiCallbackTest.class);
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get IllegalArgumentException.");
        }
        assertNotNull(testResults);

        for (ApiCallbackTest testResult : testResults) {
            assertFalse(Utils.isEmpty(testResult.guid));
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        apiHelper = new ApiHelper(TEST_API_KEY, TEST_BASE_API_URL);
    }
}
