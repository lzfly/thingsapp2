package com.smartdevice.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomBindDevice implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String roomName;
	private String deviceSN;
	private String deviceName;
	private int    typeCode;
	private String gatewaySN;
	private String username;
    private boolean flag = true; //true data;false add―button


	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String name) {
		this.roomName = name;
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
