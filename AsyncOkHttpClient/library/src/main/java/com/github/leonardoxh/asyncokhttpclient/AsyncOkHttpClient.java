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

import java.net.HttpURLConnection;
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

    /** The executor that will execute the request on a separated thread*/
	private ThreadPoolExecutor mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /** The request model for internal usage will make the headers more easy to set */
	private final RequestModel mRequest = new RequestModel();

    /** The main actor of this library tanks to Square Inc. */
	private final OkHttpClient mClient = new OkHttpClient();

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
     * Set the connection time out for the requests based on the time unit
     * @param timeout a new timeout value
     * @param unit the unit for the timeout
     * @see java.util.concurrent.TimeUnit
     */
	public void setConnectionTimeut(long timeout, TimeUnit unit) {
		mClient.setConnectTimeout(timeout, unit);
	}

    /**
     * Send the specific request for the request response
     * @param client the client for execute the request
     * @param url the url for execute
     * @param response the response handler for this request
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param params the request parameters null parameters means no parameters
     * @see com.github.leonardoxh.asyncokhttpclient.AsyncHttpRequest
     */
	protected void sendRequest(OkHttpClient client, String url, AsyncHttpResponse response, 
			String contentType, RequestParams params) {
		try {
			mRequest.setUrl(new URL(url));
			HttpURLConnection conn = client.open(mRequest.getURL());
			if(contentType != null) conn.setRequestProperty("Content-Type", contentType);
			mThreadPool.submit(new AsyncHttpRequest(conn, response, params, mRequest));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Execute a GET request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param response the response handler to manage the results
     */
	public void get(String url, String contentType, AsyncHttpResponse response) {
		get(url, contentType, null, response);
	}

    /**
     * Execute a GET request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void get(String url, AsyncHttpResponse response) {
		get(url, null, null, response);
	}

    /**
     * Execute a GET request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void get(String url, RequestParams params, AsyncHttpResponse response) {
		get(url, null, params, response);
	}

    /**
     * Execute a GET request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void get(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.GET);
		sendRequest(mClient, Util.getUrlWithQueryString(url, params), response, contentType, params);
	}

    /**
     * Execute a POST request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param response the response handler to manage the results
     */
	public void post(String url, String contentType, AsyncHttpResponse response) {
		post(url, contentType, null, response);
	}

    /**
     * Execute a POST request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void post(String url, AsyncHttpResponse response) {
		post(url, null, null, response);
	}

    /**
     * Execute a POST request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void post(String url, RequestParams params, AsyncHttpResponse response) {
		post(url, null, params, response);
	}

    /**
     * Execute a POST request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void post(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.POST);
		sendRequest(mClient, url, response, contentType, params);
	}

    /**
     * Execute a PUT request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void put(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.PUT);
		sendRequest(mClient, url, response, contentType, params);
	}

    /**
     * Execute a PUT request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void put(String url, AsyncHttpResponse response) {
		put(url, null, null, response);
	}

    /**
     * Execute a PUT request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param response the response handler to manage the results
     */
	public void put(String url, String contentType, AsyncHttpResponse response) {
		put(url, contentType, null, response);
	}

    /**
     * Execute a PUT request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void put(String url, RequestParams params, AsyncHttpResponse response) {
		put(url, null, params, response);
	}

    /**
     * Execute a DELETE request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void delete(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.DELETE);
		sendRequest(mClient, url, response, contentType, params);
	}

    /**
     * Execute a DELETE request
     * @param url the url for execute
     * @param response the response handler to manage the results
     */
	public void delete(String url, AsyncHttpResponse response) {
		delete(url, null, null, response);
	}

    /**
     * Execute a DELETE request
     * @param url the url for execute
     * @param contentType the content type for this request if this parameter
     *                    is null a default Content-Type will be used
     * @param response the response handler to manage the results
     */
	public void delete(String url, String contentType, AsyncHttpResponse response) {
		delete(url, contentType, null, response);
	}

    /**
     * Execute a DELETE request
     * @param url the url for execute
     * @param params the request parameters null parameters means no parameters
     * @param response the response handler to manage the results
     */
	public void delete(String url, RequestParams params, AsyncHttpResponse response) {
		delete(url, null, params, response);
	}
	
}
