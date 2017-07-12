package com.smartdevice.mode;

import java.io.Serializable;

public class Gateway implements Serializable{

	private static final long serialVersionUID = 1L;
	
	static int count;
	int Id;
	String gatewaySN;
	String gatewayModel;
	String gateType;
	String ssid;
	String wifipwd;
	String hwVer;
	String swVer;
	int operateCap;
	String activetime;
	String Name;
	String gatewayMac;
	int state;
	int isMaster;
	
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public static int getCount() {
		return count;
	}
	public static void setCount(int count) {
		Gateway.count = count;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getGatewaySN() {
		return gatewaySN;
	}
	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
	}
	public String getGatewayModel() {
		return gatewayModel;
	}
	public void setGatewayModel(String gatewayModel) {
		this.gatewayModel = gatewayModel;
	}
	public String getGateType() {
		return gateType;
	}
	public void setGateType(String gateType) {
		this.gateType = gateType;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getWifipwd() {
		return wifipwd;
	}
	public void setWifipwd(String wifipwd) {
		this.wifipwd = wifipwd;
	}
	public String getHwVer() {
		return hwVer;
	}
	public void setHwVer(String hwVer) {
		this.hwVer = hwVer;
	}
	public String getSwVer() {
		return swVer;
	}
	public void setSwVer(String swVer) {
		this.swVer = swVer;
	}
	public int getOperateCap() {
		return operateCap;
	}
	public void setOperateCap(int operateCap) {
		this.operateCap = operateCap;
	}
	public String getActivetime() {
		return activetime;
	}
	public void setActivetime(String activetime) {
		this.activetime = activetime;
	}
	public String getGatewayMac() {
		return gatewayMac;
	}
	public void setGatewayMac(String gatewayMac) {
		this.gatewayMac = gatewayMac;
	}
	public int getIsMaster() {
		return isMaster;
	}
	public void setIsMaster(int isMaster) {
		this.isMaster = isMaster;
	}
}
