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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public class AsyncHttpRequest implements Runnable {
	
	private final AsyncHttpResponse mResponse;
	private final HttpURLConnection mClient;
	private final RequestParams mRequestParams;
	private final RequestModel mRequest;
	
	public AsyncHttpRequest(HttpURLConnection client, AsyncHttpResponse responseHandler, 
			RequestParams params, RequestModel request) {
		mResponse = responseHandler;
		mClient = client;
		mRequest = request;
		mRequestParams = params;
	}
	
	@Override
	public void run() {
		if(mResponse == null) throw new NullPointerException("response == null");
		try {
			mResponse.sendStartMessage();
			makeRequest();
			mResponse.sendEndMessage();
		} catch(Exception e) {
			mResponse.sendFailMessage(e, null);
			mResponse.sendEndMessage();
		}
	}
	
	private void makeRequest() throws Exception {
		if(!Thread.currentThread().isInterrupted()) {
			try {
				if(!Thread.currentThread().isInterrupted()) {
					if(mResponse != null) { //TODO - Possible resource leak, close the inputstream
						mClient.setRequestMethod(mRequest.getRequestMethod());
						for(Map.Entry<String, String> entry : mRequest.getHeaders().entrySet()) {
							mClient.setRequestProperty(entry.getKey(), entry.getValue());
						}
						if(mRequestParams != null && !mRequest.getRequestMethod().equals("GET")) {
							OutputStream params = mClient.getOutputStream();
							params.write(mRequestParams.getParams().getBytes());
							params.close();
						}
						this.mResponse.sendResponseMessage(mClient);
					}
				}
			} catch(Exception e) {
				if(!Thread.currentThread().isInterrupted()) throw e;
			}
		}
	}
	
}
