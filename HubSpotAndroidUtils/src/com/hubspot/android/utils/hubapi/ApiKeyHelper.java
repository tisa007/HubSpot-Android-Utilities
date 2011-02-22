package com.hubspot.android.utils.hubapi;

import java.net.URLEncoder;
import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;

import android.text.Html.TagHandler;
import android.util.Log;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.http.HttpUtilsException;
import com.hubspot.android.utils.http.IHttpUtils;

public class ApiKeyHelper {
    /** The HttpUtils to use for web requests */
    private final IHttpUtils httpUtils;
    
    /** The JSON mapper to use */
    private final ObjectMapper mapper;
    
    /**
     * Create an ApiKeyHelper
     * @param httpUtils The HttpUtils to use for web requests
     */
    public ApiKeyHelper(final IHttpUtils httpUtils) {
        this.httpUtils = httpUtils;
        this.mapper = new ObjectMapper();
    }
    
    /**
     * Requests an API key
     * @param username
     * @param password
     * @param portalId
     * @return
     */
    public String requestApiKey(final CharSequence username, final CharSequence password, final Long portalId) throws ApiKeyHelperException {
        if (Utils.isEmpty(username) || Utils.isEmpty(password)) {
            throw new ApiKeyHelperException("Username and password are required");
        }
        
        StringBuilder url = new StringBuilder("https://api.hubapi.com/keys/api-add?userName=");
        url.append(URLEncoder.encode(username.toString()));
        url.append("&password=");
        url.append(URLEncoder.encode(password.toString()));
        if (portalId != null) {
            url.append("&portalId=");
            url.append(portalId);
        }
        
        String response;
        try {
            response = httpUtils.get(url.toString());
        } catch (HttpUtilsException httpUtilsException) {
            throw new ApiKeyHelperException("Couldn't communicate with HubSpot", httpUtilsException);
        }
        
        HashMap<String, String> responseJson;
        try {
            responseJson = mapper.readValue(response, HashMap.class);
        } catch (Exception exception) {
            throw new ApiKeyHelperException("Couldn't read response", exception);
        }
        
        if (!responseJson.containsKey("result") || !responseJson.containsKey("message")) {
            throw new ApiKeyHelperException("Couldn't understand response");
        }
        
        boolean success = "true".equals(responseJson.get("result"));
        String message = responseJson.get("message");
        
        if (!success) {
            throw new ApiKeyHelperException(message);
        }
        
        return message;
    }
}
