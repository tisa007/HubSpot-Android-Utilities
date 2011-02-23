package com.hubspot.android.utils.hubapi;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.type.TypeFactory;

import android.util.Log;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.http.HttpUtils;
import com.hubspot.android.utils.http.HttpUtilsException;
import com.hubspot.android.utils.http.IHttpUtils;

public class ApiHelper {

    private static final String LOG_TAG = "hubspot.utils.hapi";
    public static final String DEFAULT_BASE_URL = "https://api.hubapi.com";

    private static final ObjectMapper mapper = new ObjectMapper();
    private IHttpUtils httpUtils;

    private final String apiKey;
    private final String baseApiUrl;
    
    public ApiHelper(final String apiKey, final String baseApiUrl) {
        this.apiKey = apiKey;
        if (baseApiUrl.endsWith("/")) {
            this.baseApiUrl = baseApiUrl.substring(0, baseApiUrl.length() - 1);
        } else {
            this.baseApiUrl = baseApiUrl;
        }
    }

    
    @SuppressWarnings("unchecked")
    public <T> T postNewApiObject(final String apiPath, final T object) throws ApiHelperException {
        if (Utils.isEmpty(apiPath)) {
            throw new IllegalArgumentException("Must include a non-blank api url to create object via api.");
        }

        if (object == null) {
            throw new IllegalArgumentException("Must include a non-null object to create object via api.");
        }

        Log.d(LOG_TAG, apiPath);
        try {
            return (T) readApiJsonToObject(apiPath, getHttpUtils().getReaderForPost(getFullApiUrl(apiPath), mapper.writeValueAsString(object)), object.getClass());
        } catch (HttpUtilsException ex) {
            if (ex.getResponseCode() != null) {
                Log.e(LOG_TAG, "Got " + ex.getResponseCode() + " response from POST request.", ex);
            }
            throw new ApiHelperException(getFullApiUrl(apiPath), null, ex);
        } catch (ApiHelperException ex) {
            throw new ApiHelperException(getFullApiUrl(apiPath), null, ex);
        } catch (Exception ex) {
            Log.e(LOG_TAG,"Error serializing object to json.", ex);
            throw new ApiHelperException(getFullApiUrl(apiPath), null, ex);
        }
    }

    public <T> T readUrlToObject(final String apiPath, final Class<T> clazz) throws ApiHelperException {
        if (Utils.isEmpty(apiPath)) {
            throw new IllegalArgumentException("Must include a non-blank api url to read results.");
        }

        Log.d(LOG_TAG, apiPath);
        try {
            return readApiJsonToObject(apiPath, getHttpUtils().getReaderForUrl(getFullApiUrl(apiPath)), clazz);
        } catch (HttpUtilsException ex) {
            throw new ApiHelperException(getFullApiUrl(apiPath), null, ex);
        }
    }
    
    private <T> T readApiJsonToObject(final String apiUrl, final Reader jsonReader, final Class<T> clazz) throws ApiHelperException {
        try {
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonReader, TypeFactory.type(clazz));
        } catch (JsonParseException jsonParseException) {
            Log.e(LOG_TAG, "Exception parsing JSON", jsonParseException);
            throw new ApiHelperException(apiUrl, "Couldn't parse JSON from API response.", jsonParseException);
        } catch (JsonMappingException jsonMappingException) {
            Log.e(LOG_TAG, "Exception parsing JSON", jsonMappingException);
            throw new ApiHelperException(apiUrl, "Couldn't parse JSON from API response.", jsonMappingException);
        } catch (IOException ioException) {
            Log.e(LOG_TAG, "Exception retrieving response.", ioException);
            throw new ApiHelperException(apiUrl, "Couldn't read JSON, IOException when retrieving response.", ioException);
        } catch(Exception ex) {
            Log.e(LOG_TAG, "Exception retrieving response.", ex);
            throw new ApiHelperException(apiUrl, "Couldn't read JSON, IOException when retrieving response.", ex);
        }
    }
    
    /**
     * Take a HubApi URL, query it, and process the results 
     * @param url
     * @return
     * @throws LeadsException 
     */
    public <T> List<T> readUrlToList(final String apiPath, final Class<T> clazz) throws ApiHelperException {
        if (Utils.isEmpty(apiPath)) {
            throw new IllegalArgumentException("Must include a non-blank api url to read results.");
        }

        Log.d(LOG_TAG, apiPath);
        try {
            return readApiJsonToList(apiPath, getHttpUtils().getReaderForUrl(getFullApiUrl(apiPath)), clazz);
        } catch (HttpUtilsException ex) {
            throw new ApiHelperException(getFullApiUrl(apiPath), null, ex);
        }
    }

    private <T> List<T> readApiJsonToList(final String apiUrl, final Reader jsonReader, final Class<T> clazz) throws ApiHelperException {
        try {
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonReader, TypeFactory.collectionType(List.class, clazz));
        } catch (JsonParseException jsonParseException) {
            Log.e(LOG_TAG, "Exception parsing JSON", jsonParseException);
            throw new ApiHelperException(apiUrl, "Couldn't parse JSON from API response.", jsonParseException);
        } catch (JsonMappingException jsonMappingException) {
            Log.e(LOG_TAG, "Exception parsing JSON", jsonMappingException);
            throw new ApiHelperException(apiUrl, "Couldn't parse JSON from API response.", jsonMappingException);
        } catch (IOException ioException) {
            Log.e(LOG_TAG, "Exception retrieving response.", ioException);
            throw new ApiHelperException(apiUrl, "Couldn't read JSON, IOException when retrieving response.", ioException);
        } catch(Exception ex) {
            Log.e(LOG_TAG, "Exception retrieving response.", ex);
            throw new ApiHelperException(apiUrl, "Couldn't read JSON, IOException when retrieving response.", ex);
        }
        
    }

    /**
     * Public for unit tests
     * 
     * Converts an API path to a full URL with its API key attached 
     */
    public String getFullApiUrl(final String apiPath) {
        final String queryString;
        if (Utils.contains(apiPath, "?")) {
            queryString = String.format("&hapikey=%s", apiKey);
        } else {
            queryString = String.format("?hapikey=%s", apiKey);
        }
        
        if (apiPath.startsWith("/")) {
            return String.format("%s%s%s", baseApiUrl, apiPath, queryString);
        } else {
            return String.format("%s/%s%s", baseApiUrl, apiPath, queryString);
        }
    }
    
    private IHttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    protected void setHttpUtils(IHttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }
}
