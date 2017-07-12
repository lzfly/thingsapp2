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
import com.smartdevice.mode.Group;
import com.smartdevice.mode.GroupBindDevice;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

public class GroupControl {
	private final static String TAG = GroupControl.class.getSimpleName();
	public int isMaster;
	private String uri;
	
	public GroupControl(String uri){
		this.uri = uri;
	}
	
	public GroupControl(){
		
	}
	
	
	public String addGroup(String username, String groupName) throws JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.group_uri;
		HttpPost httpPost = new HttpPost(uri);
		 Log.i(TAG, "bind uri " + uri);
		httpPost.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		object.put("username", username);
		object.put("group_name", groupName);

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
                Log.i(TAG, "add response result " + result);
			}else {
				/*Do something here*/
				Log.i(TAG, "add fail, http status code " + response.getStatusLine().getStatusCode());
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
	
	public List<Group> getGroups(String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.group_uri;
		uri = uri + "?query=username:eq:" + username;
		uri = uri + "&limit=20";
		Log.i("GroupControl", "getGroups uri " + uri);
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
						JSONArray array = object.getJSONArray("devicegroups");
						int len = array.length();
						if(len != 0){
							List<Group> groupList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject groupInfo = array.getJSONObject(i);
								Group group = new Group();
								group.setGroupName(groupInfo.getString("group_name"));
								group.setUserName(groupInfo.getString("username"));
								group.setId(groupInfo.getInt("id"));
								groupList.add(group);
							}
							return groupList;
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
				LogUtil.w("GroupControl", "response status code " + statusCode);
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
	
	public List<Group> getGroup(String username, String groupName){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.group_uri;
		uri = uri + "?query=username:eq:" + username + ",group_name:eq:" + groupName;
		Log.i("GroupControl", "getGroups uri " + uri);
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
						JSONArray array = object.getJSONArray("devicegroups");
						int len = array.length();
						if(len != 0){
							List<Group> groupList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject groupInfo = array.getJSONObject(i);
								Group group = new Group();
								group.setGroupName(groupInfo.getString("group_name"));
								group.setUserName(groupInfo.getString("username"));
								group.setId(groupInfo.getInt("id"));
								groupList.add(group);
							}
							return groupList;
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
				LogUtil.w("GroupControl", "response status code " + statusCode);
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
	
	public String DeleteGroup(int groupId) throws JSONException{
		
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.group_uri;
		HttpDelete httpdelete = new HttpDelete(uri+"/"+groupId);
		 Log.i(TAG, "bind uri " + uri+"/"+groupId);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String DeleteGroup(String username, String groupName) throws JSONException{
		
		List<Group> groupList = getGroup(username, groupName);
		if(groupList != null && groupList.size() > 0)
			DeleteGroup(groupList.get(0).getId());
		return null;
	}
	
	
	public String addGroupBindDevice(String username, String groupName, String deivceSN, String deviceName, int typeCode, int attrCode, String attrValue) throws JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.groupbinddevice_uri;
		HttpPost httpPost = new HttpPost(uri);
		 Log.i(TAG, "bind uri " + uri);
		httpPost.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		object.put("username", username);
		object.put("group_name", groupName);
		object.put("device_sn", deivceSN);
		object.put("device_name", deviceName);
		object.put("type_code", typeCode);
		object.put("attr_code", attrCode);
		object.put("attr_value", attrValue);


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
	
	public List<GroupBindDevice> getGroupBindDevices(String username, String groupName){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.groupbinddevice_uri;
		uri = uri + "?query=username:eq:" + username + ",group_name:eq:" + groupName;
		Log.i("GroupControl", "getGroupBindDevices uri " + uri);
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
						JSONArray array = object.getJSONArray("devicegroupbinddevices");
						int len = array.length();
						if(len != 0){
							List<GroupBindDevice> groupbinddeviceList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject groupbinddeviceInfo = array.getJSONObject(i);
								GroupBindDevice groupbinddevice = new GroupBindDevice();
								groupbinddevice.setGroupName(groupbinddeviceInfo.getString("group_name"));
								groupbinddevice.setDeviceSN(groupbinddeviceInfo.getString("device_sn"));
								groupbinddevice.setDeviceName(groupbinddeviceInfo.getString("device_name"));
								groupbinddevice.setTypeCode(groupbinddeviceInfo.getInt("type_code"));
								groupbinddevice.setAttrCode(groupbinddeviceInfo.getInt("attr_code"));
								groupbinddevice.setAttrValue(groupbinddeviceInfo.getString("attr_value"));
								//groupbinddevice.setGatewaySN(groupbinddeviceInfo.getString("gateway_sn"));
								groupbinddevice.setUserName(groupbinddeviceInfo.getString("username"));
								groupbinddevice.setId(groupbinddeviceInfo.getInt("id"));
								groupbinddeviceList.add(groupbinddevice);
							}
							return groupbinddeviceList;
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
				LogUtil.w("GroupControl", "response status code " + statusCode);
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
	
	
	public List<GroupBindDevice> getGroupBindDevice(String username, String groupName, String deviceSN, int attrCode){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.groupbinddevice_uri;
		uri = uri + "?query=username:eq:" + username + ",group_name:eq:" + groupName + ",device_sn:eq:" + deviceSN + ",attr_code:eq:" + attrCode;
		Log.i("GroupControl", "getGroupBindDevices uri " + uri);
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
						JSONArray array = object.getJSONArray("devicegroupbinddevices");
						int len = array.length();
						if(len != 0){
							List<GroupBindDevice> groupbinddeviceList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject groupbinddeviceInfo = array.getJSONObject(i);
								GroupBindDevice groupbinddevice = new GroupBindDevice();
								groupbinddevice.setGroupName(groupbinddeviceInfo.getString("group_name"));
								groupbinddevice.setDeviceSN(groupbinddeviceInfo.getString("device_sn"));
								groupbinddevice.setDeviceName(groupbinddeviceInfo.getString("device_name"));
								groupbinddevice.setTypeCode(groupbinddeviceInfo.getInt("type_code"));
								groupbinddevice.setAttrCode(groupbinddeviceInfo.getInt("attr_code"));
								groupbinddevice.setAttrValue(groupbinddeviceInfo.getString("attr_value"));
								groupbinddevice.setGatewaySN(groupbinddeviceInfo.getString("gateway_sn"));
								groupbinddevice.setUserName(groupbinddeviceInfo.getString("username"));
								groupbinddevice.setId(groupbinddeviceInfo.getInt("id"));
								groupbinddeviceList.add(groupbinddevice);
							}
							return groupbinddeviceList;
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
				LogUtil.w("GroupControl", "response status code " + statusCode);
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
	
	public String DeleteGroupBindDevice(int groupBindId) throws JSONException{
		
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.groupbinddevice_uri;
		HttpDelete httpdelete = new HttpDelete(uri+"/"+groupBindId);
		 Log.i(TAG, "delete uri " + uri+"/"+groupBindId);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String DeleteGroupBindDevice(String username, String groupName, String deviceSN, int attrCode) throws JSONException{
		
		List<GroupBindDevice> groupBindDeviceList = getGroupBindDevice(username, groupName, deviceSN, attrCode);
		if(groupBindDeviceList != null && groupBindDeviceList.size() > 0)
			DeleteGroupBindDevice(groupBindDeviceList.get(0).getId());
		return null;
	}
	
	
}
