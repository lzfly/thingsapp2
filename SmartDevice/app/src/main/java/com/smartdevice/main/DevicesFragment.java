package com.smartdevice.main;

import java.util.List;

import com.smartdevice.mode.Device;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DevicesFragment extends AllDevicesFragment{

	private LinearLayout linearLayout;
	
	public DevicesFragment(List<Device> devices) {
		super(devices);
		// TODO Auto-generated constructor stub
	}
	
    public DevicesFragment(Context context) {
		// TODO Auto-generated constructor stub
    	super(context);
	}
    
    public DevicesFragment(){
    	super();
    }

	@Override
    public View onCreateView(LayoutInflater inflater,
    		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	View view = (LinearLayout) inflater.inflate(R.layout.fragment_devices_layout, container, false);
    	return super.onCreateView(inflater, (ViewGroup) view, savedInstanceState);
    }
}
