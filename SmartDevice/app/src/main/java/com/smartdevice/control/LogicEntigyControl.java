package com.smartdevice.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.CursorJoiner.Result;
import android.util.Log;

import com.smartdevice.mode.Gateway;
import com.smartdevice.mode.Group;
import com.smartdevice.mode.GroupBindDevice;
import com.smartdevice.mode.LogicEntity;
import com.smartdevice.mode.RoomBindDevice;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

public class LogicEntigyControl {
	private final static String TAG = LogicEntigyControl.class.getSimpleName();
	private String uri;
	
	public LogicEntigyControl(String uri){
		this.uri = uri;
	}
	
	public LogicEntigyControl(){
		
	}
	
	public int addLogicEntity(LogicEntity logic) throws JSONException, UnsupportedEncodingException {
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.logic_uri;
		HttpPost httpPost = new HttpPost(uri);
		LogUtil.i("LogicEntityControl", "logicentity add uri " +uri); 
		JSONObject userJson = new JSONObject();
		userJson.put("username", logic.getUserName());
		userJson.put("gateway_sn", logic.getGatewaySN());
		userJson.put("if_device_sn", logic.getIfDeviceSN());
		userJson.put("if_device_name", logic.getIfDeviceName());
		userJson.put("if_type_code", logic.getIfTypeCode());
		userJson.put("if_attr_code", logic.getIfAttrCode());
		userJson.put("if_operate_code", logic.getIfOperateCode());
		userJson.put("if_attr_value", logic.getIfAttrValue());
		userJson.put("if_attr_value2", logic.getIfAttrValue2());
		userJson.put("th_device_sn", logic.getThDeviceSN());
		userJson.put("th_device_name", logic.getThDeviceName());
		userJson.put("th_type_code", logic.getThTypeCode());
		userJson.put("th_attr_code", logic.getThAttrCode());
		userJson.put("th_attr_value", logic.getThAttrValue());
		
		StringEntity entity = new StringEntity(userJson.toString(), HTTP.UTF_8);
		LogUtil.i("LogicEntityControl", "Add logicentity info " + userJson.toString());  
		httpPost.setEntity(entity);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			Log.i("LogicEntityControl", "Add logicentity response status code " + response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
                result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                Log.i("LogicEntityControl", "Add logicentity response result " + result);
                JSONObject object = new JSONObject(result);
                return object.getInt("status");
			}else {
				/*Do something here*/
				Log.i("LogicEntityControl", "Add logicentity fail");
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
	
	public List<LogicEntity> getLogicEntitys(String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.logic_uri;
		uri = uri + "?query=username:eq:" + username;
		Log.i("LogicEntityControl", "getLogicEntitys uri " + uri);
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
						JSONArray array = object.getJSONArray("logicentitys");
						int len = array.length();
						if(len != 0){
							List<LogicEntity> logicEntityList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject logicEntityInfo = array.getJSONObject(i);

								LogicEntity logicEntity = new LogicEntity();
	
								logicEntity.setIfDeviceSN(logicEntityInfo.getString("if_device_sn"));
								logicEntity.setIfDeviceName(logicEntityInfo.getString("if_device_name"));
								logicEntity.setIfTypeCode(logicEntityInfo.getInt("if_type_code"));
								logicEntity.setIfAttrCode(logicEntityInfo.getInt("if_attr_code"));
								logicEntity.setIfOperateCode(logicEntityInfo.getString("operate_code"));
								logicEntity.setIfAttrValue(logicEntityInfo.getString("if_attr_value"));
								logicEntity.setIfAttrValue2(logicEntityInfo.getString("if_attr_value2"));
								logicEntity.setThDeviceSN(logicEntityInfo.getString("th_device_sn"));
								logicEntity.setThDeviceName(logicEntityInfo.getString("th_device_name"));
								logicEntity.setThTypeCode(logicEntityInfo.getInt("th_type_code"));
								logicEntity.setThAttrCode(logicEntityInfo.getInt("th_attr_code"));
								logicEntity.setThAttrValue(logicEntityInfo.getString("th_attr_value"));
								
								logicEntity.setGatewaySN(logicEntityInfo.getString("gateway_sn"));
								logicEntity.setUserName(logicEntityInfo.getString("username"));
								logicEntity.setId(logicEntityInfo.getInt("id"));
								logicEntityList.add(logicEntity);
							}
							return logicEntityList;
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
				LogUtil.w("LogicEntityControl", "response status code " + statusCode);
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
	
	
	public List<LogicEntity> getLogicEntity(String username, String ifDeviceSN, int ifAttrCode, String thDeviceSN, int thAttrCode){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.logic_uri;
		uri = uri + "?query=username:eq:" + username + ",if_device_sn:eq:" + ifDeviceSN + ",if_attr_code:eq:" + ifAttrCode + ",th_device_sn:eq:" + thDeviceSN + ",th_attr_code:eq:" + thAttrCode;
		Log.i("LogicEntityControl", "getLogicEntity uri " + uri);
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
						JSONArray array = object.getJSONArray("roombinddevices");
						int len = array.length();
						if(len != 0){
							List<LogicEntity> logicEntityList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject logicEntityInfo = array.getJSONObject(i);

								LogicEntity logicEntity = new LogicEntity();
	
								logicEntity.setIfDeviceSN(logicEntityInfo.getString("if_device_sn"));
								logicEntity.setIfDeviceName(logicEntityInfo.getString("if_device_name"));
								logicEntity.setIfTypeCode(logicEntityInfo.getInt("if_type_code"));
								logicEntity.setIfAttrCode(logicEntityInfo.getInt("if_attr_code"));
								logicEntity.setIfOperateCode(logicEntityInfo.getString("if_operate_code"));
								logicEntity.setIfAttrValue(logicEntityInfo.getString("if_attr_value"));
								logicEntity.setIfAttrValue2(logicEntityInfo.getString("if_attr_value2"));
								logicEntity.setThDeviceSN(logicEntityInfo.getString("th_device_sn"));
								logicEntity.setThDeviceName(logicEntityInfo.getString("th_device_name"));
								logicEntity.setThTypeCode(logicEntityInfo.getInt("th_type_code"));
								logicEntity.setThAttrCode(logicEntityInfo.getInt("th_attr_code"));
								logicEntity.setThAttrValue(logicEntityInfo.getString("th_attr_value"));
								
								logicEntity.setGatewaySN(logicEntityInfo.getString("gateway_sn"));
								logicEntity.setUserName(logicEntityInfo.getString("username"));
								logicEntity.setId(logicEntityInfo.getInt("id"));
								logicEntityList.add(logicEntity);
							}
							return logicEntityList;
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
				LogUtil.w("LogicEntityControl", "response status code " + statusCode);
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
	
	public String DeleteLogicEntity(int logicEntityId) throws JSONException{
		
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.logic_uri;
		HttpDelete httpdelete = new HttpDelete(uri+"/"+logicEntityId);
		 Log.i(TAG, "delete uri " + uri+"/"+logicEntityId);
		 httpdelete.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		try {
			HttpResponse response = httpClient.execute(httpdelete);
			
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity responeEntity = response.getEntity();
                result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
                Log.i(TAG, "delete response result " + result);
			}else {
				/*Do something here*/
				Log.i(TAG, "delete fail, http status code " + response.getStatusLine().getStatusCode());
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
	
	public String DeleteLogicEntity(String username, String ifDeviceSN, int ifAttrCode, String thDeviceSN, int thAttrCode) throws JSONException{
		String result = null;
		List<LogicEntity> logicEntityList = getLogicEntity(username, ifDeviceSN, ifAttrCode, thDeviceSN, thAttrCode);
		if(logicEntityList != null && logicEntityList.size() > 0)
			result = DeleteLogicEntity(logicEntityList.get(0).getId());
		return result;
	}
	
}
