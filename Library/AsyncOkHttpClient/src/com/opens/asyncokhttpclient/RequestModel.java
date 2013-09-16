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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestModel {
	
	private String mRequestMethod;
	private Map<String, String> mHeaders;
	private URL mUrl;
	
	public RequestModel(URL url) {
		this.mHeaders = new HashMap<String, String>();
		this.mRequestMethod = "GET";
		this.mUrl = url;
	}
	
	public RequestModel() {
		this.mHeaders = new HashMap<String, String>();
		this.mRequestMethod = "GET";
	}
	
	public void setUrl(URL url) {
		this.mUrl = url;
	}
	
	public URL getURL() {
		return this.mUrl;
	}
	
	public void setRequestMethod(String requestMethod) {
		this.mRequestMethod = requestMethod;
	}
	
	public void addHeader(String key, String value) {
		this.mHeaders.put(key, value);
	}
	
	public Map<String, String> getHeaders() {
		return this.mHeaders;
	}
	
	public String getRequestMethod() {
		return this.mRequestMethod;
	}
	
}
