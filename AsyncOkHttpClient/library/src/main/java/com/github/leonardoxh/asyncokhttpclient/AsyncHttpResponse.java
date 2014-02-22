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
import java.net.HttpRetryException;
import java.net.HttpURLConnection;

import com.opens.asyncokhttpclient.utils.Util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Retrive the response from the HttpURLConnection
 * and send it to the relative callback like onSuccess, onError
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 * @see #onSuccess(int, String)
 * @see #onError(Throwable, String)
 * @see #onStart()
 * @see #onFinish()
 */
public class AsyncHttpResponse implements Handler.Callback {

    /** Indicate the response has finished with success */
	protected static final int SUCCESS = 0;

    /** Indicate the response has failed */
	protected static final int FAIL = 1;

    /** Indicate the response has started */
	protected static final int START = 2;

    /** Indicate the response has finished (after onSuccess or onError) */
	protected static final int FINISH = 3;

    /** Handler used to pass the messages over the threads */
	private Handler mHandler;

    /** Construct a new instance of AsyncHttpResponse */
	public AsyncHttpResponse() {
		if(Looper.myLooper() != null) mHandler = new Handler(this);
	}

    /** Callback that indicate the request has started */
	public void onStart() { }

    /** Callback that indicate the request has finished */
	public void onFinish() { }

    /**
     * Callback that indicate the request has
     * this method returns the content of the response
     * @param statusCode the request status code normally 200 or 202
     * @param content the request response body
     */
	public void onSuccess(int statusCode, String content) { }

    /**
     * Callback that indicate the request has return error
     * @param error a Exception class that indicate the error
     * @param content the page error content if available or null
     * @see java.net.HttpRetryException
     */
	public void onError(Throwable error, String content) { }

    /**
     * Send the success message to the handler
     * @param statusCode the success request status code (normally 200 or 202)
     * @param responseBody the response body of the request (if any) or null
     */
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		sendMessage(obtainMessage(SUCCESS, new Object[] {Integer.valueOf(statusCode), responseBody}));
	}

    /**
     * Send the fail message to the handler
     * @param error the detail exception error
     * @param content the request response body (if any) or null
     */
	protected void sendFailMessage(Throwable error, String content) {
		sendMessage(obtainMessage(FAIL, new Object[] {error, content}));
	}

    /**
     * Send the handle message that indicate the request has started
     */
	protected void sendStartMessage() {
		sendMessage(obtainMessage(START, null));
	}

    /**
     * Send the handle message that indicate the request has finished
     */
	protected void sendEndMessage() {
		sendMessage(obtainMessage(FINISH, null));
	}

    /**
     * Handle the success message and call the relative callback
     * @param statusCode the request response status code
     * @param responseBody the request response body (if any) or null
     */
	protected void handleSuccessMessage(int statusCode, String responseBody) {
		onSuccess(statusCode, responseBody);
	}

    /**
     * Handle the fail message and call the relative callback
     * @param error the detail exception
     * @param responseBody the response body of the request (if any) or null
     * @see #onError(Throwable, String)
     */
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

    /**
     * Send a message over the handler,
     * if the handler is null no problems it will recreate it
     * @param message the message for send, can't be null
     */
	protected void sendMessage(Message message) {
		if(mHandler != null) {
			mHandler.sendMessage(message);
		} else {
			handleMessage(message);
		}
	}

    /**
     * Obtain a handler thread message to verify the results
     * this method will always return a valid message
     * @param responseMessage the response message identifier
     * @param response the response object to describle this message
     * @return a valid thread message based on the given parameters
     */
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

    /**
     * Perform the connection with the given client
     * and return the response to the relative callback
     * @param connection the connection to execute and collect the informations
     */
	void sendResponseMessage(HttpURLConnection connection) {
		String responseBody = null;
		InputStream response = null;
		try {
			int responseCode = connection.getResponseCode();
			if(responseCode >= 300) {
				response = connection.getErrorStream();
				if(response != null) responseBody = Util.inputStreamToString(response);
                sendFailMessage(new HttpRetryException(connection.getResponseMessage(),
                        responseCode), responseBody);
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
