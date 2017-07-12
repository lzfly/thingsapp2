package com.smartdevice.utils;

import android.util.Log;

public class LogUtil {

	public final static boolean loggable = true;
	public static boolean info  = true;
	public static boolean debug = true;
	public static boolean error = true;
	public static boolean warn  = true;
	
	
	public static void i(String tag, String msg){
		if(loggable && info)
		Log.i(tag, msg);
	}
	
	public static  void d(String tag, String msg){
		if(loggable && debug)
		Log.d(tag, msg);
	}
	
	public static  void w(String tag, String msg){
		if(loggable && warn)
		Log.d(tag, msg);
	}
	
	public static  void e(String tag, String msg){
		if(loggable && error)
		Log.d(tag, msg);
	}
}
