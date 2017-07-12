package com.smartdevice.mode;

import java.io.Serializable;

public class LinkDeviceAttribute implements Serializable{
	private static final long serialVersionUID = 1L;
	private String gatewaySN;
	private String deviceSN;
	private String deviceName;
	private DeviceType deviceType;;
	private DeviceAttribute deviceAttr;
	public String getGatewaySN() {
		return gatewaySN;
	}
	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
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
	public DeviceAttribute getDeviceAttr() {
		return deviceAttr;
	}
	public void setDeviceAttr(DeviceAttribute deviceAttr) {
		this.deviceAttr = deviceAttr;
	}
	public DeviceType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
}
