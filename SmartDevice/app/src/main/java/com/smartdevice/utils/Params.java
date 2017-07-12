package com.smartdevice.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Params {

	public static String ip;
	public static String port;
	public static String base_uri;
	public static String session_uri;
	public static String user_uri;
	public static String bind_gateway;
	public static String get_devices;
	public static String put_device_attr;
	public static String group_uri;
	public static String groupbinddevice_uri;
	public static String room_uri;
	public static String roombinddevice_uri;
	public static String logic_uri;
	static{
		Properties properties = new Properties();
		//File file = new File(ParamsUtils.PROPERTIES_FILE_PATH);
	
		try {
			//FileInputStream inputStream = new FileInputStream(file);
			//InputStream stream = new BufferedInputStream(inputStream);
			InputStream stream = Params.class.getResourceAsStream(ParamsUtils.PROPERTIES_FILE_PATH);
		    properties.load(stream);
		    base_uri = properties.getProperty("base_uri", "0.0.0.0");
		    session_uri = properties.getProperty("session_uri", "sessions");
		    user_uri = properties.getProperty("user_uri", "users");
		    bind_gateway = properties.getProperty("bind_gateway", "userbindgateways");
		    get_devices = properties.getProperty("get_devices", "devices");
		    put_device_attr = properties.getProperty("put_device_attr", "deviceattrinfos");
		    group_uri = properties.getProperty("group_uri", "devicegroups");
		    groupbinddevice_uri = properties.getProperty("groupbinddevice_uri", "devicegroupbinddevices");
		    room_uri = properties.getProperty("room_uri", "rooms");
		    roombinddevice_uri = properties.getProperty("roombinddevice_uri", "roombinddevices");
		    logic_uri = properties.getProperty("logic_uri", "logicentitys");
		    
		    stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
