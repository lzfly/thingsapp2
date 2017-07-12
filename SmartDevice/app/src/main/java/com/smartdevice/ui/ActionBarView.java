package com.smartdevice.ui;

import com.smartdevice.main.R;

import android.support.v7.app.ActionBar;

public class ActionBarView {
	
	public static void setActionBarStyle(ActionBar actionBar){
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.binding_gateway);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
	}

}
