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
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Handles the the messages for start/success/error/end of the incomming requests
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 */
public class AsyncHttpRequest implements Runnable {

    /** The callback response */
	private final AsyncHttpResponse mResponse;

    /** Client to execute the request */
	private final HttpURLConnection mClient;

    /** The request parameters for the request */
	private final RequestParams mRequestParams;

    /** The model that contains the request method and request headers */
	private final RequestModel mRequest;

    /**
     * Constructs a new instance of AsyncHttpRequest
     * @param client the client to execute the given request
     * @param responseHandler the callback for fire responses like success and error
     * @param params the request parameters for GET, POST...
     * @param request the model that contains the request method and headers
     */
	public AsyncHttpRequest(OkHttpClient client, AsyncHttpResponse responseHandler, 
			RequestParams params, RequestModel request) {
		mResponse = responseHandler;
		mClient = client.open(request.getURL());
		mRequest = request;
		mRequestParams = params;
	}
	
	/** Run the current request yaaa! */
	@Override
	public void run() {
		if(mResponse == null) throw new NullPointerException("response can't be null");
		try {
			mResponse.sendStartMessage();
			makeRequest();
			mResponse.sendEndMessage();
		} catch(IOException e) {
			mResponse.sendFailMessage(e, null);
			mResponse.sendEndMessage();
		} finally {
			disconnect();
		}
	}
	
	/** Disconnect the current request after send the end message */
	protected void disconnect() {
		mClient.disconnect();
	}

    /**
     * Attach the request parameters and headers to request
     * and sent it to AsyncHttpResponse to execute them
     * @throws IOException If for any reason the RequestParams cannot be write on request body
     * @see com.github.leonardoxh.asyncokhttpclient.AsyncHttpResponse
     * @see com.github.leonardoxh.asyncokhttpclient.RequestParams
     */
	private void makeRequest() throws IOException {
		try {
			if(!Thread.currentThread().isInterrupted() 
					&& mResponse != null) {
				mClient.setRequestMethod(mRequest.getRequestMethod());
				for(Map.Entry<String, String> entry : mRequest.getHeaders().entrySet()) {
					mClient.setRequestProperty(entry.getKey(), entry.getValue());
				}
				if(mRequestParams != null && 
						!RequestMethod.GET.equals(mRequest.getRequestMethod())) {
					OutputStream params = mClient.getOutputStream();
					mRequestParams.writeTo(params);
					params.close();
				}
				mResponse.sendResponseMessage(mClient);
			}
		} catch(IOException e) {
			if(!Thread.currentThread().isInterrupted()) throw e;
		}
	}
	
}
