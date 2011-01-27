package com.hubspot.android.utils.hubapi;

import android.test.AndroidTestCase;

public class ApiHelperTest extends AndroidTestCase {

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
        
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        apiHelper = new ApiHelper();
    }
    
}
