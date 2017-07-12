package com.smartdevice.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonUtil {

	private final static String TAG = JsonUtil.class.getSimpleName();
	
	public JsonUtil(){
		
	}
	
	/**
	 * @param boolean isArray if jsonData is a array json, set isArray to true,
	 * otherwise false
	 * 
	 **/
	public static List<Object> parseJsonArray(boolean isArray, String jsonData, String name){
		List<Object> data = new ArrayList<>();
		JSONObject jsonObject;
		JSONArray jsonArray = null;
		try {
			if(isArray){
				jsonArray = new JSONArray(jsonData);
			}else {
				jsonObject = new JSONObject(jsonData);
				if(name != null){
					jsonArray = jsonObject.getJSONArray(name);
				}
			}
			Log.i(TAG, "Json array len " + jsonArray.length());
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					data.add(jsonArray.get(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static JSONObject jsonArrayToJSONObject(String data){
		JSONArray jsonArray;
		JSONObject jsonObject = null;
		try {
			jsonArray = new JSONArray(data);
			jsonObject = (JSONObject) jsonArray.get(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	public static List<Object> parseJsonObject(String jsonData, String name) {
		List<Object> data = new ArrayList<>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonData);
			JSONArray nameArray = jsonObject.names();
			Log.i("JsonUtil", "names " + nameArray.toString());
			
			for (int i = 0; i < jsonObject.getJSONArray(name).length(); i++) {
				data.add(jsonObject.getJSONArray(name).get(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public static int getJsonArrayLength(String jsonData){
		int len = 0;
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jsonArray != null){
			len = jsonArray.length();
		}
		return len;
	}
	
	public static int getJsonObjectLength(String jsonData){
		int len = 0;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jsonObject != null){
			len = jsonObject.length();
		}
		return len;
	}
}
