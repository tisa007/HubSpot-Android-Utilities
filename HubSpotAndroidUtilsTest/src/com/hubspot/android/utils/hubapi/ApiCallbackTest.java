package com.hubspot.android.utils.hubapi;

public class ApiCallbackTest {

    public String guid;
    
    public String title;
    
    public String authorEmail;
    
    public String body;

    public ApiCallbackTest() {
        guid = null;
    }

    public ApiCallbackTest(final String guid) {
        this.guid = guid;
    }
}
