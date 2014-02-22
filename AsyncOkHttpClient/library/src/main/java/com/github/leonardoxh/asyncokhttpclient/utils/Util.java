/*
 * Copyright 2013-2014 Leonardo Rossetto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opens.asyncokhttpclient.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.opens.asyncokhttpclient.RequestParams;

/**
 * Utilitaries class, contains the common methods used
 * to decode responses and another utils methods
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 */
public final class Util {

    /** No instances */
	private Util() { }

    /**
     * Convert the given InputStream to a String
     * @param source the InputStream to convert
     * @return the String converted based on given InputStream
     * @throws Exception If the given InputStream is null or can't be accessed
     */
	public static String inputStreamToString(InputStream source) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(source));
	    StringBuilder out = new StringBuilder();
	    String line;
	    while((line = reader.readLine()) != null) {
	        out.append(line);
	    }
	    return out.toString();
	}

    /**
     * Util for GET request method, convert the given RequestParams
     * into a valid URL String query
     * @param url the base URL to concat the query
     * @param requestParams the query parameters
     * @return a valid String URL query for GET methods
     * @see com.opens.asyncokhttpclient.utils.RequestMethod
     * @see com.opens.asyncokhttpclient.RequestParams
     */
	public static String getUrlWithQueryString(String url, RequestParams requestParams) {
		if(requestParams != null) {
			String paramUrl = requestParams.toString();
			if(url.indexOf('?') == -1) {
				url += "?"+paramUrl;
			} else {
				url += "&"+paramUrl;
			}
		}
		return url;
	}

    /**
     * Close any Closeable quietly if any exception
     * is thrown simple ignore it
     * @param closeable a class that implements Closeable to close
     */
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch(IOException ignored) { }
	}
	
}
