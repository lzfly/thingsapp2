package com.smartdevice.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogicEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String username;
	private String gatewaySN;
	private String ifDeviceSN;
	private String ifDeviceName;
	private int ifTypeCode;
	private int ifAttrCode;
	private String ifOperateCode;
	private String ifAttrValue;
	private String ifAttrValue2;
	private String thDeviceSN;
	private String thDeviceName;
	private int thTypeCode;
	private int thAttrCode;
	private String thAttrValue;


	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String name) {
		username = name;
	}
	public String getGatewaySN() {
		return gatewaySN;
	}

	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
	}
	public String getIfDeviceSN() {
		return ifDeviceSN;
	}

	public void setIfDeviceSN(String ifDeviceSN) {
		this.ifDeviceSN = ifDeviceSN;
	}

	public String getIfDeviceName() {
		return ifDeviceName;
	}

	public void setIfDeviceName(String ifDeviceName) {
		this.ifDeviceName = ifDeviceName;
	}
	
	public int getIfTypeCode() {
		return ifTypeCode;
	}

	public void setIfTypeCode(int iftypecode) {
		this.ifTypeCode = iftypecode;
	}
	
	public int getIfAttrCode() {
		return ifAttrCode;
	}

	public void setIfAttrCode(int ifattrcode) {
		this.ifAttrCode = ifattrcode;
	}
	
	
	public String getIfOperateCode() {
		return ifOperateCode;
	}

	public void setIfOperateCode(String operateCode) {
		this.ifOperateCode = operateCode;
	}
	
	public String getIfAttrValue() {
		return ifAttrValue;
	}

	public void setIfAttrValue(String attrValue) {
		this.ifAttrValue = attrValue;
	}
	

	public String getIfAttrValue2() {
		return ifAttrValue2;
	}

	public void setIfAttrValue2(String attrValue) {
		this.ifAttrValue2 = attrValue;
	}
	
	public String getThDeviceSN() {
		return thDeviceSN;
	}
	public void setThDeviceSN(String deviceSN) {
		this.thDeviceSN = deviceSN;
	}

	public String getThDeviceName() {
		return thDeviceName;
	}

	public void setThDeviceName(String deviceName) {
		this.thDeviceName = deviceName;
	}
	
	public int getThTypeCode() {
		return thTypeCode;
	}

	public void setThTypeCode(int typecode) {
		this.thTypeCode = typecode;
	}
	
	public int getThAttrCode() {
		return thAttrCode;
	}

	public void setThAttrCode(int attrcode) {
		this.thAttrCode = attrcode;
	}
	
	
	public String getThAttrValue() {
		return thAttrValue;
	}

	public void setThAttrValue(String attrValue) {
		this.thAttrValue = attrValue;
	}
	
}
