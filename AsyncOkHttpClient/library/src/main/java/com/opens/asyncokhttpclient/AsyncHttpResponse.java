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

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.client.HttpResponseException;

import com.opens.asyncokhttpclient.utils.Util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class AsyncHttpResponse implements Handler.Callback {
	
	protected static final int SUCCESS = 0;
	protected static final int FAIL = 1;
	protected static final int START = 2;
	protected static final int FINISH = 3;
	
	private Handler mHandler;
	
	public AsyncHttpResponse() {
		if(Looper.myLooper() != null) mHandler = new Handler(this);
	}
	
	public void onStart() { }
	
	public void onFinish() { }
	
	public void onSuccess(int statusCode, String content) { }
	
	public void onError(Throwable error, String content) { }
	
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		sendMessage(obtainMessage(SUCCESS, new Object[] {Integer.valueOf(statusCode), responseBody}));
	}
	
	protected void sendFailMessage(Throwable error, String content) {
		sendMessage(obtainMessage(FAIL, new Object[] {error, content}));
	}
	
	protected void sendStartMessage() {
		sendMessage(obtainMessage(START, null));
	}
	
	protected void sendEndMessage() {
		sendMessage(obtainMessage(FINISH, null));
	}
	
	protected void handleSuccessMessage(int statusCode, String responseBody) {
		onSuccess(statusCode, responseBody);
	}
	
	protected void handleFailMessage(Throwable error, String responseBody) {
		onError(error, responseBody);
	}
	
	@Override
	public boolean handleMessage(Message message) {
		switch(message.what) {
			case START:
				onStart();
				return true;
			case FINISH:
				onFinish();
				return true;
			case SUCCESS:
				Object[] successResponse = (Object[])message.obj;
				handleSuccessMessage(((Integer) successResponse[0]).intValue(), (String) successResponse[1]);
				return true;
			case FAIL:
				Object[] failResponse = (Object[])message.obj;
				handleFailMessage((Throwable)failResponse[0], (String) failResponse[1]);
				return true;
		}
		return false;
	}
	
	protected void sendMessage(Message message) {
		if(mHandler != null) {
			mHandler.sendMessage(message);
		} else {
			handleMessage(message);
		}
	}
	
	protected Message obtainMessage(int responseMessage, Object response) {
		Message message = null;
		if(mHandler != null) {
			message = mHandler.obtainMessage(responseMessage, response);
		} else {
			message = Message.obtain();
			message.what = responseMessage;
			message.obj = response;
		}
		return message;
	}
	
	void sendResponseMessage(HttpURLConnection connection) {
		String responseBody = null;
		InputStream response = null;
		try {
			int responseCode = connection.getResponseCode();
			if(responseCode >= 300) {
				response = connection.getErrorStream();
				if(response != null) responseBody = Util.inputStreamToString(response);
				sendFailMessage(new HttpResponseException(responseCode, 
						connection.getResponseMessage()), responseBody);
			} else {
				response = connection.getInputStream();
				if(response != null) responseBody = Util.inputStreamToString(response);
				sendSuccessMessage(responseCode, responseBody);
			}			
		} catch(Exception e) {
			sendFailMessage(e, null);
		} finally {
			if(response != null) Util.closeQuietly(response);
		}
	}
	
}
