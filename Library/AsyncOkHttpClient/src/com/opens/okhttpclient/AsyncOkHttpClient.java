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
package com.opens.okhttpclient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.opens.okhttpclient.utils.Util;
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
	
	public void get(String url, String contentType, AsyncHttpResponse reseponse) {
		this.get(url, contentType, reseponse, null);
	}
	
	public void get(String url, AsyncHttpResponse response) {
		this.get(url, null, response, null);
	}
	
	public void get(String url, String contentType, AsyncHttpResponse response, RequestParams params) {
		this.mRequest.setRequestMethod("GET");
		this.sendRequest(this.mClient, Util.getUrlWithQueryString(url, params), response, contentType, params);
	}
	
}