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
package com.opens.asyncokhttpclient.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.opens.asyncokhttpclient.RequestParams;

public final class Util {
	
	private Util() { }
	
	public static String inputStreamToString(InputStream source) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(source));
	    StringBuilder out = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        out.append(line);
	    }
	    return out.toString();
	}
	
	public static String getUrlWithQueryString(String url, RequestParams requestParams) {
		if(requestParams != null) {
			String paramUrl = requestParams.toString();
			if(url.indexOf('?') == -1) {
				url += "?"+paramUrl;
			} else {
				url += "&"+paramUrl;
			}
		}
		return url;
	}
	
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch(IOException ignored) { }
	}
	
}
