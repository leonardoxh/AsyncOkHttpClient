/*   
 * Copyright [2013] [Leonardo Rossetto]
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

import com.opens.asyncokhttpclient.utils.Util;
import com.squareup.okhttp.OkHttpClient;

public class AsyncOkHttpClient {
	
	private ThreadPoolExecutor mThreadPool;
	private RequestModel mRequest;
	private OkHttpClient mClient;
	
	public AsyncOkHttpClient() {
		this.mThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		this.mRequest = new RequestModel();
		this.mClient = new OkHttpClient();
	}
	
	public OkHttpClient getOkHttpClient() {
		return this.mClient;
	}
	
	public void addHeader(String key, String value) {
		this.mRequest.addHeader(key, value);
	}
	
	public void setThreadPool(ThreadPoolExecutor threadPool) {
		this.mThreadPool = threadPool;
	}
	
	protected void sendRequest(OkHttpClient client, String url, AsyncHttpResponse response, 
			String contentType, RequestParams params) {
		try {
			this.mRequest.setUrl(new URL(url));
			HttpURLConnection conn = client.open(this.mRequest.getURL());
			if(contentType != null) conn.setRequestProperty("Content-Type", contentType);
			this.mThreadPool.submit(new AsyncHttpRequest(conn, response, params, this.mRequest));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void get(String url, String contentType, AsyncHttpResponse response) {
		this.get(url, contentType, null, response);
	}
	
	public void get(String url, AsyncHttpResponse response) {
		this.get(url, null, null, response);
	}
	
	public void get(String url, RequestParams params, AsyncHttpResponse response) {
		this.get(url, null, params, response);
	}
	
	public void get(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		this.mRequest.setRequestMethod("GET");
		this.sendRequest(this.mClient, Util.getUrlWithQueryString(url, params), response, contentType, params);
	}
	
	public void post(String url, String contentType, AsyncHttpResponse response) {
		this.post(url, contentType, null, response);
	}
	
	public void post(String url, AsyncHttpResponse response) {
		this.post(url, null, null, response);
	}
	
	public void post(String url, RequestParams params, AsyncHttpResponse response) {
		this.post(url, null, params, response);
	}
	
	public void post(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		this.mRequest.setRequestMethod("POST");
		this.sendRequest(this.mClient, url, response, contentType, params);
	}
	
	public void put(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		this.mRequest.setRequestMethod("PUT");
		this.sendRequest(this.mClient, url, response, contentType, params);
	}
	
	public void put(String url, AsyncHttpResponse response) {
		this.put(url, null, null, response);
	}
	
	public void put(String url, String contentType, AsyncHttpResponse response) {
		this.put(url, contentType, null, response);
	}
	
	public void put(String url, RequestParams params, AsyncHttpResponse response) {
		this.put(url, null, params, response);
	}
	
	public void delete(String url, String contentType, RequestParams params, AsyncHttpResponse response) {
		this.mRequest.setRequestMethod("DELETE");
		this.sendRequest(this.mClient, url, response, contentType, params);
	}
	
	public void delete(String url, AsyncHttpResponse response) {
		this.delete(url, null, null, response);
	}
	
	public void delete(String url, String contentType, AsyncHttpResponse response) {
		this.delete(url, contentType, null, response);
	}
	
	public void delete(String url, RequestParams params, AsyncHttpResponse response) {
		this.delete(url, null, params, response);
	}
	
}
