package com.opens.okhttpclient.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.opens.okhttpclient.RequestParams;

import android.test.AndroidTestCase;

public class RequestParamsTest extends AndroidTestCase {
	
	private static final String SPECTED_PARAMS = "name=Leonardo&pass=test";
	
	public void testParamsCreationWithStrings() throws Exception {
		RequestParams params = new RequestParams();
		params.put("name", "Leonardo");
		params.put("pass", "test");
		Assert.assertTrue(SPECTED_PARAMS.equals(params.toString()));
	}
	
	public void testParamsCreationWithMap() throws Exception {
		RequestParams params = new RequestParams();
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("name", "Leonardo");
		paramsMap.put("pass", "test");
		params.put(paramsMap);
		Assert.assertTrue(SPECTED_PARAMS.equals(params.toString()));
	}
	
	public void testEmptyParams() throws Exception {
		RequestParams params = new RequestParams();
		params.put(null, null);
		Assert.assertTrue("".equals(params.toString()));
	}
	
}