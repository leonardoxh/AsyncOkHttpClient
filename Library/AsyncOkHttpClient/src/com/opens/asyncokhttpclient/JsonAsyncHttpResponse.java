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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Message;

public class JsonAsyncHttpResponse extends AsyncHttpResponse {
	
	protected static final int SUCCESS_JSON = 5;
	
	public void onSuccess(int statusCode, JSONObject response) { }
	
	public void onSuccess(int statusCode, JSONArray response) { }

	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		try {
			Object jsonResponse = this.parseResponse(responseBody);
			this.sendMessage(this.obtainMessage(SUCCESS_JSON, new Object[] {statusCode, jsonResponse}));
		} catch(JSONException e) {
			this.sendFailMessage(e, responseBody);
		}
	}
	
	private Object parseResponse(String response) throws JSONException {
		response = response.trim();
		if(response.startsWith("{") || response.startsWith("[")) return new JSONTokener(response).nextValue();
		return null;
	}

	@Override
	public boolean handleMessage(Message message) {
		switch(message.what) {
			case SUCCESS_JSON:
				Object[] response = (Object[]) message.obj;
				this.handleSuccessJsonMessage(((Integer)response[0]).intValue(), response[1]);
				return true;
			default:
				return super.handleMessage(message);
		}
	}
	
	protected void handleSuccessJsonMessage(int stautsCode, Object jsonResponse) {
		if(jsonResponse instanceof JSONObject) {
			this.onSuccess(stautsCode, (JSONObject)jsonResponse);
		} else if(jsonResponse instanceof JSONArray) {
			this.onSuccess(stautsCode, (JSONArray) jsonResponse);
		} else {
			this.onError(new JSONException("Unexpected type " + jsonResponse.getClass().getName()), null);
		}
	}

}
