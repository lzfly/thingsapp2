package com.smartdevice.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.Params;

import android.util.Log;

public class UserControl {

	public UserControl() {
		// TODO Auto-generated constructor stub
	}
	
	public int addUser(String username, String password) throws JSONException, UnsupportedEncodingException {
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.user_uri;
		HttpPost httpPost = new HttpPost(uri);
		LogUtil.i("UserControl", "user add uri " +uri); 
		JSONObject userJson = new JSONObject();
		userJson.put("username", username);
		userJson.put("password", password);
		userJson.put("phone", "");
		userJson.put("mail", "");
		userJson.put("state", 1);
		StringEntity entity = new StringEntity(userJson.toString(), HTTP.UTF_8);
		LogUtil.i("UserControl", "Add user info " + userJson.toString());  
		httpPost.setEntity(entity);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			Log.i("UserControl", "Add user response status code " + response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
                result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                Log.i("UserControl", "Add user response result " + result);
                JSONObject object = new JSONObject(result);
                return object.getInt("status");
			}else {
				/*Do something here*/
				Log.i("UserControl", "Add user fail");
				return -1;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
}
