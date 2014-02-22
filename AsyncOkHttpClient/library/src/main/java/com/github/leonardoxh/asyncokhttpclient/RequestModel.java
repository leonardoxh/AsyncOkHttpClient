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
package com.github.leonardoxh.asyncokhttpclient;

import com.github.leonardoxh.asyncokhttpclient.utils.RequestMethod;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent a single request (internal API)
 * this class holds the request method, headers and URL
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 */
class RequestModel {

    /** The request method, GET by default */
	private String mRequestMethod = RequestMethod.GET;

    /** Array that represent the connection headers */
	private final Map<String, String> mHeaders = new HashMap<String, String>();

    /** The request URL */
    private URL mUrl;

    /**
     * Construct a new instance of RequestModel
     * @param url the url to use on this request
     */
	public RequestModel(URL url) {
		mUrl = url;
	}

    /** Construct a new instance of RequestModel */
	public RequestModel() { }

    /**
     * Set the URL for this request
     * @param url a new valid url for this request
     */
	public void setUrl(URL url) {
		mUrl = url;
	}

    /**
     * @return a url of this request
     */
	public URL getURL() {
		return mUrl;
	}

    /**
     * Set a new request method of this request
     * @param requestMethod the new request method for this request
     * @see com.github.leonardoxh.asyncokhttpclient.utils.RequestMethod
     */
	public void setRequestMethod(String requestMethod) {
		mRequestMethod = requestMethod;
	}

    /**
     * Add a header for this request
     * @param key the header name
     * @param value the header value
     */
	public void addHeader(String key, String value) {
		mHeaders.put(key, value);
	}

    /**
     * Add multiple headers to this request
     * @param headers a map that contain all headers on this request, can't be null
     */
    public void addHeaders(Map<String, String> headers) {
        mHeaders.putAll(headers);
    }

    /** Clear the associated headers of this request */
    public void clearHeaders() {
        mHeaders.clear();
    }

    /**
     * @return The headers of this request
     */
	public Map<String, String> getHeaders() {
		return mHeaders;
	}

    /**
     * @return The request method of this request
     */
	public String getRequestMethod() {
		return mRequestMethod;
	}
	
}
