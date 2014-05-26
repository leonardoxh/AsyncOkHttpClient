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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Message;

/**
 * This handles retrieve a message from
 * the HttpURLConnection and parse it in a JSON
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 * @see #onSuccess(int, org.json.JSONArray)
 * @see #onSuccess(int, org.json.JSONObject)
 * @see #onError(java.lang.Throwable, org.json.JSONArray)
 * @see #onError(java.lang.Throwable, org.json.JSONObject)
 * @see #onError(java.lang.Throwable, java.lang.String)
 */
public class JsonAsyncHttpResponse extends AsyncHttpResponse {

    /** Indicate the response has a valid JSON and a success message */
	protected static final int SUCCESS_JSON = 5;
	
	/** Indicate the response as a valid JSON and a fail message */
	protected static final int FAIL_JSON = 6;

    /**
     * Callback that indicate request has finished success
     * and its a JSONObject
     * @param statusCode the status code of the request normally 200
     * @param response the JSON response of this request
     */
	public void onSuccess(int statusCode, JSONObject response) { }

    /**
     * Callback that indicate request has finished success
     * and its a JSONArray
     * @param statusCode the status code of the request normally 200
     * @param response the JSON response of this request
     */
	public void onSuccess(int statusCode, JSONArray response) { }
	
	/**
	 * Callback that indicate request has finished with a fail response
	 * but a valid JSON body, this is util when you have a server that return a
	 * valid JSON with the fail detail on the body but the response code >= 300 
	 * @param error the error stack trace of this response
	 * @param response the response of this request
	 * @see #onError(java.lang.Throwable, java.lang.String)
	 */
	public void onError(Throwable error, JSONArray response) { }
	
	/**
	 * Callback that indicate request has finished with a fail response
	 * but a valid JSON body, this is util when you have a server that return a
	 * valid JSON with the fail detail on the body but the response code >= 300 
	 * @param error the error stack trace of this response
	 * @param response the response of this request
	 * @see #onError(java.lang.Throwable, java.lang.String)
	 */
	public void onError(Throwable error, JSONObject response) { }

	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		try {
			Object jsonResponse = parseResponse(responseBody);
			if(jsonResponse == null) {
				sendMessage(obtainMessage(FAIL_JSON, new Object[] {
						new NullPointerException("Response body is null"), responseBody}));
			} else {
				sendMessage(obtainMessage(SUCCESS_JSON, new Object[] {statusCode, jsonResponse}));
			}
		} catch(JSONException e) {
			sendMessage(obtainMessage(FAIL_JSON, new Object[] {e, responseBody}));
		}
	}
	
	@Override
	protected void sendFailMessage(Throwable error, String responseBody) {
		try {
			Object jsonResponse = parseResponse(responseBody);
			if(jsonResponse == null) {
				sendMessage(obtainMessage(FAIL_JSON, new Object[] {error, responseBody}));
			} else {
				sendMessage(obtainMessage(FAIL_JSON, new Object[] {error, jsonResponse}));
			}
		} catch(JSONException e) {
			sendMessage(obtainMessage(FAIL_JSON, new Object[] {e, responseBody}));
		}
	}

    /**
     * Parse the response into a valid JSON
     * @param response the response to parse
     * @return a JSON parsed and ready to use
     * @throws JSONException if the JSON is invalid
     */
	private static Object parseResponse(String response) throws JSONException {
		if(response == null) return null;
		response = response.trim();
		if(response.startsWith("{") || response.startsWith("[")) {
            return new JSONTokener(response).nextValue();
        }
		return null;
	}

	@Override
	public boolean handleMessage(Message message) {
		switch(message.what) {
			case SUCCESS_JSON:
				Object[] successResponse = (Object[]) message.obj;
				handleSuccessJsonMessage(((Integer)successResponse[0]).intValue(), successResponse[1]);
				return true;
			case FAIL_JSON:
				Object[] failResponse = (Object[]) message.obj;
				handleErrorJsonMessage((Throwable)failResponse[0], failResponse[1]);
				return true;
			default:
				return super.handleMessage(message);
		}
	}
	
	/**
	 * Handle the error json message and call the onError callback
	 * @param error the stack trace of error message
	 * @param jsonResponse the json response to retrieve the instance
	 * @see #onError(java.lang.Throwable, org.json.JSONArray)
	 * @see #onError(java.lang.Throwable, org.json.JSONObject)
	 */
	protected void handleErrorJsonMessage(Throwable error, Object jsonResponse) {
		if(jsonResponse instanceof JSONObject) {
			onError(error, (JSONObject)jsonResponse);
		} else if(jsonResponse instanceof JSONArray) {
			onError(error, (JSONArray)jsonResponse);
		} else if(jsonResponse == null) {
			onError(new JSONException("Null response "+error.getLocalizedMessage()), (String)null);
		} else {
			onError(new JSONException("Unexpected type " + jsonResponse.getClass().getName()), (String)null);
		}
	}

    /**
     * Handle the success json message and call the onSuccess callback or the onError
     * @param stautsCode the message status code of this request
     * @param jsonResponse the response parsed ready to get instances
     * @see #onSuccess(int, org.json.JSONObject)
     * @see #onSuccess(int, org.json.JSONArray)
     * @see #onError(java.lang.Throwable, java.lang.String)
     */
	protected void handleSuccessJsonMessage(int stautusCode, Object jsonResponse) {
		if(jsonResponse instanceof JSONObject) {
			onSuccess(stautusCode, (JSONObject)jsonResponse);
		} else if(jsonResponse instanceof JSONArray) {
			onSuccess(stautusCode, (JSONArray) jsonResponse);
		} else if(jsonResponse == null) {
			onError(new JSONException("Null response"), (String)null);
		} else {
			onError(new JSONException("Unexpected type " + jsonResponse.getClass().getName()), (String) null);
		}
	}

}
