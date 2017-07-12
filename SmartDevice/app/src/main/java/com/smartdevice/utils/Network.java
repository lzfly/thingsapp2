package com.smartdevice.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
	
	private Boolean connectedState = true;
	ConnectivityManager manager = null;
	
	public Network(Context context){
		if(manager == null){
			manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if(networkInfo != null){
			connectedState = networkInfo.isConnected();
		}else {
			connectedState = false;
		}
	}
	
	public Boolean isConnected(){
		return connectedState;
	}

}
