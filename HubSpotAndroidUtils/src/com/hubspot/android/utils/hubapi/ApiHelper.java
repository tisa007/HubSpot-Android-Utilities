package com.hubspot.android.utils.hubapi;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;

import com.hubspot.android.utils.Utils;
import com.hubspot.android.utils.http.HttpUtils;
import com.hubspot.android.utils.http.HttpUtilsException;

public class ApiHelper {

    private static final String LOG_TAG = "hubspot.utils.hapi";

    private static final ObjectMapper mapper = new ObjectMapper();
    private HttpUtils httpUtils;
    
    /**
     * Take a HubApi URL, query it, and process the results 
     * @param url
     * @return
     * @throws LeadsException 
     */
    public <T> List<T> readUrlToList(final String apiUrl) throws ApiHelperException {
        if (Utils.isEmpty(apiUrl)) {
            throw new IllegalArgumentException("Must include a non-blank api url to read results.");
        }

        Log.d(LOG_TAG, apiUrl);
        try {
            return readApiJson(apiUrl, getHttpUtils().getReaderForUrl(apiUrl));
        } catch (HttpUtilsException ex) {
            throw new ApiHelperException(apiUrl, null, ex);
        }
    }

    private <T> List<T> readApiJson(final String apiUrl, final Reader jsonReader) throws ApiHelperException {
        List<T> objects = null;
        try {
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objects = mapper.readValue(jsonReader, new TypeReference<List<T>>() {});
        } catch (JsonParseException jsonParseException) {
            Log.e(LOG_TAG, "Exception parsing JSON", jsonParseException);
            throw new ApiHelperException(apiUrl, "Couldn't parse JSON from API response.", jsonParseException);
        } catch (JsonMappingException jsonMappingException) {
            Log.e(LOG_TAG, "Exception parsing JSON", jsonMappingException);
            throw new ApiHelperException(apiUrl, "Couldn't parse JSON from API response.", jsonMappingException);
        } catch (IOException ioException) {
            Log.e(LOG_TAG, "Exception retrieving response.", ioException);
            throw new ApiHelperException(apiUrl, "Couldn't read JSON, IOException when retrieving response.", ioException);
        }

        return objects;
    }

    private HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    protected void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }
}
