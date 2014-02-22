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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representation class for request parameters
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 * @see com.github.leonardoxh.asyncokhttpclient.utils.RequestMethod
 */
public class RequestParams {

    /** This is the headers */
	private final Map<String, String> mParams = new ConcurrentHashMap<String, String>();

    /**
     * Put multiple parameters on this parameters
     * @param source the mapa with the parameters of this request
     *               note a null parameter here will thrown a NullPointerException
     */
	public void put(Map<String, String> source) {
		for(Map.Entry<String, String> entry : source.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

    /**
     * Put the parameter on this params
     * note if the parameter name is null or
     * parameter value is null it will be siently omited from this parameters
     * @param key the parameter name
     * @param value the parameter value
     */
	public void put(String key, String value) {
		if(key != null && value != null) {
			mParams.put(key, value);
		}
	}

    /** Clear the parameter map */
	public void clear() {
		mParams.clear();
	}

    /**
     * Return the encoded parameter of this request
     * ready for HttpURLConnection set on OutputStream
     * @return the parameters encoded
     */
	public String getParams() {
		StringBuilder result = new StringBuilder();
		for(ConcurrentHashMap.Entry<String, String> entry : mParams.entrySet()) {
			if(result.length() > 0) result.append("&");
			result.append(entry.getKey());
			result.append("=");
			result.append(entry.getValue());
		}
		return result.toString();
	}

    @Override
    /**
     * The encoded parameters of this request
     * @return the encoded parameters of this request
     * @see #getParams()
     */
	public String toString() {
		return getParams();
	}
	
}
