package com.hubspot.android.utils.hubapi;

import java.util.Map;

public interface HubApiObject {
    
    public void deserializeFromMap(final Map<String, Object> input);
    
    public Map<String, Object> serializeToMap();
    
}
