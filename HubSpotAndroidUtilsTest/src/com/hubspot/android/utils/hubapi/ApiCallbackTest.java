package com.hubspot.android.utils.hubapi;

import java.util.HashMap;
import java.util.Map;

public class ApiCallbackTest implements HubApiObject {

    public String guid;

    public ApiCallbackTest() {
        guid = null;
    }

    public ApiCallbackTest(final String guid) {
        this.guid = guid;
    }

    public ApiCallbackTest(final Map<String, Object> input) {
        deserializeFromMap(input);
    }

    @Override
    public void deserializeFromMap(Map<String, Object> input) {
        if (input.containsKey("guid")) {
            this.guid = (String) input.get("guid");
        }
    }

    @Override
    public Map<String, Object> serializeToMap() {
        Map<String, Object> testValues = new HashMap<String, Object>(1);
        testValues.put("guid", guid);
        return testValues;
    }
}
