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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestParams {
	
	private final ConcurrentHashMap<String, String> params;
	
	public RequestParams() {
		this.params = new ConcurrentHashMap<String, String>();
	}
	
	public void put(Map<String, String> source) {
		for(Map.Entry<String, String> entry : source.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}
	
	public void put(String key, String value) {
		if(key != null && value != null) {
			this.params.put(key, value);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(ConcurrentHashMap.Entry<String, String> entry : this.params.entrySet()) {
			if(result.length() > 0) result.append("&");
			result.append(entry.getKey());
			result.append("=");
			result.append(entry.getValue());
		}
		return result.toString();
	}
	
}
