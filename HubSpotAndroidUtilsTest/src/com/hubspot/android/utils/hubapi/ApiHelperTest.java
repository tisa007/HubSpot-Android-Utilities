package com.hubspot.android.utils.hubapi;

import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;

import com.hubspot.android.utils.Utils;

public class ApiHelperTest extends AndroidTestCase {

    private static final String TEST_API_URL = "https://api.hubapi.com/leads/v1/list?hapikey=demo&max=10";
    
    private ApiHelper apiHelper;
    
    public void testReadUrlToList() {
        Exception thrownEx = null;
        try {
            apiHelper.readUrlToList(null);
        } catch (IllegalArgumentException ex) {
            thrownEx = ex;
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get IllegalArgumentException.");
        }
        assertNotNull("Didn't throw IllegalArugmentException for a nul url.", thrownEx);

        List<Map<String, Object>> testResults = null;
        try {
            testResults = apiHelper.readUrlToList(TEST_API_URL);
        } catch (ApiHelperException ex) {
            fail("Hit ApiHelperException when trying to get IllegalArgumentException.");
        }
        assertNotNull(testResults);

        for(Map<String, Object> testResult : testResults) {
            ApiCallbackTest test = new ApiCallbackTest(testResult);
            assertFalse(Utils.isEmpty(test.guid));
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        apiHelper = new ApiHelper();
    }
}
