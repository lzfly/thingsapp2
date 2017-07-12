package com.smartdevice.control;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.smartdevice.utils.ParamsUtils;

import android.util.Log;

public class HttpControl {
	
	public final static String URI_SERVER_PATH = "http://123.57.206.2:8001/enngateway/";
	public final static String TAG = HttpControl.class.getSimpleName();
	public final static String HTTP_GET = "http.get";
	public final static String HTTP_PUT = "http.put";
	public final static String HTTP_POST = "http.post";
	public final static String HTTP_DELETE = "http.delete";
	
	public HttpClient httpClient;
	
	public HttpControl() {
		HttpParams httpParams = new BasicHttpParams();
		//HttpConnectionParams.setConnectionTimeout(httpParams, ParamsUtils.CONNECTION_TIMEOUT);
		//HttpConnectionParams.setSoTimeout(httpParams, 10*1000); 
		//HttpConnectionParams.setSocketBufferSize(httpParams, 8192);  
		httpClient = new DefaultHttpClient(httpParams);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,3000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,3000);//数据传输时间
	}
	
	public HttpClient getHttpClient(){
		return httpClient;
	}
	
	public String ClientHttpRequest(String tag, Map<String, String> paraMap){
		String requestURI = URI_SERVER_PATH + tag + "?";
		String result = "";
		int i = 0;
		for(String key:paraMap.keySet()){
			requestURI += key + "=" +paraMap.get(key);
			i++;
			if(i < paraMap.size()){
				requestURI += "&";
			}
		}
		
		//Log.i(TAG, "Requst uri " + requestURI);
		
		HttpGet httpGet = new HttpGet(requestURI);
		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, HTTP.UTF_8);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
}
