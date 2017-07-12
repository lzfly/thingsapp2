package com.smartdevice.control;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

public class HttpCommand {

	public final static int GET_CMD       = 0x0010;
	public final static int POST_CMD      = 0x0020;
	public final static int PUT_CMD       = 0x0040;
	public final static int DELETE_CMD    = 0x0080;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpPut httpPut;
	private HttpDelete httpDelete;
	
	public HttpCommand(String uri){
		httpGet = new HttpGet(uri);
		httpPost = new HttpPost(uri);
		httpPut = new HttpPut(uri);
		httpDelete = new HttpDelete(uri);
	}
	
	public HttpCommand(int cmdType, String uri){
		switch (cmdType) {
		case GET_CMD:
			httpGet = new HttpGet(uri);
			break;
		case POST_CMD:
			httpPost = new HttpPost(uri);
			break;
		case PUT_CMD:
			httpPut = new HttpPut(uri);
			break;
		case DELETE_CMD:
			httpDelete = new HttpDelete(uri);
			break;
		default:
			break;
		}
	}

	public HttpGet getHttpGet() {
		return httpGet;
	}

	public HttpPost getHttpPost() {
		return httpPost;
	}

	public HttpPut getHttpPut() {
		return httpPut;
	}

	public HttpDelete getHttpDelete() {
		return httpDelete;
	}
	
}
