package com.smartdevice.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.utils.ParamsUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class LoginControl {

	private String uri;
	private String username;
	private String password;
	private String sessionid;
	private final static String TAG = LoginControl.class.getSimpleName();
	
	public LoginControl(String uri){
		this.uri = uri;
	}
	
	public String login(String username, String password, String sessionid) throws UnsupportedEncodingException, JSONException{
		this.sessionid = sessionid;
		return login(username, password);
	}
	
	public String login(String username, String password) throws UnsupportedEncodingException, JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpPost httpPost = new HttpPost(uri);
		Log.i(TAG, "Login uri " +uri); 
		if(sessionid != null && sessionid.equals("")){
			httpPost.setHeader("Cookie", sessionid);
		}
		HttpClient httpClient = httpControl.getHttpClient();
		JSONObject userJson = new JSONObject();
		userJson.put("username", username);
		userJson.put("password", password);
		StringEntity entity = new StringEntity(userJson.toString(), HTTP.UTF_8);
		Log.i(TAG, "Login user info " + userJson.toString());  
		httpPost.setEntity(entity);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			Log.i(TAG, "Login response status code " + response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
                result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                Log.i(TAG, "Login response result " + result);
			}else {
				/*Do something here*/
				Log.i(TAG, "Login fail");
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
}
