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
package com.opens.asyncokhttpclient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.opens.asyncokhttpclient.utils.RequestMethod;
import com.opens.asyncokhttpclient.utils.Util;
import com.squareup.okhttp.OkHttpClient;

public class AsyncOkHttpClient {
	
	private ThreadPoolExecutor mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	private final RequestModel mRequest = new RequestModel();
	private final OkHttpClient mClient = new OkHttpClient();
	
	public OkHttpClient getOkHttpClient() {
		return mClient;
	}
	
	public void addHeader(String key, String value) {
		mRequest.addHeader(key, value);
	}
	
	public void setThreadPool(ThreadPoolExecutor threadPool) {
		mThreadPool = threadPool;
	}
	
	public void setConnectionTimeut(long timeout, TimeUnit unit) {
		mClient.setConnectTimeout(timeout, unit);
	}
	
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
	
	public void get(String url, String contentType, AsyncHttpResponse response) {
		get(url, contentType, null, response);
	}
	
	public void get(String url, AsyncHttpResponse response) {
		get(url, null, null, response);
	}
	
	public void get(String url, RequestParams params, AsyncHttpResponse response) {
		get(url, null, params, response);
	}
	
	public void get(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.GET);
		sendRequest(mClient, Util.getUrlWithQueryString(url, params), response, contentType, params);
	}
	
	public void post(String url, String contentType, AsyncHttpResponse response) {
		post(url, contentType, null, response);
	}
	
	public void post(String url, AsyncHttpResponse response) {
		post(url, null, null, response);
	}
	
	public void post(String url, RequestParams params, AsyncHttpResponse response) {
		post(url, null, params, response);
	}
	
	public void post(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.POST);
		sendRequest(mClient, url, response, contentType, params);
	}
	
	public void put(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.PUT);
		sendRequest(mClient, url, response, contentType, params);
	}
	
	public void put(String url, AsyncHttpResponse response) {
		put(url, null, null, response);
	}
	
	public void put(String url, String contentType, AsyncHttpResponse response) {
		put(url, contentType, null, response);
	}
	
	public void put(String url, RequestParams params, AsyncHttpResponse response) {
		put(url, null, params, response);
	}
	
	public void delete(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		mRequest.setRequestMethod(RequestMethod.DELETE);
		sendRequest(mClient, url, response, contentType, params);
	}
	
	public void delete(String url, AsyncHttpResponse response) {
		delete(url, null, null, response);
	}
	
	public void delete(String url, String contentType, AsyncHttpResponse response) {
		delete(url, contentType, null, response);
	}
	
	public void delete(String url, RequestParams params, AsyncHttpResponse response) {
		delete(url, null, params, response);
	}
	
}
