package com.smartdevice.mode;

public class Session {

	static String sessionId;
	static int id;
	static String username;
	
	public static String getSessionId() {
		return sessionId;
	}
	public static void setSessionId(String sessionId) {
		Session.sessionId = sessionId;
	}
	public static int getId() {
		return id;
	}
	public static void setId(int id) {
		Session.id = id;
	}
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		Session.username = username;
	}
}
