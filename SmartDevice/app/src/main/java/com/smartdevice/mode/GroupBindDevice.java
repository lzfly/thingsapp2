package com.smartdevice.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupBindDevice implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String groupName;
	private String deviceSN;
	private String deviceName;
	private int    typeCode;
	private int attrCode;
	private String attrValue;
	private String gatewaySN;
	private String username;



	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String name) {
		this.groupName = name;
	}
	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	
	public int getAttrCode() {
		return attrCode;
	}

	public void setAttrCode(int attrcode) {
		this.attrCode = attrcode;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrvalue) {
		this.attrValue = attrvalue;
	}

	public String getGatewaySN() {
		return gatewaySN;
	}

	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
	}

	public String getUserName() {
		return username;
	}
	public void setUserName(String name) {
		username = name;
	}
	
	
}
