package com.smartdevice.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
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
import com.smartdevice.mode.Room;
import com.smartdevice.mode.RoomBindDevice;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.LogUtil;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

public class RoomControl {
	private final static String TAG = RoomControl.class.getSimpleName();
	public int isMaster;
	private String uri;
	private HttpResponse response = null;
	
	public RoomControl(String uri){
		this.uri = uri;
	}
	
	public RoomControl(){
		
	}
	
	
	public String addRoom(String username, String roomName) throws JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.room_uri;
		HttpPost httpPost = new HttpPost(uri);
		 Log.i(TAG, "bind uri " + uri);
		httpPost.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		object.put("username", username);
		object.put("room_name", roomName);

		try {
			StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
			httpPost.setEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			response = httpClient.execute(httpPost);
			
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
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.d(TAG, "Connection fail");
			e.printStackTrace();
			return null;
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	public List<Room> getRooms(String username){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.room_uri;
		uri = uri + "?query=username:eq:" + username;
		Log.i("RoomControl", "getRooms uri " + uri);
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
						JSONArray array = object.getJSONArray("rooms");
						int len = array.length();
						if(len != 0){
							List<Room> roomList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject roomInfo = array.getJSONObject(i);
								Room room = new Room();
								room.setRoomName(roomInfo.getString("room_name"));
								room.setUserName(roomInfo.getString("username"));
								room.setId(roomInfo.getInt("id"));
								roomList.add(room);
							}
							return roomList;
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
				LogUtil.w("RoomControl", "response status code " + statusCode);
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
	
	
	public List<Room> getRoom(String username, String roomName){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.room_uri;
		uri = uri + "?query=username:eq:" + username + ",room_name:eq:" + roomName;
		Log.i("RoomControl", "getRooms uri " + uri);
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
						JSONArray array = object.getJSONArray("rooms");
						int len = array.length();
						if(len != 0){
							List<Room> roomList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject roomInfo = array.getJSONObject(i);
								Room room = new Room();
								room.setRoomName(roomInfo.getString("room_name"));
								room.setUserName(roomInfo.getString("username"));
								room.setId(roomInfo.getInt("id"));
								roomList.add(room);
							}
							return roomList;
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
				LogUtil.w("RoomControl", "response status code " + statusCode);
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
	
	public String DeleteRoom(int roomId) throws JSONException{
		
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.room_uri;
		HttpDelete httpdelete = new HttpDelete(uri+"/"+roomId);
		 Log.i(TAG, "bind uri " + uri+"/"+roomId);
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
	
	public String DeleteRoom(String username, String roomName) throws JSONException{
		String result = "";
		List<Room> roomList = getRoom(username, roomName);
		if(roomList != null && roomList.size() > 0)
			result = DeleteRoom(roomList.get(0).getId());
		return result;
	}
	

	public String addRoomBindDevice(String username, String roomName, String deivceSN, String deviceName, int typeCode) throws JSONException{
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.roombinddevice_uri;
		HttpPost httpPost = new HttpPost(uri);
		 Log.i(TAG, "bind uri " + uri);
		httpPost.setHeader("Cookie", Session.getSessionId());
		JSONObject object = new JSONObject();
		
		object.put("username", username);
		object.put("room_name", roomName);
		object.put("device_sn", deivceSN);
		object.put("device_name", deviceName);
		object.put("type_code", typeCode);



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
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	
	public List<RoomBindDevice> getRoomBindDevices(String username, String roomName){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.roombinddevice_uri;
		uri = uri + "?query=username:eq:" + username + ",room_name:eq:" + roomName;
		Log.i("RoomControl", "getroomBindDevices uri " + uri);
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
							List<RoomBindDevice> roombinddeviceList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject roombinddeviceInfo = array.getJSONObject(i);
								RoomBindDevice roombinddevice = new RoomBindDevice();
								roombinddevice.setRoomName(roombinddeviceInfo.getString("room_name"));
								roombinddevice.setDeviceSN(roombinddeviceInfo.getString("device_sn"));
								roombinddevice.setDeviceName(roombinddeviceInfo.getString("device_name"));
								roombinddevice.setTypeCode(roombinddeviceInfo.getInt("type_code"));
								roombinddevice.setGatewaySN(roombinddeviceInfo.getString("gateway_sn"));
								roombinddevice.setUserName(roombinddeviceInfo.getString("username"));
								roombinddevice.setId(roombinddeviceInfo.getInt("id"));
								roombinddeviceList.add(roombinddevice);
							}
							return roombinddeviceList;
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
				LogUtil.w("RoomControl", "response status code " + statusCode);
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
	
	
	public List<RoomBindDevice> getRoomBindDevice(String username, String roomName, String deviceSN){
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.roombinddevice_uri;
		uri = uri + "?query=username:eq:" + username + ",room_name:eq:" + roomName + ",device_sn:eq:" + deviceSN;
		Log.i("RoomControl", "getroomBindDevices uri " + uri);
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
							List<RoomBindDevice> roombinddeviceList = new ArrayList<>();
							for (int i = 0; i < len; i++) {
								JSONObject roombinddeviceInfo = array.getJSONObject(i);
								RoomBindDevice roombinddevice = new RoomBindDevice();
								roombinddevice.setRoomName(roombinddeviceInfo.getString("room_name"));
								roombinddevice.setDeviceSN(roombinddeviceInfo.getString("device_sn"));
								roombinddevice.setDeviceName(roombinddeviceInfo.getString("device_name"));
								roombinddevice.setTypeCode(roombinddeviceInfo.getInt("type_code"));
								roombinddevice.setGatewaySN(roombinddeviceInfo.getString("gateway_sn"));
								roombinddevice.setUserName(roombinddeviceInfo.getString("username"));
								roombinddevice.setId(roombinddeviceInfo.getInt("id"));
								roombinddeviceList.add(roombinddevice);
							}
							return roombinddeviceList;
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
				LogUtil.w("RoomControl", "response status code " + statusCode);
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
	
	public String DeleteRoomBindDevice(int roomBindId) throws JSONException{
		
		String result = new String();
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.roombinddevice_uri;
		HttpDelete httpdelete = new HttpDelete(uri+"/"+roomBindId);
		 Log.i(TAG, "delete uri " + uri+"/"+roomBindId);
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
	
	public String DeleteRoomBindDevice(String username, String groupName, String deviceSN) throws JSONException{
		
		List<RoomBindDevice> roomBindDeviceList = getRoomBindDevice(username, groupName, deviceSN);
		if(roomBindDeviceList != null && roomBindDeviceList.size() > 0)
			DeleteRoomBindDevice(roomBindDeviceList.get(0).getId());
		return null;
	}
	
}
