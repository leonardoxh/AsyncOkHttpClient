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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.github.leonardoxh.asyncokhttpclient.utils.RequestMethod;
import com.github.leonardoxh.asyncokhttpclient.utils.Util;
import com.squareup.okhttp.OkHttpClient;

/**
 * Main class should be used to controll all the client
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 */
public class AsyncOkHttpClient {

	/** The default read time out in seconds */
	private static final long DEFAULT_READ_TIMEOUT = 20;
	
	/** The default connect timeout in seconds */
	private static final long DEFAULT_CONNECT_TIMEOUT = 15;
	
    /** The executor that will execute the request on a separated thread*/
	private ThreadPoolExecutor mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /** The request model for internal usage will make the headers more easy to set */
	private final RequestModel mRequest = new RequestModel();

    /** The main actor of this library tanks to Square Inc. */
	private final OkHttpClient mClient = new OkHttpClient();
	
	public AsyncOkHttpClient() {
		mClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
		mClient.setReadTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
	}

    /**
     * Return a instance used for the requests on this client
     * @return a valid instance of the OkHttp used for the requests on this client
     * @see com.squareup.okhttp.OkHttpClient
     */
	public OkHttpClient getOkHttpClient() {
		return mClient;
	}

    /**
     * Add a header on the request,
     * please note a null header key of a null header value
     * will quietly be ignored from the final headers
     * ensure that passe parameter is not null
     * @param key the header name
     * @param value the header value
     */
	public void addHeader(String key, String value) {
		mRequest.addHeader(key, value);
	}

    /**
     * Set the executor to execute the requests
     * on the most use cases this is not necessary
     * but is a good choice for make unit tests
     * @param threadPool a new thread pool for execute the incoming requests
     */
	public void setThreadPool(ThreadPoolExecutor threadPool) {
		mThreadPool = threadPool;
	}

    /**
     * Set the connection timeout for the requests based on the time unit
     * 0 means no timeout
     * @param timeout a new timeout value
     * @param unit the unit for the timeout
     * @see java.util.concurrent.TimeUnit
     */
	public void setConnectionTimeut(long timeout, TimeUnit unit) {
		mClient.setConnectTimeout(timeout, unit);
	}
	
	/**
	 * Set the read timeout for the requests based on the time unit
	 * 0 means no timeout
     * @param timeout a new timeout value
     * @param unit the unit for the timeout
     * @see java.util.concurrent.TimeUnit
	 */
	public void setReadTieout(long timeout, TimeUnit unit) {
		mClient.setReadTimeout(timeout, unit);
	}

    /**
     * Send the specific request for the request response
     * @param client the client for execute the request
     * @param url the url for execute
     * @param response the response handler for this request
     * @param params the request parameters null parameters means no parameters
     * @param requestMethod the request method for requests
     * @see com.github.leonardoxh.asyncokhttpclient.AsyncHttpRequest
     * @see com.github.leonardoxh.asyncokhttpclient.utils.RequestMethod
     */
	protected void sendRequest(OkHttpClient client, String url, AsyncHttpResponse response, 
			RequestParams params, String requestMethod) {
		try {
			mRequest.setRequestMethod(requestMethod);
			mRequest.setUrl(new URL(url));
			mThreadPool.submit(new AsyncHttpRequest(client, response, params, mRequest));
		} catch(MalformedURLException e) {
			e.printStackTrace(); //TODO
		}
	}

    /**
     * Execute a GET request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void get(String url, AsyncHttpResponse response) {
		get(url, null, response);
	}

    /**
     * Execute a GET request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void get(String url, RequestParams params, AsyncHttpResponse response) {
		sendRequest(mClient, Util.getUrlWithQueryString(url, params), 
				response, params, RequestMethod.GET);
	}

    /**
     * Execute a POST request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void post(String url, AsyncHttpResponse response) {
		post(url, null, response);
	}

    /**
     * Execute a POST request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void post(String url, RequestParams params, AsyncHttpResponse response) {
		sendRequest(mClient, url, response, params,
				RequestMethod.POST);
	}

    /**
     * Execute a PUT request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void put(String url, RequestParams params, AsyncHttpResponse response) {
		sendRequest(mClient, url, response, params,
				RequestMethod.PUT);
	}

    /**
     * Execute a PUT request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void put(String url, AsyncHttpResponse response) {
		put(url, null, response);
	}

    /**
     * Execute a DELETE request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void delete(String url, RequestParams params, AsyncHttpResponse response) {
		sendRequest(mClient, url, response, params,
				RequestMethod.DELETE);
	}

    /**
     * Execute a DELETE request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void delete(String url, AsyncHttpResponse response) {
		delete(url, null, response);
	}
	
}