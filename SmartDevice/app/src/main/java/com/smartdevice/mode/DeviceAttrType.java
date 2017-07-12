package com.smartdevice.mode;

import java.io.Serializable;

public class DeviceAttrType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String attrTypeName;   /*attr_name*/
	private String attrTypeId; 
	private int attrTypeCode;   /*attr_code*/
	
	public static final int ATTR_TYPE_OPEN = 1001;
	public static final int ATTR_TYPE_EXTENT_OPEN = 1002;
	public static final int ATTR_TYPE_CONCRETRATION = 1003;
	public static final int ATTR_TYPE_VALUE = 1004;
	public static final int ATTR_TYPE_BRIGHTNESS = 1005;
	public static final int ATTR_TYPE_COLOR = 1006;
	public static final int ATTR_TYPE_COLOR_TEMPER = 1007;
	public static final int ATTR_TYPE_TEMPERATURE = 1008;
	public static final int ATTR_TYPE_HUMIDITY = 1009;
	public static final int ATTR_TYPE_CURTAIN_STATE = 1010;
	public static final int ATTR_TYPE_IS_ALARM = 1011;

	
	
	public String getAttrTypeName() {
		return attrTypeName;
	}
	public void setAttrTypeName(String attrTypeName) {
		this.attrTypeName = attrTypeName;
	}
	public String getAttrTypeId() {
		return attrTypeId;
	}
	public void setAttrTypeId(String attrTypeId) {
		this.attrTypeId = attrTypeId;
	}
	
	
	
	public int getAttrTypeCode() {
		return attrTypeCode;
	}
	public void setAttrTypeCode(int attrTypeCode) {
		this.attrTypeCode = attrTypeCode;
	}



	public static enum AttrType {
		ATTR_TYPE_OPEN("开关", 1001),
		ATTR_TYPE_EXTENT_OPEN("开度", 1002),
		ATTR_TYPE_CONCRETRATION("浓度", 1003),
		ATTR_TYPE_VALUE("值", 1004),
		ATTR_TYPE_BRIGHTNESS("亮度", 1005),
		ATTR_TYPE_COLOR("颜色", 1006),
		ATTR_TYPE_COLOR_TEMPER("色温", 1007),
		ATTR_TYPE_TEMPERATURE("温度", 1008),
		ATTR_TYPE_HUMIDITY("湿度", 1009),
		ATTR_TYPE_CURTAIN_STATE("窗帘状态", 1010),
		ATTR_TYPE_IS_ALARM("报警", 1011);
		
		private String attrTypeName;
		private int attrTypeId;
		
		AttrType(String name, int id) {
			this.attrTypeName = name;
			this.attrTypeId = id;
		}
		
		private int getId(){
			return attrTypeId;
		}
		
//		public String getName() {
//			return attrTypeName;
//		}
		
		public static String getName(int Id){
			for(AttrType attrType: AttrType.values()){
				if(attrType.getId() == Id){
					return attrType.attrTypeName;
				}
			}
			return null;
		}
	}

}
