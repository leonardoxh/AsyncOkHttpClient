package com.github.leonardoxh.asyncokhttpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.github.leonardoxh.asyncokhttpclient.utils.Util;

import android.os.Message;

public class ByteAsyncHttpResponse extends AsyncHttpResponse {
	
	protected static final int SUCCESS_BYTE_ARRAY = 7;
	protected static final int FAIL_BYTE_ARRAY = 8;
	
	public void onSuccess(int statusCode, byte[] content) { }
	
	public void onError(Throwable error, byte[] content) { }
	
	protected void sendSuccessMessage(int statusCode, byte[] responseBody) {
		sendMessage(obtainMessage(SUCCESS_BYTE_ARRAY, new Object[] {statusCode, responseBody}));
	}
	
	protected void sendFailMessage(int statusCode, byte[] responseBody) {
		sendMessage(obtainMessage(FAIL_BYTE_ARRAY, new Object[] {statusCode, responseBody}));
	}
	
	protected void handleSuccessByteArrayMessage(int statusCode, byte[] responseBody) {
		onSuccess(statusCode, responseBody);
	}
	
	protected void handleFailByteArrayMessage(Throwable error, byte[] responseBody) {
		onError(error, responseBody);
	}

	@Override
	public boolean handleMessage(Message message) {
		switch(message.what) {
			case SUCCESS_BYTE_ARRAY:
				Object[] successResponse = (Object[])message.obj;
				handleSuccessByteArrayMessage(((Integer) successResponse[0]).intValue(), 
						(byte[]) successResponse[1]);
				return true;
			case FAIL_BYTE_ARRAY:
				Object[] failResponse = (Object[])message.obj;
				handleSuccessByteArrayMessage(((Integer) failResponse[0]).intValue(), 
						(byte[]) failResponse[1]);
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
				sendSuccessMessage(statusCode, responseBody);
			} else {
				response = connection.getInputStream();
				if(response != null) responseBody = Util.inputStreamToByteArray(response);
				sendFailMessage(statusCode, responseBody);
			}
		} catch(IOException e) {
			sendFailMessage(e, null);
		} finally {
			if(response != null) Util.closeQuietly(response);
		}
	}
	
}