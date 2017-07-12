package com.smartdevice.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.smartdevice.mode.Device;
import com.smartdevice.mode.DeviceAttribute;
import com.smartdevice.utils.JsonUtil;

public class BaseDeviceControl {
    private final static String TAG = BaseDeviceControl.class.getSimpleName();
	private HttpControl httpControl;
	private final static String DEVICES_GET_LIST_TAG          = "getdeviceslist";
	private final static String DEVICES_LIST_TAG_NAME         = "deviceslist";
	private final static String DEVICE_GET_INFO_TAG           = "getdeviceinfo";
	private final static String DEVICE_GET_TYPE_TAG           = "getdevicetype";
	private final static String DEVICE_MANAGER_ATTR_TAG       = "adddevicectrl";
	
	public BaseDeviceControl() {
		// TODO Auto-generated constructor stub
		httpControl = new HttpControl();
	}
	
	public String getGateWayId(){
		return "we26n_78A351097F30";
	}
	
	public List<String> getAllDevicesId() throws JSONException{
		List<String> deviceIdList = new ArrayList<String>();
		List<Object> jsonList = new ArrayList<Object>();
		String response = new String();
        String deviceId = new String();
		/*Get all devices attached to the given gateway*/
		String tag = DEVICES_GET_LIST_TAG;
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("gatewayid", getGateWayId());
		response = httpControl.ClientHttpRequest(tag, paraMap);
		jsonList = JsonUtil.parseJsonObject(response, DEVICES_LIST_TAG_NAME);
		/*Read device info*/
		for (int i = 0; i < jsonList.size(); i++) {
			deviceId = jsonList.get(i).toString();
			deviceIdList.add(deviceId);
		}	
		return deviceIdList;
	}
	
	public Map<String, String> getDeviceAttr(String deviceId) throws JSONException{
		Map<String, String> attrMap = new HashMap<String, String>();
		Map<String, String> paraMap = new HashMap<>();
		int temp = 0;
		String tag = DEVICE_GET_INFO_TAG;
		String response = new String();
		String deviceName = new String();
		paraMap.put("gatewayid", getGateWayId());
		paraMap.put("deviceid", deviceId);
		response = httpControl.ClientHttpRequest(tag, paraMap);
		/*Read device name*/
		
		//JSONObject jsonObject = JsonUtil.jsonArrayToJSONObject(response);
		response = response.substring(response.indexOf("[")+1, response.indexOf("]"));
		//Log.i(TAG, "Response json string " + response);
		JSONObject jsonObject = new JSONObject(response);
		for (int j = 0; j < jsonObject.length(); j++) {
			String name = jsonObject.names().getString(j);
			//Log.i(TAG, "Json object name " + name);
			if(name.equals("devicename")){
				deviceName = jsonObject.getString(name);
				attrMap.put("devicename", deviceName);
			}else {
				temp = name.compareTo("deviceattr");
				if(temp >= 0){
					JSONObject attrJson = jsonObject.getJSONObject(name);
					String attrId = attrJson.getString("attr");
					String value  = attrJson.getString("value");
					attrMap.put(attrId, value);
				}
			}
		}
		
		return attrMap;
	}
	
	public void setDeviceAttr(Device device, String attr, String value){
		DeviceAttribute attribute = device.getDeviceAttr();
		String devId = device.getDeviceId();
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("gatewayid", getGateWayId());
		paraMap.put("deviceid", devId);
		paraMap.put("devicetype", device.getDeviceType().getTypeId());
		paraMap.put("attr", attr);		 
		paraMap.put("data", value);
		httpControl.ClientHttpRequest(DEVICE_MANAGER_ATTR_TAG, paraMap);
	}
		
	public String getDeviceType(String deviceId) throws JSONException{
		String deviceType = new String();
		String response = new String();
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("gatewayid", getGateWayId());
		paraMap.put("deviceid", deviceId);
		response = httpControl.ClientHttpRequest(DEVICE_GET_TYPE_TAG, paraMap);
		response = response.substring(response.indexOf("[")+1, response.indexOf("]"));
		Log.i(TAG, "Response json string " + response);
		JSONObject jsonObject = new JSONObject(response);
		deviceType = jsonObject.get("devicetype").toString();
		return deviceType;
	}
}
