package com.smartdevice.mode;

import java.io.Serializable;

import com.smartdevice.mode.DeviceAttrType.AttrType;

public class DeviceType extends BaseDeviceType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEVICE_TYPE_NORMAL_LIGHT           = 1;
	public static final int DEVICE_TYPE_CURTAIN                = 2;

	public static final int DEVICE_TYPE_PM_SENSOR  = 3;
	public static final int DEVICE_TYPE_FORMALDEHYDE_SENSOR  = 4;
	public static final int DEVICE_TYPE_COMBUSTIBLE_GAS_ALARM  =  5;
	public static final int DEVICE_TYPE_SMOKE_ALARM  =  6;
	public static final int DEVICE_TYPE_ELETRIC_METER  =  7;
	public static final int DEVICE_TYPE_WATER_METER  =  8;
	public static final int DEVICE_TYPE_TEMPERATURE_HUMIDITY_SENSOR  = 9; /*温湿度*/
	public static final int DEVICE_TYPE_CAMERA  =  10;
	public static final int DEVICE_TYPE_BODY_SENSOR =  11;
	public static final int DEVICE_TYPE_OTHER_ENV_METER  = 12;
	public static final int DEVICE_TYPE_ADJUST_BRG_LIGHT  =  13;
	public static final int DEVICE_TYPE_OTHERS  =  14;
	/*可调亮度色温灯*/
	public static final int DEVICE_TYPE_COLOR_TEMPERATURE_LIGHT  =  15;
	public static final int DEVICE_TYPE_SWITCH  = 16;
	public static final int DEVICE_TYPE_SWITCH_SOCKET  =  17;
	public static final int DEVICE_TYPE_MAGNET  =  18;
	public static final int DEVICE_TYPE_BODY_INFARE  =  19;
	public static final int DEVICE_TYPE_GAS_METER  =  20;
	public static final int DEVICE_TYPE_HEAT_METER  =  21;
	
	private String typeId;
	private int typeCode;
	private String typeName;
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public int getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}



	public static enum DevType{
		DEVICE_TYPE_NORMAL_LIGHT("普通灯", "0001"),
		DEVICE_TYPE_CURTAIN("窗帘", "0002"),
		DEVICE_TYPE_PM_SENSOR("PM2.5传感器", "0003"),
		DEVICE_TYPE_FORMALDEHYDE_SENSOR("甲醛传感器", "0004"),
		DEVICE_TYPE_COMBUSTIBLE_GAS_ALARM("可燃气体报警器", "0005"),
		DEVICE_TYPE_SMOKE_ALARM("烟雾报警器", "0006"),
		DEVICE_TYPE_ELETRIC_METER("电表", "0007"),
		DEVICE_TYPE_WATER_METER("水表", "0008"),
		DEVICE_TYPE_TEMPERATURE_HUMIDITY_SENSOR("温湿度","0009"),
		DEVICE_TYPE_CAMERA("摄像头", "0010"),
		DEVICE_TYPE_BODY_SENSOR("人体传感器", "0011"),
		DEVICE_TYPE_OTHER_ENV_METER("其他环境表计", "0012"),
		DEVICE_TYPE_ADJUST_BRG_LIGHT("可调亮度灯", "0013"),
		DEVICE_TYPE_OTHERS("其他未知", "0014"),
		DEVICE_TYPE_COLOR_TEMPERATURE_LIGHT("可调亮度色温灯", "0015"),
		DEVICE_TYPE_SWITCH("开关", "0016"),
		DEVICE_TYPE_SWITCH_SOCKET("开关插座", "0017"),
		DEVICE_TYPE_MAGNET("门磁", "0018"),
		DEVICE_TYPE_BODY_INFARE("人体红外", "0019"),
		DEVICE_TYPE_GAS_METER("燃气表", "0020"),
		DEVICE_TYPE_HEAT_METER("热表", "0021");
		
		private String name;
		private String id;
		
		DevType(String name, String id){
			this.name = name;
			this.id = id;
		}
		
		private String getId(){
			return id;
		}
		
		public static String getName(String Id){
			for(DevType devType: DevType.values()){
				if(devType.getId().equals(Id)){
					return devType.name;
				}
			}
			return null;
		}
	}
}
