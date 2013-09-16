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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public class AsyncHttpRequest implements Runnable {
	
	private AsyncHttpResponse mResponse;
	private HttpURLConnection mClient;
	private RequestParams mRequestParams;
	private RequestModel mRequest;
	
	public AsyncHttpRequest(HttpURLConnection client, AsyncHttpResponse responseHandler, 
			RequestParams params, RequestModel request) {
		this.mResponse = responseHandler;
		this.mClient = client;
		this.mRequest = request;
		this.mRequestParams = params;
	}
	
	@Override
	public void run() {
		try {
			if(this.mResponse != null) this.mResponse.sendStartMessage();
			this.makeRequest();
			if(this.mResponse != null) this.mResponse.sendEndMessage();
		} catch(Exception e) {
			if(this.mResponse != null) this.mResponse.sendEndMessage();
		}
	}
	
	private void makeRequest() throws Exception {
		if(!Thread.currentThread().isInterrupted()) {
			try {
				if(!Thread.currentThread().isInterrupted()) {
					if(this.mResponse != null) { //TODO - Possible resource leak, close the inputstream
						this.mClient.setRequestMethod(this.mRequest.getRequestMethod());
						for(Map.Entry<String, String> entry : this.mRequest.getHeaders().entrySet()) {
							this.mClient.setRequestProperty(entry.getKey(), entry.getValue());
						}
						if(this.mRequestParams != null && !this.mRequest.getRequestMethod().equals("GET")) {
							OutputStream params = this.mClient.getOutputStream();
							params.write(this.mRequestParams.toString().getBytes());
							params.close();
						}
						this.mResponse.sendResponseMessage(this.mClient);
					}
				}
			} catch(Exception e) {
				if(!Thread.currentThread().isInterrupted()) throw e;
			}
		}
	}
	
}
