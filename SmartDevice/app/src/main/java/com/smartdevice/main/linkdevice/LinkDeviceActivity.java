package com.smartdevice.main.linkdevice;

import com.smartdevice.main.R;
import com.smartdevice.utils.CustActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class LinkDeviceActivity extends ActionBarActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){
		setContentView(R.layout.link_device_layout);
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.getIconView().setOnClickListener(this);
		actionBar.setTitle(R.string.link_device);
		actionBar.getMenuTxView().setText(R.string.link_device_all);
		actionBar.getMenuTxView().setVisibility(View.VISIBLE);
		actionBar.getMenuTxView().setOnClickListener(this);
		View addLinkView = findViewById(R.id.id_link_device_add_button);
		addLinkView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_actionbar_icon:
			 finish();
			break;
		case R.id.id_link_device_add_button:
			Intent intent = new Intent(this, CreateLinkDeviceActivity.class);
			startActivity(intent);
			break;
		case R.id.id_actionbar_menu_tx:
			Intent intentAll = new Intent(this, CreatedLinkDeviceActivity.class);
			startActivity(intentAll);
			break;
		default:
			break;
		}
	}
}
