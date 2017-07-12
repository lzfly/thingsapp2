package com.smartdevice.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Device implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String deviceId;
	private String deviceSN;
	private String deviceName;
	private String deviceModel;
	private String deviceVer;
	private String protocol;
	private String gatewaySN;
	private int isOnline;
	private String activetime;
	//private int deviceType;
	private DeviceType deviceType;
	private DeviceAttribute deviceAttr;
	
	private List<DeviceAttribute> attrList;

	public Device(){
		deviceSN = new String();
		deviceName = new String();
		//deviceType = new DeviceType();
		deviceAttr = new DeviceAttribute();
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public DeviceAttribute getDeviceAttr() {
		return deviceAttr;
	}
	public void setDeviceAttr(DeviceAttribute deviceAttr) {
		this.deviceAttr = deviceAttr;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDeviceSN() {
		return deviceSN;
	}

	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceVer() {
		return deviceVer;
	}

	public void setDeviceVer(String deviceVer) {
		this.deviceVer = deviceVer;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getGatewaySN() {
		return gatewaySN;
	}

	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public String getActivetime() {
		return activetime;
	}

	public void setActivetime(String activetime) {
		this.activetime = activetime;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public void setAttrListItem(DeviceAttribute attribute){
		if(attrList == null){
			attrList = new ArrayList<>();
		}
		
		attrList.add(attribute);
	}

	public List<DeviceAttribute> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<DeviceAttribute> attrList) {
		this.attrList = attrList;
	}
	
	
}
