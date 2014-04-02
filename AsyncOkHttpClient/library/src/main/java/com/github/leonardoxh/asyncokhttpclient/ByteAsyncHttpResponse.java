/*   
 * Copyright 2014 Leonardo Rossetto
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;

import com.github.leonardoxh.asyncokhttpclient.utils.Util;

import android.os.Message;

/**
 * Handle the HttpURLConnection response 
 * and parse it on a valid byte[] response
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 * @see #onSuccess(int, byte[])
 * @see #onError(java.lang.Throwable, byte[])
 */
public class ByteAsyncHttpResponse extends AsyncHttpResponse {
	
	/** Indicate the response as a valid byte array message */
	protected static final int SUCCESS_BYTE_ARRAY = 7;
	
	/** Indicate the response as a fail byte array message */
	protected static final int FAIL_BYTE_ARRAY = 8;
	
	/**
	 * Callback that indicate the response as finished with success
	 * @param statusCode the status code of this request
	 * @param content the content of this request
	 */
	public void onSuccess(int statusCode, byte[] content) { }
	
	/**
	 * Callback that indicate the response as finished with a fail
	 * but have a valid byte array response
	 * @param error the stack trace of the error message
	 * @param content the content of this request
	 */
	public void onError(Throwable error, byte[] content) { }
	
	/**
	 * Send a success byte array message to handler
	 * @param statusCode the status code of the request
	 * @param responseBody the response body of the request or null
	 */
	protected void sendSuccessMessage(int statusCode, byte[] responseBody) {
		sendMessage(obtainMessage(SUCCESS_BYTE_ARRAY, new Object[] {statusCode, responseBody}));
	}
	
	/**
	 * Send a fail message to the handler
	 * @param error the related stack trace of the request
	 * @param responseBody the response body of the request or null
	 */
	protected void sendFailMessage(Throwable error, byte[] responseBody) {
		sendMessage(obtainMessage(FAIL_BYTE_ARRAY, new Object[] {error, responseBody}));
	}
	
	/**
	 * Handle the success message and call the callback
	 * @param statusCode the status code of the request
	 * @param responseBody the response body or null
	 * @see #onSuccess(int, byte[])
	 */
	protected void handleSuccessByteArrayMessage(int statusCode, byte[] responseBody) {
		onSuccess(statusCode, responseBody);
	}
	
	/**
	 * Handle the error message and call the callback
	 * @param error the stack trace of this response
	 * @param responseBody the response body of this request or null
	 * @see #onError(java.lang.Throwable, byte[])
	 */
	protected void handleFailByteArrayMessage(Throwable error, byte[] responseBody) {
		onError(error, responseBody);
	}

	@Override
	public boolean handleMessage(Message message) {
		switch(message.what) {
			case SUCCESS_BYTE_ARRAY:
				Object[] successResponse = (Object[])message.obj;
				handleSuccessByteArrayMessage(((Integer) successResponse[0]).intValue(), 
						(byte[])successResponse[1]);
				return true;
			case FAIL_BYTE_ARRAY:
				Object[] failResponse = (Object[])message.obj;
				handleSuccessByteArrayMessage(((Integer) failResponse[0]).intValue(), 
						(byte[])failResponse[1]);
				return true;
			default:
				return super.handleMessage(message);
		}
	}

	@Override
	void sendResponseMessage(HttpURLConnection connection) {
		InputStream response = null;
		byte[] responseBody = null;
		try {
			int statusCode = connection.getResponseCode();
			if(statusCode >= 300) {
				response = connection.getErrorStream();
				if(response != null) responseBody = Util.inputStreamToByteArray(response);
				sendFailMessage(new HttpRetryException(connection.getResponseMessage(), statusCode), 
						responseBody);		
			} else {
				response = connection.getInputStream();
				if(response != null) responseBody = Util.inputStreamToByteArray(response);
				sendSuccessMessage(statusCode, responseBody);
			}
		} catch(IOException e) {
			sendFailMessage(e, (byte[])null);
		} finally {
			if(response != null) Util.closeQuietly(response);
		}
	}
	
}