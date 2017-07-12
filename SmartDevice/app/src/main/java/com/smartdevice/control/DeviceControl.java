package com.smartdevice.control;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAttrType;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.mode.DeviceType;
import com.smartdevice.mode.Session;
import com.smartdevice.service.MqttSend;
import com.smartdevice.utils.Params;
import com.smartdevice.utils.ParamsUtils;

public class DeviceControl extends BaseDeviceControl{

	private HttpControl httpControl;
	
	public DeviceControl(){
		httpControl = new HttpControl();
	}
	
	public List<Device> getAllDevices() throws JSONException{
        List<String> deviceIdList = new ArrayList<>();
        Map<String, String> attrMap = new HashMap<>();
        List<Device> deviceList = new ArrayList<Device>();
        deviceIdList = getAllDevicesId();
        for (int j = 0; j < deviceIdList.size(); j++) {
        	if(!deviceIdList.get(j).equals("zigbee_fbee_12345678")
        			&& !deviceIdList.get(j).equals("zigbee_fbee_d7c61e06004b1200_17")
        			&& !deviceIdList.get(j).equals("zigbee_fbee_6767895")
        			&& !deviceIdList.get(j).equals("zigbee_fbee_edc41e06004b1200_13")){
        	Device device = new Device();
        	
        	/*Get device attribute*/
        	DeviceAttribute deviceAttr = new DeviceAttribute();
        	attrMap = getDeviceAttr(deviceIdList.get(j));
        	
        	device.setDeviceId(deviceIdList.get(j));
    		device.setDeviceName(attrMap.get("devicename"));
    		
    		/*Get device type*/
    		DeviceType deviceType = new DeviceType();
    		String devTypeId = getDeviceType(deviceIdList.get(j));
    		String devTypeName = DeviceType.DevType.getName(devTypeId);
    		deviceType.setTypeId(devTypeId);
    		deviceType.setTypeName(devTypeName);
    		device.setDeviceType(deviceType);
    		
    		for(String key:attrMap.keySet()){
    			if(!key.equals("devicename")){
    				//deviceAttr.setAttrMap(key, attrMap.get(key));
    				deviceAttr.setAttributeMap(key, attrMap.get(key));
    			}
    		}
    		device.setDeviceAttr(deviceAttr);
    		deviceList.add(device);
        	}
		}
  
		return deviceList;
	}
	
	public String getDeviceData(String tag, Map<String, String> paraMap){
		
		return httpControl.ClientHttpRequest(tag, paraMap);
	}
	
	/*Get remote meters' device data*/
	public List<Device> getMeterDeviceList(List<Device> allDeviceList) throws JSONException{
		List<Device> meterDeviceList = new ArrayList<>();
		//List<Device> allDeviceList  = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_WATER_METER) 
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_ELETRIC_METER)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_GAS_METER)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_HEAT_METER)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_OTHER_ENV_METER)){
				meterDeviceList.add(allDeviceList.get(i));
			}
		}
		return meterDeviceList;
	}
	
	/*Get lights device list*/
	public List<Device> getLightDeviceList(List<Device> allDeviceList) throws JSONException{
		//List<Device> allDeviceList  = new ArrayList<>();
		List<Device> lightDeviceList = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_NORMAL_LIGHT) 
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_ADJUST_BRG_LIGHT)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_COLOR_TEMPERATURE_LIGHT)){
				lightDeviceList.add(allDeviceList.get(i));
			}
		}
		return lightDeviceList;
	}
	
	/*Get switch device list*/
	public List<Device> getSwitchDeviceList(List<Device> allDeviceList) throws JSONException{
		//List<Device> allDeviceList  = new ArrayList<>();
		List<Device> swithDeviceList = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_SWITCH) 
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_SWITCH_SOCKET)){
				swithDeviceList.add(allDeviceList.get(i));
			}
		}
		return swithDeviceList;
	}
	
	/*Get sensor device list*/
	public List<Device> getSensorDeviceList(List<Device> allDeviceList) throws JSONException{
		//List<Device> allDeviceList  = new ArrayList<>();
		List<Device> sensorDeviceList = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_PM_SENSOR) 
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_FORMALDEHYDE_SENSOR)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_BODY_SENSOR)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_BODY_INFARE)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_TEMPERATURE_HUMIDITY_SENSOR)){
				sensorDeviceList.add(allDeviceList.get(i));
			}
		}
		return sensorDeviceList;
	}
	
	/*Get alarm device list*/
	public List<Device> getAlarmDeviceList(List<Device> allDeviceList) throws JSONException{
		//List<Device> allDeviceList  = new ArrayList<>();
		List<Device> alarmDeviceList = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_COMBUSTIBLE_GAS_ALARM) 
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_SMOKE_ALARM)){
				alarmDeviceList.add(allDeviceList.get(i));
			}
		}
		return alarmDeviceList;
	}
	
	/*Get camera devices list*/
	public List<Device> getCameraDeviceList(List<Device> allDeviceList) throws JSONException{
		//List<Device> allDeviceList  = new ArrayList<>();
		List<Device> cameraDeviceList = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_CAMERA)){
				cameraDeviceList.add(allDeviceList.get(i));
			}
		}
		return cameraDeviceList;
	}
	
	
	/*Get other devices list*/
	public List<Device> getOtherDeviceList(List<Device> allDeviceList) throws JSONException{
		//List<Device> allDeviceList  = new ArrayList<>();
		List<Device> otherDeviceList = new ArrayList<>();
		try {
			if(allDeviceList == null || allDeviceList.size() == 0){
				allDeviceList = getAllDevices();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < allDeviceList.size(); i++) {
			String devTypeId = allDeviceList.get(i).getDeviceType().getTypeId();
			//String devTypeId = getDeviceType(allDeviceList.get(i).getDeviceId());
			if(devTypeId.equals(DeviceType.DEVICE_TYPE_CURTAIN) 
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_OTHERS)
					|| devTypeId.equals(DeviceType.DEVICE_TYPE_MAGNET)){
				otherDeviceList.add(allDeviceList.get(i));
			}
		}
		return otherDeviceList;
	}
	
	
	public List<Device> getDevices(String gatewaySN){
		List<Device> deviceList;
		HttpControl httpControl = new HttpControl();
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.get_devices;
		uri = uri + "?query=gateway_sn:eq:" + gatewaySN;
		uri = uri + "&limit=20";
		HttpGet httpGet = new HttpGet(uri);
		Log.i("DeviceControl", "uri " + uri);
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
						JSONArray deviceArray = object.getJSONArray("devices");
						if(deviceArray.length() != 0){
							deviceList = new ArrayList<Device>();
							for (int i = 0; i < deviceArray.length(); i++) {
								JSONObject deviceObject = deviceArray.getJSONObject(i);
								Device device = setDeviceInfo(deviceObject);
								device.setGatewaySN(gatewaySN);
								deviceList.add(device);
							}
							
							return deviceList;
						}else {
							
						}
					}else {
						//...
						Log.i("DeviceControl", "Get device info fail.");
						return null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
                
			}else {
				//....
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
		
		return null;
	}
	
	public Device setDeviceInfo(JSONObject deviceInfo) throws JSONException{
		Device device = new Device();
		device.setDeviceSN(deviceInfo.getString("device_sn"));
		device.setDeviceName(deviceInfo.getString("device_name"));
		device.setId(deviceInfo.getInt("id"));
		DeviceType deviceType = new DeviceType();
		deviceType.setTypeCode(deviceInfo.getInt("type_code"));
		deviceType.setTypeName(deviceInfo.getString("type_name"));
		device.setDeviceType(deviceType);
		device.setDeviceModel(deviceInfo.getString("device_model"));
		device.setDeviceVer(deviceInfo.getString("device_ver"));
		device.setProtocol(deviceInfo.getString("protocol"));
		device.setIsOnline(deviceInfo.getInt("is_online"));
		device.setActivetime(deviceInfo.getString("activetime"));
		/*Set device attribute*/
		JSONArray attrArray = deviceInfo.getJSONArray("deviceattrinfos");
		for (int i = 0; i < attrArray.length(); i++) {
			DeviceAttribute attribute = new DeviceAttribute();
			DeviceAttrType attrType = new DeviceAttrType();
			JSONObject attrObject = attrArray.getJSONObject(i);
			attribute.setId(attrObject.getInt("id"));
			//attrType.setAttrTypeCode(attrObject.getInt("type_code"));
			//attrType.setAttrTypeName(attrObject.getString("type_name"));
			//attribute.setDeviceAttrType(attrType);
			attribute.setAttrCode(attrObject.getInt("attr_code"));
			attribute.setAttrName(attrObject.getString("attr_name"));
			attribute.setIsControl(attrObject.getInt("is_control"));
			attribute.setAttrCurrentValue(attrObject.getString("attr_value_cur"));
			attribute.setAttrControlValue(attrObject.getString("attr_value_ctrl"));
			attribute.setPermission(attrObject.getString("attr_permission"));
			device.setAttrListItem(attribute);
		}
		
		return device;
	}
	
	public String putDeviceAttribute(String deviceSN, int attrCode, String newValue){
		HttpClient httpClient = httpControl.getHttpClient();
		String uri = Params.base_uri + Params.put_device_attr;
		uri = uri + "/" + deviceSN + ":" + String.valueOf(attrCode);
		HttpPut httpPut = new HttpPut(uri);
		Log.i("DeviceControl", "uri " + uri);
		httpPut.setHeader("Cookie", Session.getSessionId());
		JSONObject valueObject = new JSONObject();
		try {
			valueObject.put("attr_value_ctrl", newValue);
			valueObject.put("is_control", 1);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringEntity entity;
		try {
			entity = new StringEntity(valueObject.toString(),  HTTP.UTF_8);
			httpPut.setEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpResponse response = null;
		String result = null;
		try {
			response = httpClient.execute(httpPut);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int statusCode = response.getStatusLine().getStatusCode(); 
		if(statusCode == 200){
			HttpEntity responeEntity = response.getEntity();
			try {
				result = EntityUtils.toString(responeEntity, HTTP.UTF_8);
				Log.i("DeviceControl", "Login response result " + result);
				return result;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else {
			//...
			return null;
		}
		
	}
	
	public String pushDeviceAttribute(String deviceSN, int attrCode, String newValue){
		try {
			MqttSend send = new MqttSend();
			send.Send("/v1/to_device/" + deviceSN + "/" + attrCode, 0, String
					.valueOf(newValue).getBytes());
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block^M
			e.printStackTrace();
			return "-1";
		} catch (MqttException e) {
			// TODO Auto-generated catch block^M
			Log.i("MqttSend", "send error....");
			e.printStackTrace();
			return "-1";
		}

		
		return "0";
	}

}
