package com.smartdevice.mode;

import java.io.Serializable;

public class BaseDeviceType implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*meter 表*/
	public final static int BASE_DEVICE_TYPE_METER        = 1001;
	/*lights 灯*/
	public final static int BASE_DEVICE_TYPE_LIGHTS       = 1002;
	/*lights 开关*/
	public final static int BASE_DEVICE_TYPE_SWITCH       = 1003;
	/*lights 插座*/
	//public final static int BASE_DEVICE_TYPE_SOCKET       = 1004;
	/*lights 红外*/
	//public final static int BASE_DEVICE_TYPE_INFRARED     = 1005;
	/*lights 报警器*/
	public final static int BASE_DEVICE_TYPE_ALARM        = 1004; 
	/*lights 传感器*/
	public final static int BASE_DEVICE_TYPE_SENSOR       = 1005;
	
	public final static int BASE_DEVICE_TYPE_CAMERA       = 1006;
	
	/*lights 其他*/
	public final static int BASE_DEVICE_TYPE_OTHERS       = 1007;
	
	public final static int BASE_DEVICE_TYPE_ALL          = 1008;
	
	private int baseTypeId;
	private String baseTypeName;
	
	public int getBaseTypeId() {
		return baseTypeId;
	}
	
	public String getBaseTypeName(){
		return baseTypeName;
	}
	
	public void setBaseTypeId(int id){
		this.baseTypeId = id;
	}
	
	public void setBaseTypeName(String name){
		this.baseTypeName = name;
	}
}
