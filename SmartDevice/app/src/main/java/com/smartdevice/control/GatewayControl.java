package com.smartdevice.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.smartdevice.mode.Gateway;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

public class GatewayControl {
	private final static String TAG = GatewayControl.class.getSimpleName();
	public int isMaster;
	private String uri;
	
	public GatewayControl(String uri){
		this.uri = uri;
	}
	
	public GatewayControl(){
		
	}
	
	public String bind(String gatewayId, String userId) throws JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		HttpPost httpPost = new HttpPost(uri);
		 Log.i(TAG, "bind uri " + uri);
		httpPost.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		object.put("username", userId);
		object.put("gateway_sn", gatewayId);
		object.put("is_master", 1);
		try {
			StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
			httpPost.setEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			HttpResponse response = httpClient.execute(httpPost);
			
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
                result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                Log.i(TAG, "bind response result " + result);
			}else {
				/*Do something here*/
				Log.i(TAG, "bind fail, http status code " + response.getStatusLine().getStatusCode());
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String unbind(int gatewaybindId) throws JSONException{
		
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		HttpDelete httpdelete = new HttpDelete(uri+"/"+gatewaybindId);
		 Log.i(TAG, "bind uri " + uri+"/"+gatewaybindId);
		 httpdelete.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		try {
			HttpResponse response = httpClient.execute(httpdelete);
			
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
                result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                Log.i(TAG, "bind response result " + result);
			}else {
				/*Do something here*/
				Log.i(TAG, "bind fail, http status code " + response.getStatusLine().getStatusCode());
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String unbind(String gatewaySN, String username) throws JSONException{
		
		List<Gateway> gatewayList = getBindedGateways(gatewaySN, username);
		if(gatewayList != null && gatewayList.size() > 0)
		    unbind(gatewayList.get(0).getId());
		return null;
	}
	
	public List<Gateway> getBindedGateways(String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.bind_gateway;
		uri = uri + "?query=username:eq:" + username;
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Cookie", Session.getSessionId());
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
                String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray array = object.getJSONArray("userbindgateways");
						int len = array.length();
						if(len != 0){
							List<Gateway> bindedGatewayList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject gatewayInfo = array.getJSONObject(i);
								Gateway gateway = new Gateway();
								gateway.setGatewaySN(gatewayInfo.getString("gateway_sn"));
								gateway.setIsMaster(gatewayInfo.getInt("is_master"));
								gateway.setId(gatewayInfo.getInt("id"));
								bindedGatewayList.add(gateway);
							}
							return bindedGatewayList;
						}else {
							return null;
						}
						
					}else {
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					LogUtil.e(TAG, "Json parse fail");
					e.printStackTrace();
					return null;
				}
			}else {
				LogUtil.w("GatewayControl", "response status code " + statusCode);
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
		
		return null;
	}
	
	
	public List<Gateway> getBindedGateways(String gatewaySN, String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.bind_gateway;
		uri = uri + "?query=username:eq:" + username + ",gateway_sn:eq:" + gatewaySN;
		LogUtil.w("GatewayControl", "getBindedGateways uri= " + uri);
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setHeader("Cookie", Session.getSessionId());
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				HttpEntity responeEntity = response.getEntity();
                String result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                try {
					JSONObject object = new JSONObject(result);
					int status = object.getInt("status");
					if(status == ParamsUtils.SUCCESS_CODE){
						JSONArray array = object.getJSONArray("userbindgateways");
						int len = array.length();
						if(len != 0){
							List<Gateway> bindedGatewayList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject gatewayInfo = array.getJSONObject(i);
								Gateway gateway = new Gateway();
								gateway.setGatewaySN(gatewayInfo.getString("gateway_sn"));
								gateway.setIsMaster(gatewayInfo.getInt("is_master"));
								gateway.setId(gatewayInfo.getInt("id"));
								bindedGatewayList.add(gateway);
							}
							return bindedGatewayList;
						}else {
							return null;
						}
						
					}else {
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					LogUtil.e(TAG, "Json parse fail");
					e.printStackTrace();
					return null;
				}
			}else {
				LogUtil.w("GatewayControl", "response status code " + statusCode);
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
		
		return null;
	}	
}
