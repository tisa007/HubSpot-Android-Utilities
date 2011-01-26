package com.hubspot.android.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WebUtils {
	private static final DefaultHttpClient httpClient = new DefaultHttpClient();

	private static final String LOG_TAG = "hubspot.utils.WebUtils";

    public static Reader getReaderForUrl(final String location) throws IOException {
		HttpEntity entity = null;
		try {
			HttpGet conn = new HttpGet(location);

			HttpResponse response = httpClient.execute(conn);
			entity = response.getEntity();
			return new BufferedReader(
					new InputStreamReader(entity.getContent()));
		} catch (IOException ioException) {
			Log.w(LOG_TAG, ioException);

			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException ioException2) {
					Log.w(LOG_TAG, ioException2);
				}
			}

			throw ioException; 
		}
	}

    public static String downloadString(final String location) {
		HttpEntity entity = null;
		try {
			HttpGet conn = new HttpGet(location);

			HttpResponse response = httpClient.execute(conn);

			/*
			 * if (conn instanceof HttpsURLConnection) { // AWFUL HACK
			 * ((HttpsURLConnection) conn) .setHostnameVerifier(new
			 * AllowAllHostnameVerifier()); }
			 */

			entity = response.getEntity();
			return convertStreamToString(entity.getContent());
		} catch (IOException ioException) {
			Log.w(LOG_TAG, ioException);
			return null;
		} finally {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException ioException2) {
					Log.w(LOG_TAG, ioException2);
				}
			}
		}
	}

    public static String httpGet(final String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);

			HttpEntity entity = httpResponse.getEntity();
			if (entity == null) {
				return null;
			}

			return convertStreamToString(entity.getContent());
		} catch (ClientProtocolException e) {
			Log.e(LOG_TAG, "ClientProtocolException", e);
			return null;
		} catch (IOException e) {
			Log.e(LOG_TAG, "IOException", e);
			return null;
		}
	}

	/**
	 * Reads an InputStream to its end, and closes the stream
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
    private static String convertStreamToString(final InputStream is)
			throws IOException {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			byte[] buf = new byte[2048];
			int ret = 0;
			while ((ret = is.read(buf)) > 0) {
				os.write(buf, 0, ret);
			}
			is.close();

			return new String(os.toByteArray(), "UTF-8");
		} finally {
			is.close();
		}
	}
}
