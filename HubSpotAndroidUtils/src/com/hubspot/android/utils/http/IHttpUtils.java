package com.hubspot.android.utils.http;

import java.io.Reader;

public interface IHttpUtils {

    /**
     * Retrieve the contents of a url as a Reader.
     * 
     * @param url
     * @return
     * @throws HttpUtilsException
     */
    public abstract Reader getReaderForUrl(final String url) throws HttpUtilsException;

    /**
     * Retrieve the contents of a url as a string.
     * 
     * @param url
     * @return
     * @throws HttpUtilsException
     */
    public abstract String get(final String url) throws HttpUtilsException;

    /**
     * Retrieve the contents of a url as a Reader.
     * 
     * @param url
     * @return
     * @throws HttpUtilsException
     */
    public abstract Reader getReaderForPut(final String url, final String putBody) throws HttpUtilsException;

    /**
     * Retrieve the contents of a response for a PUT request on the given URL with the given body contents.
     * 
     * @param url
     * @param putBody
     * @return
     * @throws HttpUtilsException
     */
    public abstract String put(final String url, final String putBody) throws HttpUtilsException;

    /**
     * Retrieve the contents of a url as a Reader.
     * 
     * @param url
     * @return
     * @throws HttpUtilsException
     */
    public abstract Reader getReaderForPost(final String url, final String postBody) throws HttpUtilsException;

    /**
     * Retrieve the contents of a response for a POST request on the given URL with the given body contents.
     * 
     * @param url
     * @param postBody
     * @return
     * @throws HttpUtilsException
     */
    public abstract String post(final String url, final String postBody) throws HttpUtilsException;

}
