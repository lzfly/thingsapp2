package com.smartdevice.mode;

import java.io.Serializable;

public class Room implements Serializable{

	private static final long serialVersionUID = 1L;
	
	static int count;
	private int Id;
	private String roomName;
	private String username;

	
	
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String name) {
		roomName = name;
	}

	public String getUserName() {
		return username;
	}
	public void setUserName(String name) {
		username = name;
	}
	
	public static int getCount() {
		return count;
	}
	public static void setCount(int count) {
		Room.count = count;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}

}