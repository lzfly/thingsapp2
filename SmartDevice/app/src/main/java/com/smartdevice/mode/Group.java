package com.smartdevice.mode;

import java.io.Serializable;

public class Group implements Serializable{

	private static final long serialVersionUID = 1L;
	
	static int count;
	private int Id;
	private String groupName;
	private String username;

	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String name) {
		groupName = name;
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
		Group.count = count;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}

}