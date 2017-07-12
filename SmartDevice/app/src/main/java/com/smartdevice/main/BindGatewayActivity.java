package com.smartdevice.main;

import java.util.ArrayList;
import java.util.List;

import com.smartdevice.control.GatewayControl;
import com.smartdevice.mode.Gateway;
import com.smartdevice.mode.GatewayAdapter;
import com.smartdevice.mode.Session;
import com.smartdevice.utils.ACache;
import com.smartdevice.utils.CustActionBar;
import com.smartdevice.utils.DensityUtil;
import com.smartdevice.utils.ParamsUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BindGatewayActivity extends ActionBarActivity{
   
	private View mAddGatewayView;
	GatewayAdapter gatewayAdapter;
	ListView bindedGatewayListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CustActionBar actionBar = new CustActionBar(getSupportActionBar());
		actionBar.setTitle(R.string.gateway);
		actionBar.setIconOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		setContentView(R.layout.bind_gateway_layout);
		mAddGatewayView = findViewById(R.id.id_bind_gateway);
		
		bindedGatewayListView = (ListView) findViewById(R.id.id_binded_gateway_list);
		gatewayAdapter = new GatewayAdapter(this);
		
		DensityUtil densityUtil = new DensityUtil(this);
		
//		SharedPreferences preferences = this.getSharedPreferences(ParamsUtils.GATEWAY_COUNT, MODE_PRIVATE);
//		int gatewayCount = preferences.getInt(ParamsUtils.GATEWAY_COUNT_CURRENT, 0);
//		ACache mCache = ACache.get(this);
		getGatewayList(); //new ArrayList<>();
//		for (int i = 1; i <= gatewayCount; i++) {
//			
//			String value = mCache.getAsString("gateway" + i);
//			if(value != null){
//				Gateway gateway = new Gateway();
//				gateway.setGatewaySN(value);
//				gatewayList.add(gateway);
//			}
//		}
		
		mAddGatewayView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), AddGatewayActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	public void getGatewayList(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GatewayControl control = new GatewayControl();
				List<Gateway> gatewayList = control.getBindedGateways(Session.getUsername());
				Message msg = new Message();
				msg.obj = gatewayList;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.obj != null){
				 List<Gateway> gatewayList = (List<Gateway>) msg.obj;
				 gatewayAdapter.setGatewayList(gatewayList);
				 bindedGatewayListView.setAdapter(gatewayAdapter); 
			}
		};
	};
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
